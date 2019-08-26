package com.example.AulaDSW.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.AulaDSW.entities.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
