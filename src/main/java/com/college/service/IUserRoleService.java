package com.college.service;

import com.college.domain.UserRole;
import java.util.List;

public interface IUserRoleService extends IService<UserRole, Integer> {
    List<UserRole> getAll();
}
