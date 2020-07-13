package me.donghun.tobyspringvol1;

import me.donghun.tobyspringvol1.user.dao.UserDao;
import me.donghun.tobyspringvol1.user.domain.User;

import java.sql.SQLException;

public class Test {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        UserDao dao = new UserDao();

        User user = new User();
        user.setId("donghun");
        user.setName("강동훈");
        user.setPassword("1031");

        dao.add(user);

        System.out.println(user.getId() + " 등록 성공");

        User user2 = dao.get("donghun");
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());
        System.out.println(user2.getId() + " 조회 성공");
    }

}
