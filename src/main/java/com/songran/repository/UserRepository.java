package com.songran.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.songran.model.User;

public interface UserRepository extends JpaRepository<User, Long>{

	User findByUserName(String userName);
	User findByUserNameAndPassword(String userName ,String password);
	List<User> findByisEnabled(boolean isActive);
	List<User> findByisAdmin(boolean isActive);
}
