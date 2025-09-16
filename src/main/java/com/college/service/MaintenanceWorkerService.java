package com.college.service;

import com.college.domain.MaintenanceWorker;
import com.college.repository.MaintenanceWorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MaintenanceWorkerService implements IMaintenanceWorkerService {

    private final MaintenanceWorkerRepository repository;

    @Autowired
    public MaintenanceWorkerService(MaintenanceWorkerRepository repository) {
        this.repository = repository;
    }

    @Override
    public MaintenanceWorker create(MaintenanceWorker worker) {
        return repository.save(worker);
    }

    @Override
    public MaintenanceWorker read(Integer id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public MaintenanceWorker update(MaintenanceWorker worker) {
        return repository.save(worker);
    }

    @Override
    public boolean delete(Integer id) {
        repository.deleteById(id);
        return true;
    }

    @Override
    public MaintenanceWorker findById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<MaintenanceWorker> getAll() {
        return repository.findAll();
    }
}
