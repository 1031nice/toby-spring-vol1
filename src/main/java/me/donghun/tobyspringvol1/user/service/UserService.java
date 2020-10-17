package me.donghun.tobyspringvol1.user.service;

import me.donghun.tobyspringvol1.user.domain.User;

public interface UserService {

    void add(User user);
    void upgradeLevels();

}
