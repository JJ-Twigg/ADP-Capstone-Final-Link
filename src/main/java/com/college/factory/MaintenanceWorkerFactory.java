package com.college.factory;

import com.college.domain.MaintenanceWorker;

public class MaintenanceWorkerFactory {

    public static MaintenanceWorker createMaintenanceWorker(int id, boolean external, String company, String type) {
        return new MaintenanceWorker.MaintenanceWorkerBuilder()
                .setMaintenanceId(id)
                .setExternal(external)
                .setCompany(company)
                .setType(type)
                .build();
    }
}