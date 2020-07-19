package me.donghun.tobyspringvol1;

import me.donghun.tobyspringvol1.user.dao.ConnectionMaker;
import me.donghun.tobyspringvol1.user.dao.DaoFactory;
import me.donghun.tobyspringvol1.user.dao.KakaoConnectionMaker;
import me.donghun.tobyspringvol1.user.dao.UserDao;
import me.donghun.tobyspringvol1.user.domain.User;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

public class UserDaoTest {

    // 모든 클래스에는 자신을 entry point로 설정해 직접 실행이 가능하게 해주는 static method main()이 있다.
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        // UserDao를 가져오는 메소드는 하나뿐인데 왜 굳이 이름을 사용할까?
        // 빈은 클래스가 아니라 객체이다. 따라서 같은 클래스라도 여러 개의 객체가 IoC 컨테이너에 담길 수 있다.
        // 생성 방식 또는 구성이 다른 UserDao 객체가 추가될 수 있으므로 이름을 사용한다.
        UserDao userDao = context.getBean("userDao", UserDao.class);
//        Object userDao = context.getBean("userDao"); // 클래스를 안주면 Object 반환
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
