package com.example.AulaDSW.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.AulaDSW.entities.Order;
import com.example.AulaDSW.entities.User;

public interface OrderRepository extends JpaRepository<Order, Long> {

	List<Order> findByClient(User client);
}
