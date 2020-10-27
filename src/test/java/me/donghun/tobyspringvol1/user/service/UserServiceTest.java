package me.donghun.tobyspringvol1.user.service;

import me.donghun.tobyspringvol1.user.dao.UserDao;
import me.donghun.tobyspringvol1.user.dao.UserDaoJdbc;
import me.donghun.tobyspringvol1.user.domain.Level;
import me.donghun.tobyspringvol1.user.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static me.donghun.tobyspringvol1.user.service.UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER;
import static me.donghun.tobyspringvol1.user.service.UserServiceImpl.MIN_RECOMMEND_FOR_GOLD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class UserServiceTest {

    static class TestUserService extends UserServiceImpl {
        private String id;
        public TestUserService(String id) {
            this.id = id;
        }
        @Override
        public void upgradeLevel(User user){
            if(user.getId().equals(this.id)) throw new TestUserServiceException();
            super.upgradeLevel(user);
        }
    }

    static class MockMailSender implements MailSender {
        private List<String> requests = new ArrayList<>();

        public List<String> getRequests(){
            return requests;
        }

        @Override
        public void send(SimpleMailMessage mailMessage) throws MailException {
            requests.add(mailMessage.getTo()[0]);
        }

        @Override
        public void send(SimpleMailMessage... mailMessages) throws MailException {

        }
    }

    static class MockUserDao implements UserDao {
        private List<User> users;
        private List<User> updated = new ArrayList<>();

        public MockUserDao(List<User> users) {
            this.users = users;
        }
        public List<User> getUpdated() {
            return this.updated;
        }
        public List<User> getAll(){ // stub
            return this.users;
        }
        public void update(User user){ // mock
            updated.add(user);
        }
        public void deleteAll() {
            throw new UnsupportedOperationException();
        }
        public int getCount() {
            throw new UnsupportedOperationException();
        }
        public void add(User user) {
            throw new UnsupportedOperationException();
        }
        public User get(String id) {
            throw new UnsupportedOperationException();
        }
    }

    static class TestUserServiceException extends RuntimeException {}

    @Autowired
    ApplicationContext context;

    @Autowired
    UserService userService;

    // 굳이 구체적인 클래스를 가져온 이유는
    // 메일 서비스를 테스트할 때 목 오브젝트를 이용해 수동 DI를 적용(수정자 메소드 이용)해야하기 때문이다.
    @Autowired
    UserServiceImpl userServiceImpl;

    @Autowired
    UserDaoJdbc userDao;

    @Autowired
    DataSource dataSource;

    @Autowired
    PlatformTransactionManager transactionManager;

    @Autowired
    MailSender mailSender;

    List<User> users;

    @Before
    public void setUp(){
        users = Arrays.asList(
                new User("user1", "name1", "pass1", "user1@gmail.com", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER - 1, 0),
                new User("user2", "name2", "pass2", "user2@gmail.com", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),
                new User("user3", "name3", "pass3", "user3@gmail.com", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD - 1),
                new User("user4", "name4", "pass4", "user4@gmail.com", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD),
                new User("user5", "name5", "pass5", "user5@gmail.com", Level.GOLD, 100, Integer.MAX_VALUE)
        );
    }

    @Test
    @DirtiesContext
    public void upgradeAllOrNothing() throws Exception {
        TestUserService testUserService = new TestUserService(users.get(3).getId());
        // 수동 DI
        testUserService.setUserDao(this.userDao);
        testUserService.setMailSender(mailSender);

        ProxyFactoryBean txProxyFactoryBean = context.getBean("&userService", ProxyFactoryBean.class);
        txProxyFactoryBean.setTarget(testUserService);
        UserService txUserService = (UserService) txProxyFactoryBean.getObject();

//        TransactionHandler txHandler = new TransactionHandler();
//        txHandler.setTarget(testUserService);
//        txHandler.setTransactionManager(transactionManager);
//        txHandler.setPattern("upgradeLevels");
//        UserService txUserService = (UserService) Proxy.newProxyInstance(
//                getClass().getClassLoader(), new Class[]{UserService.class}, txHandler
//        );

//        UserServiceTx txUserService = new UserServiceTx();
//        txUserService.setTransactionManager(transactionManager);
//        txUserService.setUserService(testUserService);

        userDao.deleteAll();
        for(User user : users) userDao.add(user);

        try{
            txUserService.upgradeLevels();
            fail("TestUserServiceException expected");
        }
        catch (TestUserServiceException e){
        }
        checkLevel(users.get(1), false);
    }

    @Test
    public void mockUpgradeLevels() {
        UserServiceImpl userServiceImpl = new UserServiceImpl();

        UserDao mockUserDao = mock(UserDao.class);
        when(mockUserDao.getAll()).thenReturn(this.users);
        userServiceImpl.setUserDao(mockUserDao);

        MailSender mockMailSender = mock(MailSender.class);
        userServiceImpl.setMailSender(mockMailSender);

        userServiceImpl.upgradeLevels();

        verify(mockUserDao, times(2)).update(any(User.class));
        verify(mockUserDao).update(users.get(1));
        assertThat(users.get(1).getLevel()).isEqualTo(Level.SILVER);
        verify(mockUserDao).update(users.get(3));
        assertThat(users.get(3).getLevel()).isEqualTo(Level.GOLD);

        ArgumentCaptor<SimpleMailMessage> mailMessageArg = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mockMailSender, times(2)).send(mailMessageArg.capture());
        List<SimpleMailMessage> mailMessages = mailMessageArg.getAllValues();
        assertThat(mailMessages.get(0).getTo()[0]).isEqualTo(users.get(1).getEmail());
        assertThat(mailMessages.get(1).getTo()[0]).isEqualTo(users.get(3).getEmail());
    }

    @Test
    @DirtiesContext // 컨텍스트의 DI 설정을 변경하는 테스트임을 뜻한다
    public void upgradeLevels() throws Exception {
        // 고립된 테스트에서는 테스트 대상 오브젝트를 직접 생성하면 된다.
        // 스프링 컨테이너의 도움(DI)을 받을 필요가 없기 때문이다.
        UserServiceImpl userServiceImpl = new UserServiceImpl();

        MockUserDao mockUserDao = new MockUserDao(this.users);
        userServiceImpl.setUserDao(mockUserDao);

        MockMailSender mockMailSender = new MockMailSender();
        userServiceImpl.setMailSender(mockMailSender); // DI

        userServiceImpl.upgradeLevels();

//        checkLevel(users.get(0), false);
//        checkLevel(users.get(1), true);
//        checkLevel(users.get(2), false);
//        checkLevel(users.get(3), true);
//        checkLevel(users.get(4), false);

        List<User> updated = mockUserDao.getUpdated();
        assertThat(updated.size()).isEqualTo(2);
        checkUserAndLevel(updated.get(0), "user2", Level.SILVER);
        checkUserAndLevel(updated.get(1), "user4", Level.GOLD);

        List<String> requests = mockMailSender.getRequests();
        assertThat(requests.size()).isEqualTo(2);
        assertThat(requests.get(0)).isEqualTo(users.get(1).getEmail());
        assertThat(requests.get(1)).isEqualTo(users.get(3).getEmail());
    }

    private void checkUserAndLevel(User updated, String expectedId, Level expectedLevel) {
        assertThat(updated.getId()).isEqualTo(expectedId);
        assertThat(updated.getLevel()).isEqualTo(expectedLevel);
    }

    private void checkLevel(User user, boolean upgraded){
        User userUpdate = userDao.get(user.getId());
        if(upgraded)
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel().nextLevel());
        else
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel());
    }

    @Test
    public void add() {
        userDao.deleteAll();

        User userWithLevel = users.get(4); // level 초기화가 없어야 한다
        User userWithoutLevel = users.get(0); // level이 비어 있으므로 basic으로 설정되어야 한다
        userWithoutLevel.setLevel(null);

        userService.add(userWithLevel);
        userService.add(userWithoutLevel);

        User userWithLevelRead = userDao.get(userWithLevel.getId());
        User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

        assertThat(userWithLevelRead.getLevel()).isEqualTo(userWithLevel.getLevel());
        assertThat(userWithoutLevelRead.getLevel()).isEqualTo(Level.BASIC);
    }

}