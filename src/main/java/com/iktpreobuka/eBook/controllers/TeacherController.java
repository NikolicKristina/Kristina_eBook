package com.iktpreobuka.eBook.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
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
import com.iktpreobuka.eBook.entities.TeacherEntity;
import com.iktpreobuka.eBook.entities.UserEntity;
import com.iktpreobuka.eBook.entities.dto.UserRegisterDTO;
import com.iktpreobuka.eBook.enums.UserTitle;
import com.iktpreobuka.eBook.repositories.TeacherRepository;
import com.iktpreobuka.eBook.repositories.UserRepository;
import com.iktpreobuka.eBook.repositories.YearRepository;
import com.iktpreobuka.eBook.security.Views;

@RestController
@RequestMapping(path = "/api/v1/teacher")
public class TeacherController {

	@Autowired
	private TeacherRepository teacherRepository;

	@Autowired
	YearRepository yearRepository;

	@Autowired
	private UserRepository userRepository;

	@RequestMapping(method = RequestMethod.POST)
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<?> createTeacher(@RequestBody UserRegisterDTO userDTO) {
		if (userDTO.getTitle().equals(UserTitle.NASTAVNIK)) {
			if (teacherRepository.findTeacherByJmbg(userDTO.getJmbg()) == null) {
				TeacherEntity tEnt = new TeacherEntity();
				tEnt.setFirstName(userDTO.getFirstName());
				tEnt.setLastName(userDTO.getLastName());
				tEnt.setEmail(userDTO.getEmail());
				tEnt.setJmbg(userDTO.getJmbg());
				tEnt.setTitle(userDTO.getTitle());
				tEnt.setAddress(userDTO.getAddress());
				tEnt.setDateOfBirth(userDTO.getDateOfBirth());
				teacherRepository.save(tEnt);
				return new ResponseEntity<TeacherEntity>(tEnt, HttpStatus.OK);
			} else {
				return new ResponseEntity<RESTError>(new RESTError(1, "Teacher already exists"),
						HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<RESTError>(new RESTError(2, "Invalid title"), HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	@CrossOrigin(origins = "http://localhost:4200")
	@JsonView(Views.Public.class)
	public ResponseEntity<?> getAllTeachers() {
		return new ResponseEntity<Iterable<TeacherEntity>>(teacherRepository.findAll(), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/admin")
	@CrossOrigin(origins = "http://localhost:4200")
	@JsonView(Views.Admin.class)
	public ResponseEntity<?> getAllTeachersAdmin() {
		return new ResponseEntity<Iterable<TeacherEntity>>(teacherRepository.findAll(), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{teacherId}")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<?> updateTeacher(@PathVariable Integer teacherId, @RequestBody UserRegisterDTO userDTO) {
		try {
			if (teacherRepository.exists(teacherId)) {
				TeacherEntity teacher = teacherRepository.findOne(teacherId);
				if (userDTO.getFirstName() != null) {
					teacher.setFirstName(userDTO.getFirstName());
				}
				if (userDTO.getLastName() != null) {
					teacher.setLastName(userDTO.getLastName());
				}
				if (userDTO.getEmail() != null) {
					teacher.setEmail(userDTO.getEmail());
				}
				if (userDTO.getDateOfBirth() != null) {
					teacher.setDateOfBirth(userDTO.getDateOfBirth());
				}
				if (userDTO.getTitle() != null) {
					if (userDTO.getTitle().equals(UserTitle.NASTAVNIK)) {
						teacher.setTitle(userDTO.getTitle());
					} else {
						return new ResponseEntity<RESTError>(new RESTError(1, "Invalid title"), HttpStatus.BAD_REQUEST);
					}
				}
				if (userDTO.getJmbg() != null) {
					teacher.setJmbg(userDTO.getJmbg());
				}
				if (userDTO.getAddress() != null) {
					teacher.setAddress(userDTO.getAddress());
				}
				teacherRepository.save(teacher);
				return new ResponseEntity<TeacherEntity>(teacher, HttpStatus.OK);
			} else {
				return new ResponseEntity<RESTError>(new RESTError(2, "Teacher not found"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(3, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{teacherId}")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<?> deleteTeacher(@PathVariable Integer teacherId) {
		try {
			if (teacherRepository.exists(teacherId)) {
				TeacherEntity teacher = teacherRepository.findOne(teacherId);
				teacher.setDeleted(true);
				teacherRepository.save(teacher);
				return new ResponseEntity<TeacherEntity>(teacher, HttpStatus.OK);
			} else {
				return new ResponseEntity<RESTError>(new RESTError(1, "Teacher not found"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(2, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{teacherId}/restore")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<?> restoreEntity(@PathVariable Integer teacherId) {
		try {
			if (teacherRepository.exists(teacherId)) {
				TeacherEntity teacher = teacherRepository.findOne(teacherId);
				teacher.setDeleted(false);
				teacherRepository.save(teacher);
				return new ResponseEntity<TeacherEntity>(teacher, HttpStatus.OK);
			} else {
				return new ResponseEntity<RESTError>(new RESTError(1, "Teacher not found"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(2, "Exception occured: " + e.getMessage()),
					HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{teacherId}/by-id")
	@CrossOrigin(origins = "http://localhost:4200")
	@JsonView(Views.Admin.class)
	public ResponseEntity<?> getTeacherById(@PathVariable Integer teacherId) {
		try {
			if (teacherRepository.exists(teacherId)) {
				TeacherEntity teacher = teacherRepository.findOne(teacherId);
				return new ResponseEntity<TeacherEntity>(teacher, HttpStatus.OK);
			} else {
				return new ResponseEntity<RESTError>(new RESTError(1, "Teacher not found"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(2, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/by-jmbg")
	@CrossOrigin(origins = "http://localhost:4200")
	@JsonView(Views.Public.class)
	public ResponseEntity<?> getTeacherByJmbg(@RequestParam String jmbg) {
		try {
			List<TeacherEntity> teachers = teacherRepository.findTeacherByJmbgStartingWith(jmbg);
			return new ResponseEntity<List<TeacherEntity>>(teachers, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(2, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{teacherId}/user")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<?> addUserToTeacher(@PathVariable Integer teacherId, @RequestParam Integer userId) {
		try {
			if (teacherRepository.exists(teacherId)) {
				if (userRepository.exists(userId)) {
					UserEntity uEnt = userRepository.findOne(userId);
					TeacherEntity teacher = teacherRepository.findOne(teacherId);
					teacher.setUser(uEnt);
					teacherRepository.save(teacher);
					return new ResponseEntity<TeacherEntity>(teacher, HttpStatus.OK);
				} else {
					return new ResponseEntity<RESTError>(new RESTError(2, "User not found"), HttpStatus.NOT_FOUND);
				}
			} else {
				return new ResponseEntity<RESTError>(new RESTError(3, "Student not found"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(4, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
