package me.donghun.tobyspringvol1.user.dao;

import me.donghun.tobyspringvol1.user.domain.User;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.sql.DataSource;
import java.sql.*;

public abstract class UserDao {

    private DataSource dataSource;

    public UserDao() {}

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void add(User user) throws ClassNotFoundException, SQLException {
        try(Connection c = dataSource.getConnection();
            PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?, ?, ?)"))
        {
            ps.setString(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPassword());
            ps.executeUpdate();
        }
    }

//    public User get(String id) throws ClassNotFoundException, SQLException {
//        Connection c = dataSource.getConnection();
//
//        PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
//        ps.setString(1, id);
//
//        ResultSet rs = ps.executeQuery();
//
//        User user = null;
//        if(rs.next()) {
//            user = new User();
//            user.setId(rs.getString("id"));
//            user.setName(rs.getString("name"));
//            user.setPassword(rs.getString("password"));
//        }
//
//        rs.close();
//        ps.close();
//        c.close();
//
//        if(user == null) throw new EmptyResultDataAccessException(1);
//
//        return user;
//    }

    public User get(String id) throws ClassNotFoundException, SQLException {
        try(Connection c = dataSource.getConnection();
            PreparedStatement ps = c.prepareStatement("select * from users where id = ?"))
        {
            ps.setString(1, id);
            try(ResultSet rs = ps.executeQuery()) {
                User user = null;
                if (rs.next()) {
                    user = new User();
                    user.setId(rs.getString("id"));
                    user.setName(rs.getString("name"));
                    user.setPassword(rs.getString("password"));
                }
                if (user == null) throw new EmptyResultDataAccessException(1);
                return user;
            }
        }
    }

    
    public void deleteAll() throws SQLException{
        try(Connection c = dataSource.getConnection())
        {
            StatementStrategy strategy = new DeleteAllStatement();
            try(PreparedStatement ps = strategy.makePreparedStatement(c)) {
                ps.executeUpdate();
            }
        }
    }

    public int getCount() throws SQLException {
        try(Connection c = dataSource.getConnection();
            PreparedStatement ps = c.prepareStatement("select count(*) from users");
            ResultSet rs = ps.executeQuery())
        {
            rs.next();
            int count = rs.getInt(1);
            return count;
        }
    }

    abstract protected PreparedStatement makeStatement(Connection c) throws SQLException;
}