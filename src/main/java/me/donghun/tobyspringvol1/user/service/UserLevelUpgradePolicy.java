package me.donghun.tobyspringvol1.user.service;

import me.donghun.tobyspringvol1.user.domain.User;

public interface UserLevelUpgradePolicy {

    boolean canUpgradeLevel(User user);
    void upgradeLevel(User user);

}
