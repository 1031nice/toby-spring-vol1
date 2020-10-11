package me.donghun.tobyspringvol1.user.service;

import me.donghun.tobyspringvol1.user.domain.User;

import java.sql.Connection;

public interface UserLevelUpgradePolicy {

    boolean canUpgradeLevel(User user);
    void upgradeLevel(Connection c, User user);

}
