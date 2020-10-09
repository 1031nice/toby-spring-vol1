package me.donghun.tobyspringvol1.user.service;

import me.donghun.tobyspringvol1.user.dao.UserDao;
import me.donghun.tobyspringvol1.user.domain.Level;
import me.donghun.tobyspringvol1.user.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static me.donghun.tobyspringvol1.user.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static me.donghun.tobyspringvol1.user.service.UserService.MIN_RECOMMEND_FOR_GOLD;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;

    @Autowired
    UserLevelUpgradePolicy userLevelUpgradePolicy;

    List<User> users;
    boolean eventTest;

    @Before
    public void setUp(){
        System.out.println(userLevelUpgradePolicy.getClass());
        if(userLevelUpgradePolicy.getClass()==(UserLevelDefaultPolicy.class)) {
            eventTest = false;
            System.out.println("no event");
        }
        else {
            eventTest = true;
            System.out.println("event");
        }
        if(!eventTest) {
            users = Arrays.asList(
                    new User("user1", "name1", "pass1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER - 1, 0),
                    new User("user2", "name2", "pass2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),
                    new User("user3", "name3", "pass3", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD - 1),
                    new User("user4", "name4", "pass4", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD),
                    new User("user5", "name5", "pass5", Level.GOLD, 100, Integer.MAX_VALUE)
            );
        }
        else {
            users = Arrays.asList(
                    new User("user1", "name1", "pass1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER / 2 - 1, 0),
                    new User("user2", "name2", "pass2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER / 2, 0),
                    new User("user3", "name3", "pass3", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD / 2 - 1),
                    new User("user4", "name4", "pass4", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD / 2),
                    new User("user5", "name5", "pass5", Level.GOLD, 100, Integer.MAX_VALUE)
            );
        }
    }

    @Test
    public void upgradeLevels(){
        userDao.deleteAll();
        for(User user : users) userDao.add(user);

        userService.upgradeLevels();

        checkLevel(users.get(0), false);
        checkLevel(users.get(1), true);
        checkLevel(users.get(2), false);
        checkLevel(users.get(3), true);
        checkLevel(users.get(4), false);
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