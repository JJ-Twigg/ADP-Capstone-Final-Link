package com.college.service;

import com.college.domain.subclasses.FoodWorker;

import java.util.List;

public interface IFoodWorkerService extends IService <FoodWorker, Integer> {

    List<FoodWorker> getAll();

}
