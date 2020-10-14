package me.donghun.tobyspringvol1;

import me.donghun.tobyspringvol1.user.dao.DaoFactory;
import me.donghun.tobyspringvol1.user.dao.UserDao;
import me.donghun.tobyspringvol1.user.domain.Level;
import me.donghun.tobyspringvol1.user.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class UserDaoTest {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private UserDao dao;
    private User user1;
    private User user2;
    private User user3;

    /*
    JUnit은 @Test가 붙은 메소드를 실행하기 전과 후에 각각
    @Before와 @After가 붙은 메소드를 자동으로 실행
     */
    @Before
    public void setUp(){
        System.out.println(this.context); // context는 항상 동일
        System.out.println(this); // UserDaoTest 객체는 매번 다름
        this.user1 = new User("user1", "name1", "pass1", "abc@gmail.com", Level.BASIC, 1, 0);
        this.user2 = new User("user2", "name2", "pass2", "def@naver.com", Level.SILVER, 55, 10);
        this.user3 = new User("user3", "name3", "pass3", "ghi@kakao.com", Level.GOLD, 100, 40);
    }

    /*
    각 테스트 메소드를 실행할 때마다 테스트 클래스의 객체가 새로 만들어짐
    -> 각 테스트가 서로 영향을 주지 않고 독립적으로 실행됨을 보장하기 위함
    -> 인스턴스 변수도 부담 없이 사용 가능
     */

    @Test
    public void update() {
        dao.deleteAll();

        dao.add(user1);
        dao.add(user2);

        user1.setName("강동훈");
        user1.setPassword("1031");
        user1.setEmail("1031@gmail.com");
        user1.setLevel(Level.GOLD);
        user1.setLogin(1000);
        user1.setRecommend(999);
        dao.update(user1);

        User user1update = dao.get(user1.getId());
        checkSameUser(user1, user1update);
        // 수정하지 않은 사용자의 정보가 그대로인지도 테스트
        // where 절이 빠지면 모든 데이터가 update되기 때문
        User user2update = dao.get(user2.getId());
        checkSameUser(user2, user2update);
    }

    @Test
    public void andAndGet() throws SQLException, ClassNotFoundException {
        dao.deleteAll();
        assertThat(dao.getCount()).isEqualTo(0);

        dao.add(user1);
        dao.add(user2);
        assertThat(dao.getCount()).isEqualTo(2);

        User userget1 = dao.get(user1.getId());
        checkSameUser(userget1, user1);

        User userget2 = dao.get(user2.getId());
        checkSameUser(userget2, user2);
    }

    public void checkSameUser(User user1, User user2){
        assertThat(user1.getId()).isEqualTo(user2.getId());
        assertThat(user1.getName()).isEqualTo(user2.getName());
        assertThat(user1.getPassword()).isEqualTo(user2.getPassword());
        assertThat(user1.getEmail()).isEqualTo(user2.getEmail());
        assertThat(user1.getLevel()).isEqualTo(user2.getLevel());
        assertThat(user1.getLogin()).isEqualTo(user2.getLogin());
        assertThat(user1.getRecommend()).isEqualTo(user2.getRecommend());
    }

    @Test
    public void count() throws SQLException, ClassNotFoundException {
        dao.deleteAll();
        assertThat(dao.getCount()).isEqualTo(0);

        dao.add(user1);
        assertThat(dao.getCount()).isEqualTo(1);

        dao.add(user2);
        assertThat(dao.getCount()).isEqualTo(2);

        dao.add(user3);
        assertThat(dao.getCount()).isEqualTo(3);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getUserFailure() throws SQLException, ClassNotFoundException {
        dao.deleteAll();
        assertThat(dao.getCount()).isEqualTo(0);

        dao.get("unknown_id");
    }
}