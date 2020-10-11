package me.donghun.tobyspringvol1.user.service;

import me.donghun.tobyspringvol1.user.domain.Level;
import me.donghun.tobyspringvol1.user.domain.User;

import java.sql.Connection;

import static me.donghun.tobyspringvol1.user.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static me.donghun.tobyspringvol1.user.service.UserService.MIN_RECOMMEND_FOR_GOLD;

public class UserLevelDefaultPolicy implements UserLevelUpgradePolicy {

    @Override
    public void upgradeLevel(Connection c, User user) {
        user.upgradeLevel();
    }

    @Override
    public boolean canUpgradeLevel(User user) {
        Level currentLevel = user.getLevel();
        switch (currentLevel){
            case BASIC: return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
            case SILVER: return (user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD);
            case GOLD: return false;
            // 새로운 레벨이 추가되고 로직을 수정하지 않으면 에러가 나서 확인할 수 있다.
            default: throw new IllegalArgumentException("Unknown Level: " + currentLevel);
        }
    }
}
