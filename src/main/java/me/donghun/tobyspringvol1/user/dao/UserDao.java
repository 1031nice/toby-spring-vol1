package me.donghun.tobyspringvol1.user.dao;

import me.donghun.tobyspringvol1.user.domain.Level;
import me.donghun.tobyspringvol1.user.domain.User;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class UserDao {

//    private DataSource dataSource;
//    private JdbcContext jdbcContext;
    private JdbcTemplate jdbcTemplate;
    private RowMapper<User> userMapper =
        new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet resultSet, int i) throws SQLException {
                User user = new User();
                user.setId(resultSet.getString("id"));
                user.setName(resultSet.getString("name"));
                user.setPassword(resultSet.getString("password"));
                user.setLevel(Level.valueOf(resultSet.getInt("level")));
                user.setLogin(resultSet.getInt("login"));
                user.setRecommend(resultSet.getInt("recommend"));
                return user;
            }
        };

    public UserDao() {}

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
//        this.dataSource = dataSource;
    }

    // 내부 클래스에서 외부의 변수를 사용할 때는 외부 변수가 (유사)final이어야 한다
//    public void add(final User user) throws ClassNotFoundException, SQLException {
//        this.jdbcContext.workWithStatementStrategy(
//                new StatementStrategy() {
//                    public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
//                        PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?, ?, ?)");
//                        ps.setString(1, user.getId());
//                        ps.setString(2, user.getName());
//                        ps.setString(3, user.getPassword());
//                        return ps;
//                    }
//            }
//        );
//    }

    public void add(final User user) throws ClassNotFoundException, SQLException {
        this.jdbcTemplate.update("insert into users(id, name, password, level, login, recommend) values(?, ?, ?, ?, ?, ?)",
                user.getId(), user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend());
    }

//    public User get(String id) throws ClassNotFoundException, SQLException {
//        try(Connection c = dataSource.getConnection();
//            PreparedStatement ps = c.prepareStatement("select * from users where id = ?"))
//        {
//            ps.setString(1, id);
//            try(ResultSet rs = ps.executeQuery()) {
//                User user = null;
//                if (rs.next()) {
//                    user = new User();
//                    user.setId(rs.getString("id"));
//                    user.setName(rs.getString("name"));
//                    user.setPassword(rs.getString("password"));
//                }
//                if (user == null) throw new EmptyResultDataAccessException(1);
//                return user;
//            }
//        }
//    }

    public User get(String id){
        return this.jdbcTemplate.queryForObject("select * from users where id = ?",
                new Object[]{id}, this.userMapper);
    }

    public List<User> getAll(){
        return this.jdbcTemplate.query("select * from users order by id",
                this.userMapper);
    }

    
//    public void deleteAll() throws SQLException{
//        this.jdbcContext.executeSql("delete from users");
//    }

    public void deleteAll() {
        this.jdbcTemplate.update(
                new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        return connection.prepareStatement("delete from users");
                    }
                }
        );
//        this.jdbcTemplate.update("delete from users");
    }

//    public void jdbcContextWithStatementStrategy(StatementStrategy stmt) throws SQLException{
//        try(Connection c = dataSource.getConnection())
//        {
//            try(PreparedStatement ps = stmt.makePreparedStatement(c)) {
//                ps.executeUpdate();
//            }
//        }
//    }

//    public int getCount() throws SQLException {
//        try (Connection c = dataSource.getConnection();
//             PreparedStatement ps = c.prepareStatement("select count(*) from users");
//             ResultSet rs = ps.executeQuery()) {
//            rs.next();
//            int count = rs.getInt(1);
//            return count;
//        }
//    }
    public int getCount() throws SQLException {
        return this.jdbcTemplate.query(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                return connection.prepareStatement("select count(*) from users");
            }
        }, new ResultSetExtractor<Integer>() {
            @Override
            public Integer extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                resultSet.next();
                return resultSet.getInt(1);
            }
        });
        //        return this.jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
    }

}