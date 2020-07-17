package me.donghun.tobyspringvol1;

import me.donghun.tobyspringvol1.user.dao.ConnectionMaker;
import me.donghun.tobyspringvol1.user.dao.DaoFactory;
import me.donghun.tobyspringvol1.user.dao.KakaoConnectionMaker;
import me.donghun.tobyspringvol1.user.dao.UserDao;
import me.donghun.tobyspringvol1.user.domain.User;

import java.sql.SQLException;

public class UserDaoTest {

    // 모든 클래스에는 자신을 entry point로 설정해 직접 실행이 가능하게 해주는 static method main()이 있다.
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        UserDao dao = new DaoFactory().userDao();

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
