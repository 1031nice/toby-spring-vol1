package me.donghun.tobyspringvol1.user.dao;

import java.sql.Connection;
import java.sql.SQLException;

public class NaverUserDao extends UserDao {
    @Override
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        return null;
    }

}
