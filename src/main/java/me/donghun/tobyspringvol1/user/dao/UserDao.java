package me.donghun.tobyspringvol1.user.dao;

import me.donghun.tobyspringvol1.user.domain.User;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.sql.DataSource;
import java.sql.*;

public class UserDao {

    private DataSource dataSource;

    public UserDao() {}

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // 내부 클래스에서 외부의 변수를 사용할 때는 외부 변수가 (유사)final이어야 한다
    public void add(final User user) throws ClassNotFoundException, SQLException {
        class AddStatement implements StatementStrategy {
            @Override
            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?, ?, ?)");
                ps.setString(1, user.getId());
                ps.setString(2, user.getName());
                ps.setString(3, user.getPassword());
                return ps;
            }
        }
        StatementStrategy st = new AddStatement();
        jdbcContextWithStatementStrategy(st);
    }

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
        StatementStrategy st = new DeleteAllStatement(); // deleteAll이 클라이언트
        jdbcContextWithStatementStrategy(st);
    }

    public void jdbcContextWithStatementStrategy(StatementStrategy stmt) throws SQLException{
        try(Connection c = dataSource.getConnection())
        {
            try(PreparedStatement ps = stmt.makePreparedStatement(c)) {
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

}