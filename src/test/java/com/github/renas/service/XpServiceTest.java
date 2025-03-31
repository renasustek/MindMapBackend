package com.github.renas.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.github.renas.persistance.UserRepository;
import com.github.renas.persistance.XpPointsRepo;
import com.github.renas.persistance.models.UserDao;
import com.github.renas.persistance.models.XpDao;
import com.github.renas.gamification.Rewards;
import com.github.renas.service.XpService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

class XpServiceTest {

    @Mock
    private XpPointsRepo xpPointsRepo;

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private XpService xpService;





    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockSecurityContext();
    }

    private void mockSecurityContext() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(UUID.randomUUID().toString());
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testAddXP() {
        UUID userId = UUID.randomUUID();
        UserDao userDao = new UserDao();
        userDao.setUsername("testUser");

        XpDao existingXp = new XpDao();
        existingXp.setXpPoints(100);

        XpDao updatedXp = new XpDao();
        updatedXp.setXpPoints(150);

        when(userRepo.findById(userId)).thenReturn(Optional.of(userDao));
        when(xpPointsRepo.findById(userId)).thenReturn(Optional.of(existingXp));
        when(xpPointsRepo.save(any(XpDao.class))).thenReturn(updatedXp);

        Rewards rewards = Rewards.TASK_COMPLETED;
        XpDao result = xpService.addXP(rewards);

        assertEquals(150, result.getXpPoints());
        verify(xpPointsRepo).save(any(XpDao.class));
    }

    @Test
    void testGetUserLevel() {
        UUID userId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        XpDao xpDao = new XpDao();
        xpDao.setXpPoints(10000);  // 10000 XP points

        when(xpPointsRepo.findById(userId)).thenReturn(Optional.of(xpDao));

        int level = xpService.getUserLevel();
        assertEquals(10, level);  // Correct expected value according to your level formula
    }


    @Test
    void testGetLeaderboard() {
        List<XpDao> xpList = new ArrayList<>();
        XpDao user1 = new XpDao();
        user1.setUsername("user1");
        user1.setXpPoints(200);
        XpDao user2 = new XpDao();
        user2.setUsername("user2");
        user2.setXpPoints(100);

        xpList.add(user1);
        xpList.add(user2);

        when(xpPointsRepo.findAllByOrderByXpDesc()).thenReturn(xpList);

        List<Map<String, Object>> leaderboard = xpService.getLeaderboard();
        assertEquals(2, leaderboard.size());
        assertEquals("user1", leaderboard.get(0).get("username"));
        assertEquals(200, leaderboard.get(0).get("xp"));
        assertEquals(1, leaderboard.get(0).get("level"));  // Ensure this is the correct expected level calculation
    }


}
