package com.github.renas.service;

import com.github.renas.gamification.Rewards;
import com.github.renas.persistance.XpPointsRepo;
import com.github.renas.persistance.models.UserDao;
import com.github.renas.persistance.models.XpDao;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class XpService {
    private final XpPointsRepo xpPointsRepo;


    public XpService(XpPointsRepo xpPointsRepo) {
        this.xpPointsRepo = xpPointsRepo;
    }

    public XpDao addXP(Rewards rewards) {
        XpDao xpDao = new XpDao();
        xpDao.setId(userId());
        xpDao.setXpPoints(rewards.getXpValue() + xpPointsRepo.findById(userId()).get().getXpPoints());
        return xpPointsRepo.save(xpDao);
    }

    private UUID userId() {
        UserDao userDetails = (UserDao) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getId();
    }
}
