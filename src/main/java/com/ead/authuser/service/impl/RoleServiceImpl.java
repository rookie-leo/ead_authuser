package com.ead.authuser.service.impl;

import com.ead.authuser.repositories.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl {

    final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
}
