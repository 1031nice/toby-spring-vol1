package me.donghun.tobyspringvol1;

import me.donghun.tobyspringvol1.user.dao.DaoFactory;
import me.donghun.tobyspringvol1.user.dao.UserDao;
import me.donghun.tobyspringvol1.user.domain.User;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class UserDaoTest {

    @Test
    public void andAndGet() throws SQLException, ClassNotFoundException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao userDao = context.getBean("userDao", UserDao.class);
        UserDao dao = new DaoFactory().userDao();

        User user = new User();
        user.setId("donghun");
        user.setName("강동훈");
        user.setPassword("1031");

//        dao.add(user);

//        System.out.println(user.getId() + " 등록 성공");

        User user2 = dao.get(user.getId());

        assertThat(user2.getName(), is(user.getName()));
        assertThat(user2.getPassword(), is(user.getPassword()));
    }
}