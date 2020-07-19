package me.donghun.tobyspringvol1.user.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// application context가 사용할 설정정보
@Configuration
public class DaoFactory {

    @Bean // 객체 생성을 담당하는 IoC용 메소드라는 표시
    public UserDao userDao(){
        return new UserDao(connectionMaker());
    }

    @Bean
    public ConnectionMaker connectionMaker(){
        return new KakaoConnectionMaker();
    }

}
