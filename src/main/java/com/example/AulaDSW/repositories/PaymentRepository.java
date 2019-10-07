package com.example.AulaDSW.repositories;



import org.springframework.data.jpa.repository.JpaRepository;

import com.example.AulaDSW.entities.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
