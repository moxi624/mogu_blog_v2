package com.moxi.mogublog.admin.security;

import org.springframework.data.repository.CrudRepository;

import com.moxi.mogublog.xo.entity.Admin;

public interface SecurityUserRepository extends CrudRepository<Admin, Integer> {
    Admin findByUsername(String username);
}

