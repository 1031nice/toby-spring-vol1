package me.donghun.tobyspringvol1;

import me.donghun.tobyspringvol1.user.dao.ConnectionMaker;
import me.donghun.tobyspringvol1.user.dao.KakaoConnectionMaker;
import me.donghun.tobyspringvol1.user.dao.UserDao;
import me.donghun.tobyspringvol1.user.domain.User;

import java.sql.SQLException;

public class UserDaoTest {

    // 모든 클래스에는 자신을 entry point로 설정해 직접 실행이 가능하게 해주는 static method main()이 있다.
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        ConnectionMaker connectionMaker = new KakaoConnectionMaker();
        // UserDao가 사용할 ConnectionMaker 구현 클래스를 클라이언트에서 결정하고 객체 생성하여 주입
        // 결국 클라이언트가 두 객체 사이에 의존관계를 설정한 것이다
        UserDao dao = new UserDao(connectionMaker);

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
