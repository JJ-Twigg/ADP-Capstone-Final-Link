/**
 * MaintenanceWorkerRepository.java
 * Maintenance Worker Repository
 * Author: Muaath Slamong (230074138)
 * Date: 22 May 2025
 */
package com.college.repository;

import com.college.domain.MaintenanceWorker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaintenanceWorkerRepository extends JpaRepository<MaintenanceWorker, Integer> {
}
