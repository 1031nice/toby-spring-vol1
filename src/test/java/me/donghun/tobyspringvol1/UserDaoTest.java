package me.donghun.tobyspringvol1;

import me.donghun.tobyspringvol1.user.dao.DaoFactory;
import me.donghun.tobyspringvol1.user.dao.UserDao;
import me.donghun.tobyspringvol1.user.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class UserDaoTest {

    private UserDao dao;

    /*
    JUnit은 @Test가 붙은 메소드를 실행하기 전과 후에 각각
    @Before와 @After가 붙은 메소드를 자동으로 실행
     */
    @Before
    public void setUp(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        this.dao = context.getBean("userDao", UserDao.class);
    }

    /*
    각 테스트 메소드를 실행할 때마다 테스트 클래스의 객체가 새로 만들어짐
    -> 각 테스트가 서로 영향을 주지 않고 독립적으로 실행됨을 보장하기 위함
    -> 인스턴스 변수도 부담 없이 사용 가능
     */
    @Test
    public void andAndGet() throws SQLException, ClassNotFoundException {
        User user1 = new User("user1", "name1", "pass1");
        User user2 = new User("user2", "name2", "pass2");

        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.add(user1);
        dao.add(user2);
        assertThat(dao.getCount(), is(2));

        User userget1 = dao.get(user1.getId());
        assertThat(user1.getName(), is(userget1.getName()));
        assertThat(user1.getPassword(), is(userget1.getPassword()));

        User userget2 = dao.get(user2.getId());
        assertThat(user2.getName(), is(userget2.getName()));
        assertThat(user2.getPassword(), is(userget2.getPassword()));

    }

    @Test
    public void count() throws SQLException, ClassNotFoundException {
        User user1 = new User("id1", "user1", "pass1");
        User user2 = new User("id2", "user2", "pass2");
        User user3 = new User("id3", "user3", "pass3");

        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.add(user1);
        assertThat(dao.getCount(), is(1));

        dao.add(user2);
        assertThat(dao.getCount(), is(2));

        dao.add(user3);
        assertThat(dao.getCount(), is(3));
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getUserFailure() throws SQLException, ClassNotFoundException {
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.get("unknown_id");
    }
}