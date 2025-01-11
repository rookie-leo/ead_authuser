package com.ead.authuser.service;

import com.ead.authuser.enums.RoleType;
import com.ead.authuser.models.RoleModel;

public interface RoleService {

    RoleModel findByRoleName(RoleType name);

}
