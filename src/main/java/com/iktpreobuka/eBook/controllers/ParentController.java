package com.iktpreobuka.eBook.controllers;

import java.util.List;

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
import com.iktpreobuka.eBook.entities.ParentEntity;
import com.iktpreobuka.eBook.entities.StudentEntity;
import com.iktpreobuka.eBook.entities.UserEntity;
import com.iktpreobuka.eBook.entities.dto.UserRegisterDTO;
import com.iktpreobuka.eBook.enums.UserTitle;
import com.iktpreobuka.eBook.repositories.ClassRepository;
import com.iktpreobuka.eBook.repositories.ParentRepository;
import com.iktpreobuka.eBook.repositories.StudentRepository;
import com.iktpreobuka.eBook.repositories.UserRepository;
import com.iktpreobuka.eBook.security.Views;
import com.iktpreobuka.eBook.services.ParentDao;

@RestController
@RequestMapping(path = "/api/v1/parent")
public class ParentController {

	@Autowired
	private ParentRepository parentRepository;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ClassRepository classRepository;

	@Autowired
	private ParentDao parentDao;

	@RequestMapping(method = RequestMethod.POST)
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<?> createParent(@RequestBody UserRegisterDTO userDTO) {
		if (userDTO.getTitle().equals(UserTitle.RODITELJ)) {
			if (parentRepository.findParentByJmbg(userDTO.getJmbg()) == null) {
				ParentEntity pEnt = new ParentEntity();
				pEnt.setFirstName(userDTO.getFirstName());
				pEnt.setLastName(userDTO.getLastName());
				pEnt.setEmail(userDTO.getEmail());
				pEnt.setJmbg(userDTO.getJmbg());
				pEnt.setTitle(userDTO.getTitle());
				pEnt.setAddress(userDTO.getAddress());
				pEnt.setDateOfBirth(userDTO.getDateOfBirth());
				parentRepository.save(pEnt);
				return new ResponseEntity<ParentEntity>(pEnt, HttpStatus.OK);
			} else {
				return new ResponseEntity<RESTError>(new RESTError(1, "Parent already exists"), HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<RESTError>(new RESTError(2, "Invalid title"), HttpStatus.BAD_REQUEST);
		}
	}

	/*
	 * @RequestMapping(method = RequestMethod.GET)
	 * 
	 * @CrossOrigin(origins = "http://localhost:4200") public ResponseEntity<?>
	 * getAllParents(Pageable pageable) { return new
	 * ResponseEntity<Page<ParentEntity>>(parentRepository.findAll(pageable),
	 * HttpStatus.OK); }
	 */

	@RequestMapping(method = RequestMethod.GET, value = "/admin")
	@CrossOrigin(origins = "http://localhost:4200")
	@JsonView(Views.Admin.class)
	public ResponseEntity<?> getAllParentsAdmin() {
		return new ResponseEntity<Iterable<ParentEntity>>(parentRepository.findAll(), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET)
	@CrossOrigin(origins = "http://localhost:4200")
	@JsonView(Views.Public.class)
	public ResponseEntity<?> getAllParents() {
		return new ResponseEntity<Iterable<ParentEntity>>(parentRepository.findAll(), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{parentId}")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<?> updateParent(@PathVariable Integer parentId, @RequestBody UserRegisterDTO userDTO) {
		try {
			if (parentRepository.exists(parentId)) {
				ParentEntity parent = parentRepository.findOne(parentId);
				if (userDTO.getFirstName() != null) {
					parent.setFirstName(userDTO.getFirstName());
				}
				if (userDTO.getLastName() != null) {
					parent.setLastName(userDTO.getLastName());
				}
				if (userDTO.getEmail() != null) {
					parent.setEmail(userDTO.getEmail());
				}
				if (userDTO.getDateOfBirth() != null) {
					parent.setDateOfBirth(userDTO.getDateOfBirth());
				}
				if (userDTO.getTitle() != null) {
					if (userDTO.getTitle().equals(UserTitle.RODITELJ)) {
						parent.setTitle(userDTO.getTitle());
					} else {
						return new ResponseEntity<RESTError>(new RESTError(1, "Invalid title"), HttpStatus.BAD_REQUEST);
					}
				}
				if (userDTO.getJmbg() != null) {
					parent.setJmbg(userDTO.getJmbg());
				}
				if (userDTO.getAddress() != null) {
					parent.setAddress(userDTO.getAddress());
				}
				parentRepository.save(parent);
				return new ResponseEntity<ParentEntity>(parent, HttpStatus.OK);
			} else {
				return new ResponseEntity<RESTError>(new RESTError(2, "Parent not found"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(3, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{parentId}")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<?> deleteParent(@PathVariable Integer parentId) {
		try {
			if (parentRepository.exists(parentId)) {
				ParentEntity parent = parentRepository.findOne(parentId);
				parent.setDeleted(true);
				parentRepository.save(parent);
				return new ResponseEntity<ParentEntity>(parent, HttpStatus.OK);
			} else {
				return new ResponseEntity<RESTError>(new RESTError(1, "Parent not found"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(2, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{parentId}/by-id")
	@CrossOrigin(origins = "http://localhost:4200")
	@JsonView(Views.Admin.class)
	public ResponseEntity<?> getParentById(@PathVariable Integer parentId) {
		try {
			if (parentRepository.exists(parentId)) {
				ParentEntity parent = parentRepository.findOne(parentId);
				return new ResponseEntity<ParentEntity>(parent, HttpStatus.OK);
			} else {
				return new ResponseEntity<RESTError>(new RESTError(1, "Parent not found"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(2, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/by-jmbg")
	@CrossOrigin(origins = "http://localhost:4200")
	@JsonView(Views.Public.class)
	public ResponseEntity<?> getParentByJmbg(@RequestParam String jmbg) {
		try {
			List<ParentEntity> parents = parentRepository.findParentByJmbgStartingWith(jmbg);
			return new ResponseEntity<List<ParentEntity>>(parents, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(2, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/by-jmbg/admin")
	@CrossOrigin(origins = "http://localhost:4200")
	@JsonView(Views.Admin.class)
	public ResponseEntity<?> getParentByJmbgAdmin(@RequestParam String jmbg) {
		try {
			List<ParentEntity> parents = parentRepository.findParentByJmbgStartingWith(jmbg);
			return new ResponseEntity<List<ParentEntity>>(parents, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(2, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/by-class")
	@CrossOrigin(origins = "http://localhost:4200")
	@JsonView(Views.Public.class)
	public ResponseEntity<?> getParentsForClass(@RequestParam String classSign) {
		try {
			if (classRepository.findClassByClassSign(classSign) != null) {
				if (parentDao.findParentsForClass(classSign) != null) {
					List<ParentEntity> parents = parentDao.findParentsForClass(classSign);
					return new ResponseEntity<List<ParentEntity>>(parents, HttpStatus.OK);
				} else {
					return new ResponseEntity<RESTError>(new RESTError(1, "Parents not found"), HttpStatus.NOT_FOUND);
				}
			} else {
				return new ResponseEntity<RESTError>(new RESTError(2, "Class not found"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(3, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/by-class/admin")
	@CrossOrigin(origins = "http://localhost:4200")
	@JsonView(Views.Admin.class)
	public ResponseEntity<?> getParentsForClassAdmin(@RequestParam String classSign) {
		try {
			if (classRepository.findClassByClassSign(classSign) != null) {
				if (parentDao.findParentsForClass(classSign) != null) {
					List<ParentEntity> parents = parentDao.findParentsForClass(classSign);
					return new ResponseEntity<List<ParentEntity>>(parents, HttpStatus.OK);
				} else {
					return new ResponseEntity<RESTError>(new RESTError(1, "Parents not found"), HttpStatus.NOT_FOUND);
				}
			} else {
				return new ResponseEntity<RESTError>(new RESTError(2, "Class not found"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(3, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{parentId}/student")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<?> addStudentToParent(@PathVariable Integer parentId, @RequestParam Integer studentId) {
		try {
			if (parentRepository.exists(parentId)) {
				if (studentRepository.exists(studentId)) {
					ParentEntity parent = parentRepository.findOne(parentId);
					StudentEntity student = studentRepository.findOne(studentId);
					List<StudentEntity> students = parent.getStudents();
					if (students.contains(student)) {
						return new ResponseEntity<RESTError>(new RESTError(1, "Student already exists"),
								HttpStatus.BAD_REQUEST);
					} else {
						students.add(student);
						parent.setStudents(students);
						parentRepository.save(parent);
						return new ResponseEntity<ParentEntity>(parent, HttpStatus.OK);
					}
				} else {
					return new ResponseEntity<RESTError>(new RESTError(1, "Student not found"), HttpStatus.NOT_FOUND);
				}
			} else {
				return new ResponseEntity<RESTError>(new RESTError(2, "Parent not found"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(3, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{parentId}/user")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<?> addUserToParent(@PathVariable Integer parentId, @RequestParam Integer userId) {
		try {
			if (parentRepository.exists(parentId)) {
				if (userRepository.exists(userId)) {
					UserEntity uEnt = userRepository.findOne(userId);
					ParentEntity parent = parentRepository.findOne(parentId);
					parent.setUser(uEnt);
					parentRepository.save(parent);
					return new ResponseEntity<ParentEntity>(parent, HttpStatus.OK);
				} else {
					return new ResponseEntity<RESTError>(new RESTError(2, "User not found"), HttpStatus.NOT_FOUND);
				}
			} else {
				return new ResponseEntity<RESTError>(new RESTError(3, "Parent not found"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(4, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
