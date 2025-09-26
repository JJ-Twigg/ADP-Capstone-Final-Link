package com.college.service;

import com.college.domain.Role;
import java.util.List;

public interface IRoleService extends IService<Role, Integer> {
    List<Role> getAll();
}
