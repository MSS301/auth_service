package com.auth_svc.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.auth_svc.auth.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {}
