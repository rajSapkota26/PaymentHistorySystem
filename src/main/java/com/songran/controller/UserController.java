package com.songran.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.songran.helper.MessageManager;
import com.songran.model.Payment;
import com.songran.model.User;
import com.songran.service.PaymentService;
import com.songran.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserService service;
	
	@Autowired
	PaymentService paymentService;
	
	@GetMapping("/home")
	public String homePage(Model model,HttpSession session) {
		try {
			User user = service.getUserByUserId(session);
			Payment payment = paymentService.getPaymentByUserAndIsLastPayment(user, true);
			model.addAttribute("title", "User-Home-page");
			model.addAttribute("user",user);
			model.addAttribute("payment",payment);
			return "user/userDashboard";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			session.setAttribute("message", new MessageManager(e.getMessage(), "alert-danger"));
			return "signin";
		}
		
		
		
	}
	
	@GetMapping("/history")
	public String paymentHistoryPage(Model model,HttpSession session) {
		try {
			User user = service.getUserByUserId(session);
			List<Payment> list = paymentService.getAllPaymentByUser(session);
			model.addAttribute("title", "Payment_History-page");
			model.addAttribute("user",user);
			model.addAttribute("list",list);
			return "user/history";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			session.setAttribute("message", new MessageManager(e.getMessage(), "alert-danger"));
			return "signin";
		}
		
		
		
	}
}
