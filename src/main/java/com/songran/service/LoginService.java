package com.songran.service;

import javax.servlet.http.HttpSession;

import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.songran.helper.MessageManager;
import com.songran.model.Payment;
import com.songran.model.User;

@Service
public class LoginService {

	@Autowired
	UserService userService;

	@Autowired
	PaymentService paymentService;

	public String doLogin(User user, HttpSession session) throws Exception {
		try {
			System.out.println(user.getUserName()+user.getPassword());
			User user2 = userService.getUserByUserNameAndPassword(user.getUserName(), user.getPassword());
			System.out.println(user.getUserName()+user.getPassword());
			if (user2 == null) {
				throw new Exception("Username and password not match...");
			} else if (user2.isAdmin()) {
				session.setAttribute("adminId", user2.getUserId());
				return "redirect:/admin/home";
			} else if (!user2.isEnabled()) {
				session.setAttribute("userId", user2.getUserId());
				throw new Exception("User is disabled please complete the payment...");
				
			}else {
				Payment payment = paymentService.getPaymentByUserAndIsLastPayment(user2, true);
				LocalDateTime currentDate = LocalDateTime.now();
				LocalDateTime subscriptionDate = LocalDateTime.parse(payment.getSubscriptionDate());
				Instant endInterval = new Instant(subscriptionDate.toDateTime());
				Instant startInterval = new Instant(currentDate.toDateTime());
				Duration duration = new Duration(startInterval, endInterval);
				if (duration.getStandardSeconds() > 0 && payment.isLastPayment()) {
					return "redirect:/user/home";
				} else {
					return "signin";
				}
			}			

		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("message", new MessageManager(e.getMessage(), "alert-danger"));
			return "signin";
		}

		
	}
}
