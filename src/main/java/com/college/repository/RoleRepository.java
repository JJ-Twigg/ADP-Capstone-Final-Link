package com.college.repository;

import com.college.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    // Checks if any UserRole references this role
    @Query("SELECT CASE WHEN COUNT(ur) > 0 THEN true ELSE false END FROM UserRole ur WHERE ur.role.id = :roleId")
    boolean existsUserRoleByRoleId(@Param("roleId") Integer roleId);

    Role findByRoleName(String roleName);
}
