package com.college.service;

import com.college.domain.subclasses.FoodWorker;
import com.college.repository.FoodWorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodWorkerService implements IFoodWorkerService {

    @Autowired
    private FoodWorkerRepository foodWorkerRepository;

    @Override
    public List<FoodWorker> getAll() {
        return foodWorkerRepository.findAll();
    }

    @Override
    public FoodWorker create(FoodWorker foodWorker) {
        return foodWorkerRepository.save(foodWorker);
    }

    @Override
    public FoodWorker read(Integer id) {
        return foodWorkerRepository.findById(id).orElse(null);
    }

    @Override
    public FoodWorker update(FoodWorker foodWorker) {
        return foodWorkerRepository.save(foodWorker);
    }

    @Override
    public boolean delete(Integer id) {
        if (foodWorkerRepository.existsById(id)) {
            foodWorkerRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
