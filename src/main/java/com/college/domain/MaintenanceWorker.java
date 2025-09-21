package com.college.domain;

import jakarta.persistence.*;

@Entity
@Table(name="MaintenanceWorker")
public class MaintenanceWorker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int maintenanceId;
    private boolean external;
    private String company;
    private String type;




    //FK to Employee
    @OneToOne
    @JoinColumn(name = "employee_id", unique = true) // foreign key to Employee
    private Employee employee;






    protected MaintenanceWorker() {}

    private MaintenanceWorker(MaintenanceWorkerBuilder builder) {
        this.maintenanceId = builder.maintenanceId;
        this.external = builder.external;
        this.company = builder.company;
        this.type = builder.type;
    }

    public int getMaintenanceId() {
        return maintenanceId;
    }

    public boolean isExternal() {
        return external;
    }

    public String getCompany() {
        return company;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "MaintenanceWorker{" +
                "maintenanceId=" + maintenanceId +
                ", external=" + external +
                ", company='" + company + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public static class MaintenanceWorkerBuilder {
        private int maintenanceId;
        private boolean external;
        private String company;
        private String type;

        public MaintenanceWorkerBuilder setMaintenanceId(int maintenanceId) {
            this.maintenanceId = maintenanceId;
            return this;
        }

        public MaintenanceWorkerBuilder setExternal(boolean external) {
            this.external = external;
            return this;
        }

        public MaintenanceWorkerBuilder setCompany(String company) {
            this.company = company;
            return this;
        }

        public MaintenanceWorkerBuilder setType(String type) {
            this.type = type;
            return this;
        }

        public MaintenanceWorker build() {
            return new MaintenanceWorker(this);
        }
    }
}