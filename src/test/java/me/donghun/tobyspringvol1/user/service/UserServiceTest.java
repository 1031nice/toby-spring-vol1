package me.donghun.tobyspringvol1.user.service;

import me.donghun.tobyspringvol1.user.dao.UserDao;
import me.donghun.tobyspringvol1.user.domain.Level;
import me.donghun.tobyspringvol1.user.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;

    List<User> users;

    @Before
    public void setUp(){
        users = Arrays.asList(
                new User("user1", "name1", "pass1", Level.BASIC, 49, 0),
                new User("user2", "name2", "pass2", Level.BASIC, 50, 0),
                new User("user3", "name3", "pass3", Level.SILVER, 60, 29),
                new User("user4", "name4", "pass4", Level.SILVER, 60, 30),
                new User("user5", "name5", "pass5", Level.GOLD, 100, 100)
        );
    }

    @Test
    public void upgradeLevels(){
        userDao.deleteAll();
        for(User user : users) userDao.add(user);

        userService.upgradeLevels();

        checkLevel(users.get(0), Level.BASIC);
        checkLevel(users.get(1), Level.SILVER);
        checkLevel(users.get(2), Level.SILVER);
        checkLevel(users.get(3), Level.GOLD);
        checkLevel(users.get(4), Level.GOLD);
    }

    private void checkLevel(User user, Level expectedLevel){
        User userUpdate = userDao.get(user.getId());
        assertThat(userUpdate.getLevel()).isEqualTo(expectedLevel);
    }

    @Test
    public void add() {
        userDao.deleteAll();

        User userWithLevel = users.get(4); // level 초기화가 없어야 한다
        User userWithoutLevel = users.get(0); // level이 비어 있으므로 basic으로 설정되어야 한다
        userWithoutLevel.setLevel(null);

        userService.add(userWithLevel);
        userService.add(userWithoutLevel);

        User userWithLevelRead = userDao.get(userWithLevel.getId());
        User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

        assertThat(userWithLevelRead.getLevel()).isEqualTo(userWithLevel.getLevel());
        assertThat(userWithoutLevelRead.getLevel()).isEqualTo(Level.BASIC);
    }

}