package com.songran.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.songran.model.Payment;
import com.songran.model.User;
import com.songran.repository.PaymentRepository;
import com.songran.repository.UserRepository;

@Service
public class PaymentService {
	@Autowired
	UserRepository userRepository;
	@Autowired
	PaymentRepository paymentRepository;
	private LocalDateTime currentDate;

	public Payment savePayment(Payment payment, HttpSession session) throws Exception {
		long id = (long) session.getAttribute("userId");
		if (id <= 0) {
			throw new Exception("User not found....");
		}
		User user = userRepository.getById(id);
		currentDate = LocalDateTime.now();
		List<Payment> list = paymentRepository.findByUser(user);
		if (list.size() > 0) {
			Payment p = paymentRepository.findByUserAndIsLastPayment(user, true);

			if (p != null) {
				Payment p1 = new Payment();
				p1.setAmount(p.getAmount() + payment.getAmount());
				p1.setLastPayment(true);
				user.setEnabled(true);
				p1.setUser(user);
				p1.setPaymentDate(currentDate.toString());
				p1.setSubscriptionDate(modifySubscriptionDate(p.getSubscriptionDate(), payment.getAmount()));
				p1.setSubscriptionType(p.getSubscriptionType() + "And" + payment.getSubscriptionType());

				for (Payment payment2 : list) {
					payment2.setLastPayment(false);
				}
				paymentRepository.saveAll(list);
				paymentRepository.save(p1);
			}
			System.out.println("payment done");

		} else {
			payment.setPaymentDate(currentDate.toString());
			payment.setSubscriptionDate(addSubscriptionDate(payment.getAmount()));
			user.setEnabled(true);
			payment.setUser(user);
			payment.setLastPayment(true);
			paymentRepository.save(payment);
		}

		return payment;
	}

	private String modifySubscriptionDate(String subscriptionDate, double amount) {
		LocalDateTime olddDateTime = LocalDateTime.parse(subscriptionDate);
		if (amount == 5) {
			return olddDateTime.plusDays(1).toString();
		}
		if (amount == 25) {
			return olddDateTime.plusWeeks(1).toString();
		}
		if (amount == 50) {
			return olddDateTime.plusMonths(1).toString();
		}
		if (amount == 100) {
			return olddDateTime.plusYears(1).toString();
		}
		return olddDateTime.toString();
	}

	private String addSubscriptionDate(double amount) {
		currentDate = LocalDateTime.now();
		if (amount == 5) {
			return currentDate.plusDays(1).toString();
		}
		if (amount == 25) {
			return currentDate.plusWeeks(1).toString();
		}
		if (amount == 50) {
			return currentDate.plusMonths(1).toString();
		}
		if (amount == 100) {
			return currentDate.plusYears(1).toString();
		}
		return currentDate.toString();
	}

	public Payment getPaymentByUserAndIsLastPayment(User user, boolean isLastPayment) throws Exception {
		Payment payment = paymentRepository.findByUserAndIsLastPayment(user, isLastPayment);
		if (payment == null) {
			throw new Exception("Payment not avilable ....");
		}
		return payment;
	}

	public List<Payment> getAllPaymentByUser(HttpSession session) throws Exception {
		long id = (long) session.getAttribute("userId");
		if (id <= 0) {
			throw new Exception("User not found....");
		}
		User user = userRepository.getById(id);
		List<Payment> list = paymentRepository.findByUser(user);
		if (list.size() <= 0) {
			throw new Exception("list is empty ....");
		}
		return list;
	}
	public List<Payment> getAllPaymentByUserForAdmin(long id) throws Exception {
		
		User user = userRepository.getById(id);
		List<Payment> list = paymentRepository.findByUser(user);
		if (list.size() <= 0) {
			throw new Exception("list is empty ....");
		}
		
		return list;
	}

	public List<Payment> getAllPaymentByIsLastPayment(boolean isLastPayment) throws Exception {
		List<Payment> list = paymentRepository.findByIsLastPayment(true);
		if (list.size() <= 0) {
			throw new Exception("list is empty ....");
		}
		return list;

	}

}
