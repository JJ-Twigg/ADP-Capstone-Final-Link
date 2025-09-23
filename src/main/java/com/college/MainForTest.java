package com.college;

import com.college.domain.subclasses.FoodWorker;
import com.college.factory.FoodWorkerFactory;

public class MainForTest {
    public static void main(String[] args) {

//        FoodWorker worker = new FoodWorker.FoodWorkerBuilder()
//                .id(1)
//                .type("Chef")
//                .specialization("Italian Cuisine")
//                .build();
//
//        System.out.println("a " + worker);

        FoodWorker worker1 = FoodWorkerFactory.createFoodWorker("Chef", "Italian Cuisine");
        System.out.println(worker1);
    }
}
