package com.github.renas.service;

import com.github.renas.gamification.Rewards;
import com.github.renas.persistance.UserRepository;
import com.github.renas.persistance.XpPointsRepo;
import com.github.renas.persistance.models.UserDao;
import com.github.renas.persistance.models.XpDao;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.github.renas.security.CurrentUserId.getLoggedInUserId;


@Service
public class XpService {
    private final XpPointsRepo xpPointsRepo;
    private final UserRepository userRepo;
    public XpService(XpPointsRepo xpPointsRepo, UserRepository userRepo) {
        this.xpPointsRepo = xpPointsRepo;
        this.userRepo = userRepo;
    }

    public XpDao addXP(Rewards rewards) {
        XpDao xpDao = new XpDao();
        xpDao.setId(getLoggedInUserId());
        xpDao.setUsername(userRepo.findById(getLoggedInUserId()).get().getUsername());
        xpDao.setXpPoints(rewards.getXpValue() +xpPointsRepo.findById(getLoggedInUserId()).get().getXpPoints());
        return xpPointsRepo.save(xpDao);
    }


    private int calculateLevel(int xp) {
        return (int) Math.sqrt(xp / 100.0);  // Example XP to Level formula
    }

    public int getUserLevel() {
        return xpPointsRepo.findById(getLoggedInUserId())
                .map(xpDao -> calculateLevel(xpDao.getXpPoints()))
                .orElse(0);
    }

    public List<Map<String, Object>> getLeaderboard() {
        List<XpDao> users = xpPointsRepo.findAllByOrderByXpDesc();
        List<Map<String, Object>> leaderboard = new ArrayList<>();
        for (XpDao user : users) {
            Map<String, Object> entry = new HashMap<>();
            entry.put("username", user.getUsername());
            entry.put("xp", user.getXpPoints());
            entry.put("level", calculateLevel(user.getXpPoints()));
            leaderboard.add(entry);
        }
        return leaderboard;
    }

}
