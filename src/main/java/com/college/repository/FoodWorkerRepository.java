package com.college.repository;

import com.college.domain.subclasses.FoodWorker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodWorkerRepository extends JpaRepository<FoodWorker, Integer> {
}
