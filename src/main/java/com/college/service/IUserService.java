package com.college.service;


import com.college.domain.User;

import java.util.List;

public interface IUserService extends IService<User, Integer> {
    List<User> getAll();
}