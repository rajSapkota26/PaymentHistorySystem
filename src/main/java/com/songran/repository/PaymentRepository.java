package com.songran.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.songran.model.Payment;
import com.songran.model.User;


public interface PaymentRepository extends JpaRepository<Payment, Long>{

	List<Payment> findByIsLastPayment(boolean value);
	List<Payment> findByUser(User user);
	Payment findByUserAndIsLastPayment(User user,Boolean id);
}
