package com.example.AulaDSW.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.AulaDSW.dto.PaymentDTO;
import com.example.AulaDSW.entities.Order;
import com.example.AulaDSW.entities.Payment;
import com.example.AulaDSW.repositories.OrderRepository;
import com.example.AulaDSW.repositories.PaymentRepository;
import com.example.AulaDSW.services.exceptions.ResourceNotFoundException;

@Service
public class PaymentService {

	@Autowired
	private PaymentRepository paymentRepository;
	
	@Autowired
	private OrderRepository orderRepository;

	public List<PaymentDTO> findAll() {
		List<Payment> list = paymentRepository.findAll();
		return list.stream().map(e -> new PaymentDTO(e)).collect(Collectors.toList());
	}

	public PaymentDTO findById(Long id) {	
		Optional<Payment> obj = paymentRepository.findById(id);
		Payment entity = obj.orElseThrow(() -> new ResourceNotFoundException(id));
		return new PaymentDTO(entity);
	}
	
	@Transactional
	public PaymentDTO insert(PaymentDTO dto) {		
		Order order = orderRepository.getOne(dto.getOrderId());
		Payment payment = new Payment(null, dto.getMoment(), order);
		order.setPayment(payment);
		orderRepository.save(order);
		return new PaymentDTO(order.getPayment());
	}

	@Transactional
	public PaymentDTO update(Long id, PaymentDTO dto) {
		try {
			Payment entity = paymentRepository.getOne(id);
			updateData(entity, dto);
			entity = paymentRepository.save(entity);
			return new PaymentDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}

	private void updateData(Payment entity, PaymentDTO dto) {
		entity.setMoment(dto.getMoment());
	}

}