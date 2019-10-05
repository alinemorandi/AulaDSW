package com.example.AulaDSW.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.AulaDSW.dto.OrderDTO;
import com.example.AulaDSW.dto.OrderItemDTO;
import com.example.AulaDSW.entities.Order;
import com.example.AulaDSW.entities.OrderItem;
import com.example.AulaDSW.entities.User;
import com.example.AulaDSW.repositories.OrderRepository;
import com.example.AulaDSW.repositories.UserRepository;
import com.example.AulaDSW.services.exceptions.ResourceNotFoundException;

@Service
public class OrderService {
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthService authService;
	
	public List<OrderDTO> findAll(){

		List<Order> list = orderRepository.findAll();
		return list.stream().map(e -> new OrderDTO(e)).collect(Collectors.toList());
	}
	
	public OrderDTO  findById(Long id) {
		Optional<Order> obj = orderRepository.findById(id);
		Order entity = obj.orElseThrow(() -> new ResourceNotFoundException(id));
		authService.validadeOwnOrderOrAdmin(entity);
		return new OrderDTO(entity);
	}
	
	public List<OrderDTO> findByClient() {
		User client = authService.authenticated();
		List<Order> list = orderRepository.findByClient(client);
		return list.stream().map(e -> new OrderDTO(e)).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public List<OrderItemDTO> findItems(Long id) {
		Order order = orderRepository.getOne(id);
		authService.validadeOwnOrderOrAdmin(order);
		Set<OrderItem> set = order.getItems();
		return set.stream().map(e -> new OrderItemDTO(e)).collect(Collectors.toList());
	}
	
	@Transactional(readOnly = true)
	public List<OrderDTO> findByClientId(Long clientId) {
		User client = userRepository.getOne(clientId);
		List<Order> list = orderRepository.findByClient(client);
		return list.stream().map(e -> new OrderDTO(e)).collect(Collectors.toList());
	}

	
}

