package com.github.renas.security;

import com.github.renas.persistance.models.UserDao;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public class CurrentUserId {

    public static UUID getLoggedInUserId() {
        UserDao userDetails = (UserDao) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getId();
    }

}
