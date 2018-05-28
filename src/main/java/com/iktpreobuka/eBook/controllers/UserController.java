package com.iktpreobuka.eBook.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.eBook.controllers.util.RESTError;
import com.iktpreobuka.eBook.entities.RoleEntity;
import com.iktpreobuka.eBook.entities.UserEntity;
import com.iktpreobuka.eBook.entities.dto.LoginDTO;
import com.iktpreobuka.eBook.repositories.RoleRepository;
import com.iktpreobuka.eBook.repositories.UserRepository;
import com.iktpreobuka.eBook.security.Views;

@Controller
@RequestMapping("/api/v1/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createUser(@RequestBody LoginDTO log, @RequestParam Integer roleId) {
		if (userRepository.findUserByEmail(log.getEmail()) == null) {
			if (roleRepository.exists(roleId)) {
				UserEntity uEnt = new UserEntity();
				RoleEntity role = roleRepository.findOne(roleId);
				uEnt.setEmail(log.getEmail());
			//uEnt.setPassword(new BCryptPasswordEncoder().encode(log.getPassword()));
				uEnt.setRole(role);
				userRepository.save(uEnt);
				return new ResponseEntity<UserEntity>(uEnt, HttpStatus.OK);
			} else {
				return new ResponseEntity<RESTError>(new RESTError(1, "Role not found"), HttpStatus.NOT_FOUND);
			}
		} else {
			return new ResponseEntity<RESTError>(new RESTError(1, "User with that email already exists"),
					HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAllUsers(Pageable pageable) {
		return new ResponseEntity<Page<UserEntity>>(userRepository.findAll(pageable), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{userId}")
	public ResponseEntity<?> updateUser(@PathVariable Integer userId, @RequestBody LoginDTO log) {
		try {
			if (userRepository.exists(userId)) {
				UserEntity user = userRepository.findOne(userId);
				if (log.getEmail() != null) {
					if (userRepository.findUserByEmail(log.getEmail()) == null) {
						user.setEmail(log.getEmail());
					} else {
						return new ResponseEntity<RESTError>(new RESTError(1, "User with that email already exists"),
								HttpStatus.BAD_REQUEST);
					}
				}
				if (log.getPassword() != null) {
					if (userRepository.findUserByPassword(log.getPassword()) == null) {
						user.setPassword(log.getPassword());
					} else {
						return new ResponseEntity<RESTError>(new RESTError(2, "User with that password already exists"),
								HttpStatus.BAD_REQUEST);
					}
				}
				userRepository.save(user);
				return new ResponseEntity<UserEntity>(user, HttpStatus.OK);
			} else {
				return new ResponseEntity<RESTError>(new RESTError(3, "User not found"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(4, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{userId}")
	public ResponseEntity<?> deleteUser(@PathVariable Integer userId) {
		try {
			if (userRepository.exists(userId)) {
				UserEntity user = userRepository.findOne(userId);
				user.setDeleted(true);
				userRepository.save(user);
				return new ResponseEntity<UserEntity>(user, HttpStatus.OK);
			} else {
				return new ResponseEntity<RESTError>(new RESTError(1, "User not found"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(2, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{userId}/by-id")
	@JsonView(Views.Admin.class)
	public ResponseEntity<?> getUserById(@PathVariable Integer userId) {
		try {
			if (userRepository.exists(userId)) {
				UserEntity user = userRepository.findOne(userId);
				return new ResponseEntity<UserEntity>(user, HttpStatus.OK);
			} else {
				return new ResponseEntity<RESTError>(new RESTError(1, "User not found"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(2, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
