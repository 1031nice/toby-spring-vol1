package me.donghun.tobyspringvol1.user.dao;

// UserDao의 생성 책임을 맡은 팩토리 메소드
public class DaoFactory {

    public UserDao userDao(){
        ConnectionMaker connectionMaker = new KakaoConnectionMaker();
        UserDao userDao = new UserDao(connectionMaker);
        return userDao;
    }

}
