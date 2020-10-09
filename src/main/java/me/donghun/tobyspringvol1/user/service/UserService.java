package me.donghun.tobyspringvol1.user.service;

import me.donghun.tobyspringvol1.user.dao.UserDao;
import me.donghun.tobyspringvol1.user.domain.Level;
import me.donghun.tobyspringvol1.user.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;

public class UserService {

    UserDao userDao;

    public void setUserDao(UserDao userDao){
        this.userDao = userDao;
    }

    public void upgradeLevels(){
        List<User> users = userDao.getAll();
        for(User user : users){
            Boolean changed = null;
            if(user.getLevel() == Level.BASIC && user.getLogin() >= 50){
                user.setLevel(Level.SILVER);
                changed = true;
            }
            else if(user.getLevel() == Level.SILVER && user.getRecommend() >= 30){
                user.setLevel(Level.GOLD);
                changed = true;
            }
            else if (user.getLevel() == Level.GOLD){
                changed = false;
            }
            else{
                changed = false;
            }
            if(changed){
                userDao.update(user);
            }
        }
    }

    public void add(User user){
        if(user.getLevel() == null) user.setLevel(Level.BASIC);
        userDao.add(user);
    }

}
