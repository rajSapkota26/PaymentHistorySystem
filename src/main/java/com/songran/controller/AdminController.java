package com.songran.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.songran.helper.MessageManager;
import com.songran.model.Payment;
import com.songran.model.User;
import com.songran.service.PaymentService;
import com.songran.service.UserService;
@Controller
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	UserService service;
	
	@Autowired
	PaymentService paymentService;
	
	@GetMapping("/home")
	public String homePage(Model model,HttpSession session) {
		try {
			User user = service.getAdminByAdminId(session);		
			model.addAttribute("title", "admin-Home-page");
			model.addAttribute("user",user);
		
			return "admin/adminDashboard";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			session.setAttribute("message", new MessageManager(e.getMessage(), "alert-danger"));
			return "signin";
		}	
			
	}
	@GetMapping("/Allusers")
	public String allUser(Model model,HttpSession session) {
		try {
			User user = service.getAdminByAdminId(session);		
			List<User> allUsers = this.service.getAllUsers(true);
			model.addAttribute("title", "admin-Home-page");
			model.addAttribute("user",user);		
			
			model.addAttribute("users",allUsers);
			
			return "admin/userList";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			session.setAttribute("message", new MessageManager(e.getMessage(), "alert-danger"));
			return "admin/adminDashboard";
		}	
		
	}
	@GetMapping("/AllPayments")
	public String allPayment(Model model,HttpSession session) {
		try {
			User user = service.getAdminByAdminId(session);	
			List<Payment> list = paymentService.getAllPaymentByIsLastPayment(true)	;
			model.addAttribute("title", "admin-Home-page");
			model.addAttribute("payments",list);
			model.addAttribute("user",user);	
			return "admin/paymentList";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			session.setAttribute("message", new MessageManager(e.getMessage(), "alert-danger"));
			return "admin/adminDashboard";
		}	
		
	}
	@GetMapping("/history/{userId}")
	public String paymentHistry(@PathVariable("userId") long userId,Model model,HttpSession session) {
		try {
			User user = service.getAdminByAdminId(session);	
			User user1 = service.getUseByUserId(userId);	
			List<Payment> list = paymentService.getAllPaymentByUserForAdmin(user1.getUserId())	;
			model.addAttribute("title", "admin-Home-page");
			model.addAttribute("list",list);
			model.addAttribute("admin",user);	
			model.addAttribute("user",user1);	
			return "admin/paymentHistory";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			session.setAttribute("message", new MessageManager(e.getMessage(), "alert-danger"));
			return "admin/adminDashboard";
		}	
		
	}
}
