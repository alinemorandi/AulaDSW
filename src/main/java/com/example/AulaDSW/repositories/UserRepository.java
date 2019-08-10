package com.example.AulaDSW.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.AulaDSW.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
