package com.github.renas.service;

import com.github.renas.persistance.XpPointsRepo;
import com.github.renas.persistance.models.XpDao;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class XpService {
    private final XpPointsRepo xpPointsRepo;

    public XpService(XpPointsRepo xpPointsRepo) {
        this.xpPointsRepo = xpPointsRepo;
    }

    public int addXP(int points, UUID userId) {
        XpDao xpDao = new XpDao();
        xpDao.setXpPoints(points+xpPointsRepo.findById(userId).get().getXpPoints());
        return xpPointsRepo.save(xpDao).getXpPoints();
    }
}
