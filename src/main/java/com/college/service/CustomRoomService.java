package com.college.service;

import com.college.domain.CustomRoom;
import com.college.factory.CustomRoomFactory;
import com.college.repository.CustomRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomRoomService implements ICustomRoomService {

    @Autowired
    private CustomRoomRepository customRoomRepository;

    @Override
    public List<CustomRoom> getAll() {
        return customRoomRepository.findAll();
    }

    @Override
    public CustomRoom create(CustomRoom customRoom) {
        customRoomRepository.save(customRoom);
        return customRoom;
    }

    @Override
    public CustomRoom read(Integer integer) {
        return customRoomRepository.findById(integer).orElse(null);
    }

    @Override
    public CustomRoom update(CustomRoom customRoom) {
        return customRoomRepository.save(customRoom);
    }

    @Override
    public boolean delete(Integer integer) {
        customRoomRepository.deleteById(integer);
        return true;
    }
}
