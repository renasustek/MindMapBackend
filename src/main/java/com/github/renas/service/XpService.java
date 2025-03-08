package com.github.renas.service;

import com.github.renas.persistance.XpPointsRepo;
import org.springframework.stereotype.Service;

@Service
public class XpService {
    private final XpPointsRepo xpPointsRepo;

    public XpService(XpPointsRepo xpPointsRepo) {
        this.xpPointsRepo = xpPointsRepo;
    }
}
