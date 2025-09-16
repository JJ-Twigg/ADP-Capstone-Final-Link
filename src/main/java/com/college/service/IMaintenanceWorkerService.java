package com.college.service;

import com.college.domain.MaintenanceWorker;
import java.util.List;

public interface IMaintenanceWorkerService extends IService<MaintenanceWorker, Integer> {
    MaintenanceWorker findById(Integer id);
    List<MaintenanceWorker> getAll();
}