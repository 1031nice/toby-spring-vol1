package me.donghun.tobyspringvol1.user.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;

// application context가 사용할 설정정보
@Configuration
public class DaoFactory {

    @Bean // 객체 생성을 담당하는 IoC용 메소드라는 표시
    public UserDao userDao(){
        UserDao userDao = new UserDao();
        userDao.setDataSource(dataSource());
        return userDao;
    }

    @Bean
    public DataSource dataSource(){
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

        dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);
        dataSource.setUrl("jdbc:mysql://192.168.99.100:3306/toby");
        dataSource.setUsername("donghun");
        dataSource.setPassword("1234");

        return dataSource;
    }

}
