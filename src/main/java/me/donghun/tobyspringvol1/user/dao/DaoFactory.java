package me.donghun.tobyspringvol1.user.dao;

// UserDao의 생성 책임을 맡은 팩토리 메소드
public class DaoFactory {

    public UserDao userDao(){
        return new UserDao(connectionMaker());
    }

    public AccountDao accountDao(){
        return new AccountDao(connectionMaker());
    }

    public MessageDao messageDao(){
        return new MessageDao(connectionMaker());
    }
    
    public ConnectionMaker connectionMaker(){
        return new KakaoConnectionMaker();
    }

}
