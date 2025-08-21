package com.cedrus.attendance.repository;

import org.springframework.data.repository.CrudRepository;

import com.cedrus.attendance.entity.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {

	Role findByName(String moduleName);
}
