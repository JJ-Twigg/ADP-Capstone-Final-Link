package com.college.service;

import com.college.domain.CustomRoom;

import java.util.List;

public interface ICustomRoomService extends IService<CustomRoom, Integer> {
    List<CustomRoom> getAll();
}
