package com.iktpreobuka.eBook.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.eBook.controllers.util.RESTError;
import com.iktpreobuka.eBook.entities.AdminEntity;
import com.iktpreobuka.eBook.entities.UserEntity;
import com.iktpreobuka.eBook.entities.dto.UserRegisterDTO;
import com.iktpreobuka.eBook.enums.UserTitle;
import com.iktpreobuka.eBook.repositories.AdminRepository;
import com.iktpreobuka.eBook.repositories.UserRepository;
import com.iktpreobuka.eBook.security.Views;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private UserRepository userRepository;

	@RequestMapping(method = RequestMethod.POST)
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<?> createAdmin(@RequestBody UserRegisterDTO userDTO) {
		if (userDTO.getTitle().equals(UserTitle.ADMINISTRATOR)) {
			if (adminRepository.findAdminByJmbg(userDTO.getJmbg()) == null) {
				AdminEntity aEnt = new AdminEntity();
				aEnt.setFirstName(userDTO.getFirstName());
				aEnt.setLastName(userDTO.getLastName());
				aEnt.setEmail(userDTO.getEmail());
				aEnt.setJmbg(userDTO.getJmbg());
				aEnt.setTitle(userDTO.getTitle());
				aEnt.setAddress(userDTO.getAddress());
				aEnt.setDateOfBirth(userDTO.getDateOfBirth());
				adminRepository.save(aEnt);
				return new ResponseEntity<AdminEntity>(aEnt, HttpStatus.OK);
			} else {
				return new ResponseEntity<RESTError>(new RESTError(1, "Admin already exists"), HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<RESTError>(new RESTError(2, "Invalid title"), HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	@CrossOrigin(origins = "http://localhost:4200")
	@JsonView(Views.Admin.class)
	public ResponseEntity<?> getAll() {
		return new ResponseEntity<Iterable<AdminEntity>>(adminRepository.findAll(), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{adminId}")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<?> updateAdmin(@PathVariable Integer adminId, @RequestBody UserRegisterDTO userDTO) {
		try {
			if (adminRepository.exists(adminId)) {
				AdminEntity admin = adminRepository.findOne(adminId);
				if (userDTO.getFirstName() != null) {
					admin.setFirstName(userDTO.getFirstName());
				}
				if (userDTO.getLastName() != null) {
					admin.setLastName(userDTO.getLastName());
				}
				if (userDTO.getEmail() != null) {
					admin.setEmail(userDTO.getEmail());
				}
				if (userDTO.getDateOfBirth() != null) {
					admin.setDateOfBirth(userDTO.getDateOfBirth());
				}
				if (userDTO.getTitle() != null) {
					if (userDTO.getTitle().equals(UserTitle.ADMINISTRATOR)) {
						admin.setTitle(userDTO.getTitle());
					} else {
						return new ResponseEntity<RESTError>(new RESTError(1, "Invalid title"), HttpStatus.BAD_REQUEST);
					}
				}
				if (userDTO.getJmbg() != null) {
					admin.setJmbg(userDTO.getJmbg());
				}
				if (userDTO.getAddress() != null) {
					admin.setAddress(userDTO.getAddress());
				}
				adminRepository.save(admin);
				return new ResponseEntity<AdminEntity>(admin, HttpStatus.OK);
			} else {
				return new ResponseEntity<RESTError>(new RESTError(2, "Admin not found"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(3, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{adminId}")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<?> deleteAdmin(@PathVariable Integer adminId) {
		try {
			if (adminRepository.exists(adminId)) {
				AdminEntity admin = adminRepository.findOne(adminId);
				admin.setDeleted(true);
				adminRepository.save(admin);
				return new ResponseEntity<AdminEntity>(admin, HttpStatus.OK);
			} else {
				return new ResponseEntity<RESTError>(new RESTError(1, "Admin not found"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(2, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(method = RequestMethod.GET, value = "/{adminId}")
	@JsonView(Views.Admin.class)
	public ResponseEntity<?> getAdminById(@PathVariable Integer adminId) {
		try {
			if (adminRepository.exists(adminId)) {
				AdminEntity admin = adminRepository.findOne(adminId);
				return new ResponseEntity<AdminEntity>(admin, HttpStatus.OK);
			} else {
				return new ResponseEntity<RESTError>(new RESTError(1, "Admin not found"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(2, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{adminId}/user")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<?> addUserToAdmin(@PathVariable Integer adminId, @RequestParam Integer userId) {
		try {
			if (adminRepository.exists(adminId)) {
				if (userRepository.exists(userId)) {
					UserEntity uEnt = userRepository.findOne(userId);
					AdminEntity admin = adminRepository.findOne(adminId);
					admin.setUser(uEnt);
					adminRepository.save(admin);
					return new ResponseEntity<AdminEntity>(admin, HttpStatus.OK);
				} else {
					return new ResponseEntity<RESTError>(new RESTError(2, "User not found"), HttpStatus.NOT_FOUND);
				}
			} else {
				return new ResponseEntity<RESTError>(new RESTError(3, "Admin not found"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(4, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
