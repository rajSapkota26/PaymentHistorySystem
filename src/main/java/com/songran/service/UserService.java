package com.songran.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.songran.model.User;
import com.songran.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;

	public User saveUser(User user) throws Exception {
		User existUser = userRepository.findByUserName(user.getUserName());
		if (existUser != null) {
			throw new Exception("User already exist ....");
		}
		existUser = userRepository.save(user);
		return existUser;
	}

	public User getUserByUserName(String username) throws Exception {
		User existUser = userRepository.findByUserName(username);
		if (existUser == null) {
			throw new Exception("User not exist ....");
		}
		return existUser;
	}

	public User getUserByUserNameAndPassword(String username, String password) throws Exception {
		User existUser = userRepository.findByUserNameAndPassword(username, password);
		if (existUser == null) {
			throw new Exception("User not exist ....");
		}
		return existUser;
	}

	public User getUserByUserId(HttpSession session) throws Exception {
		if (session.getAttribute("userId") == (null)) {
			throw new Exception("User not exist ....Please login first");
		}
		long userId = (long) session.getAttribute("userId");
		if (userId <= 0) {
			throw new Exception("User not exist ....");
		}
		User existUser = userRepository.getById(userId);
		if (existUser == null) {
			throw new Exception("User not exist ....");
		}
		return existUser;
	}

	// for admin sides
	public User getAdminByAdminId(HttpSession session) throws Exception {
		if (session.getAttribute("adminId") == (null)) {
			throw new Exception("Admin not exist ....Please login first");
		}
		long userId = (long) session.getAttribute("adminId");
		if (userId <= 0) {
			throw new Exception("Admin not exist ....");
		}
		User existUser = userRepository.getById(userId);
		if (existUser == null) {
			throw new Exception("Admin not exist ....");
		}
		return existUser;
	}

	public List<User> getAllUsers(boolean isAdmin) throws Exception {
		List<User> all = userRepository.findByisEnabled(isAdmin);
		if (all.size() <= 0) {
			throw new Exception("User list is empty ....");
		}
		return all;
	}

	public User getUseByUserId(long id) throws Exception {

		User existUser = userRepository.getById(id);
		if (existUser == null) {
			throw new Exception("User not exist ....");
		}
		return existUser;
	}
}
