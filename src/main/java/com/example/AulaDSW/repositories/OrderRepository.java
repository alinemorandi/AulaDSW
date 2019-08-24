package com.example.AulaDSW.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.AulaDSW.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
