package com.college.domain.subclasses;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class FoodWorker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String type;
    private String specialization;

    // Default constructor
    public FoodWorker() {}

    // Constructor for builder
    private FoodWorker(FoodWorkerBuilder builder) {
        this.id = builder.id;
        this.type = builder.type;
        this.specialization = builder.specialization;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    @Override
    public String toString() {
        return "FoodWorker{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", specialization='" + specialization + '\'' +
                '}';
    }

    // Builder
    public static class FoodWorkerBuilder {
        private int id;
        private String type;
        private String specialization;

        public FoodWorkerBuilder() {}

        public FoodWorkerBuilder id(int id) {
            this.id = id;
            return this;
        }

        public FoodWorkerBuilder type(String type) {
            this.type = type;
            return this;
        }

        public FoodWorkerBuilder specialization(String specialization) {
            this.specialization = specialization;
            return this;
        }

        public FoodWorker build() {
            return new FoodWorker(this);
        }
    }
}
