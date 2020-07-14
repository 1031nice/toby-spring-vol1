package me.donghun.tobyspringvol1.user.dao;

import java.sql.Connection;
import java.sql.SQLException;

public class KakaoUserDao extends UserDao {
    @Override
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        return null;
    }
}
