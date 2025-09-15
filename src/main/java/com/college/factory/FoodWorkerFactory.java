
package com.college.factory;
import com.college.domain.subclasses.FoodWorker;

public class FoodWorkerFactory {

    public static FoodWorker createFoodWorker(String type, String specialization) {
        return new FoodWorker.FoodWorkerBuilder()
                .type(type)
                .specialization(specialization)
                .build();
    }

}