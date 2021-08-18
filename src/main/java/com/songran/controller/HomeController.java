package com.songran.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.songran.helper.MessageManager;
import com.songran.model.Payment;
import com.songran.model.User;
import com.songran.service.LoginService;
import com.songran.service.PaymentService;
import com.songran.service.UserService;

@Controller
public class HomeController {
	@Autowired
	UserService service;

	@Autowired
	LoginService loginService;
	@Autowired
	PaymentService paymentService;

	@GetMapping("")
	public String homePage(Model model) {
		model.addAttribute("title", "Home-page");
		return "index";
	}

	@GetMapping("/register")
	public String registerPage(Model model) {
		model.addAttribute("title", "Register-page");
		model.addAttribute("user", new User());
		return "signup";
	}

	@GetMapping("/login")
	public String loginPage(Model model) {
		model.addAttribute("title", "Login-page");
		model.addAttribute("user", new User());
		return "signin";
	}
	@GetMapping("/subscription")
	public String paymentPage(Model model) {
		model.addAttribute("title", "Payment-page");
		model.addAttribute("payment", new Payment());
		return "subscription";
	}
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("userId");
		session.removeAttribute("adminId");
		return "redirect:/";
	}
	

	@PostMapping("/processRegister")
	public String processRegister(@ModelAttribute("user") User user, Model model, HttpSession session) {

		try {
			service.saveUser(user);
			model.addAttribute("title", "Create Account");
			model.addAttribute("user", new User());
			session.setAttribute("message", new MessageManager("Registration success", "alert-success"));
			return "signup";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", new User());
			session.setAttribute("message", new MessageManager(e.getMessage(), "alert-danger"));
			return "signup";
		}

	}

	@PostMapping("/processlogin")
	public String processLogin(@ModelAttribute("user") User user, Model model, HttpSession session) {
		model.addAttribute("title", "Login-page");
		try {
			String login = loginService.doLogin(user, session);
			return login;
		} catch (Exception e) {
					e.printStackTrace();
			model.addAttribute("user", new User());
			session.setAttribute("message", new MessageManager(e.getMessage(), "alert-danger"));
			return "signin";
		}
		
	}
	@PostMapping("/processPayment")
	public String processPayment(@ModelAttribute("payment") Payment payment, Model model, HttpSession session) {
		model.addAttribute("title", "Login-page");
		try {
			paymentService.savePayment(payment, session);
			session.setAttribute("message", new MessageManager("Subscription is done...", "alert-success"));
			return "signin";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", new User());
			session.setAttribute("message", new MessageManager(e.getMessage(), "alert-danger"));
			return "signin";
		}
		
	}

}
