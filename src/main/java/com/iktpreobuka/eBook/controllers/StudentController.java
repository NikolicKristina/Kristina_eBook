package com.iktpreobuka.eBook.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.iktpreobuka.eBook.entities.ClassEntity;
import com.iktpreobuka.eBook.entities.StudentEntity;
import com.iktpreobuka.eBook.entities.UserEntity;
import com.iktpreobuka.eBook.entities.dto.UserRegisterDTO;
import com.iktpreobuka.eBook.enums.UserTitle;
import com.iktpreobuka.eBook.repositories.ClassRepository;
import com.iktpreobuka.eBook.repositories.ParentRepository;
import com.iktpreobuka.eBook.repositories.StudentRepository;
import com.iktpreobuka.eBook.repositories.SubjectRepository;
import com.iktpreobuka.eBook.repositories.UserRepository;
import com.iktpreobuka.eBook.security.Views;
import com.iktpreobuka.eBook.services.StudentDao;

@RestController
@RequestMapping(path = "/api/v1/student")
public class StudentController {

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private ClassRepository classRepository;

	@Autowired
	private ParentRepository parentRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SubjectRepository subjectRepository;

	@Autowired
	private StudentDao studentDao;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping(method = RequestMethod.POST)
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<?> createStudent(@RequestBody UserRegisterDTO userDTO) {
		// User user =
		// (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		logger.info("1");
		if (userDTO.getTitle().equals(UserTitle.UČENIK)) {
			if (studentRepository.findStudentByJmbg(userDTO.getJmbg()) == null) {
				StudentEntity sEnt = new StudentEntity();
				sEnt.setFirstName(userDTO.getFirstName());
				sEnt.setLastName(userDTO.getLastName());
				sEnt.setEmail(userDTO.getEmail());
				sEnt.setJmbg(userDTO.getJmbg());
				sEnt.setTitle(userDTO.getTitle());
				sEnt.setAddress(userDTO.getAddress());
				sEnt.setDateOfBirth(userDTO.getDateOfBirth());
				studentRepository.save(sEnt);
				return new ResponseEntity<StudentEntity>(sEnt, HttpStatus.OK);
			} else {
				return new ResponseEntity<RESTError>(new RESTError(1, "Student already exists"),
						HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<RESTError>(new RESTError(2, "Invalid title"), HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/admin")
	@CrossOrigin(origins = "http://localhost:4200")
	@JsonView(Views.Admin.class)
	public ResponseEntity<?> getStudentsAdmin() {
		return new ResponseEntity<Iterable<StudentEntity>>(studentRepository.findAll(), HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	@CrossOrigin(origins = "http://localhost:4200")
	@JsonView(Views.Public.class)
	public ResponseEntity<?> getStudents() {
		return new ResponseEntity<Iterable<StudentEntity>>(studentRepository.findAll(), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{studentId}")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<?> updateStudent(@PathVariable Integer studentId, @RequestBody UserRegisterDTO userDTO) {
		try {
			if (studentRepository.exists(studentId)) {
				StudentEntity student = studentRepository.findOne(studentId);
				if (userDTO.getFirstName() != null) {
					student.setFirstName(userDTO.getFirstName());
				}
				if (userDTO.getLastName() != null) {
					student.setLastName(userDTO.getLastName());
				}
				if (userDTO.getEmail() != null) {
					student.setEmail(userDTO.getEmail());
				}
				if (userDTO.getDateOfBirth() != null) {
					student.setDateOfBirth(userDTO.getDateOfBirth());
				}
				if (userDTO.getTitle() != null) {
					if (userDTO.getTitle().equals(UserTitle.UČENIK)) {
						student.setTitle(userDTO.getTitle());
					} else {
						return new ResponseEntity<RESTError>(new RESTError(1, "Invalid title"), HttpStatus.BAD_REQUEST);
					}
				}
				if (userDTO.getJmbg() != null) {
					student.setJmbg(userDTO.getJmbg());
				}
				if (userDTO.getAddress() != null) {
					student.setAddress(userDTO.getAddress());
				}
				studentRepository.save(student);
				return new ResponseEntity<StudentEntity>(student, HttpStatus.OK);
			} else {
				return new ResponseEntity<RESTError>(new RESTError(2, "Student not found"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(3, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{studentId}")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<?> deleteStudent(@PathVariable Integer studentId) {
		try {
			logger.info("1");
			if (studentRepository.exists(studentId)) {
				StudentEntity student = studentRepository.findOne(studentId);
				student.setDeleted(true);
				studentRepository.save(student);
				return new ResponseEntity<StudentEntity>(student, HttpStatus.OK);
			} else {
				return new ResponseEntity<RESTError>(new RESTError(1, "Student not found"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(2, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{studentId}/by-id")
	@CrossOrigin(origins = "http://localhost:4200")
	@JsonView(Views.Admin.class)
	public ResponseEntity<?> getStudentById(@PathVariable Integer studentId) {
		try {
			if (studentRepository.exists(studentId)) {
				StudentEntity student = studentRepository.findOne(studentId);
				return new ResponseEntity<StudentEntity>(student, HttpStatus.OK);
			} else {
				return new ResponseEntity<RESTError>(new RESTError(1, "Student not found"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(2, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/by-jmbg")
	@CrossOrigin(origins = "http://localhost:4200")
	@JsonView(Views.Public.class)
	public ResponseEntity<?> getStudentByJmbg(@RequestParam String jmbg) {
		try {
			List<StudentEntity> students = studentRepository.findStudentByJmbgStartingWith(jmbg);
			return new ResponseEntity<List<StudentEntity>>(students, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(2, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/by-jmbg/admin")
	@CrossOrigin(origins = "http://localhost:4200")
	@JsonView(Views.Admin.class)
	public ResponseEntity<?> getStudentByJmbgAdmin(@RequestParam String jmbg) {
		try {
			List<StudentEntity> students = studentRepository.findStudentByJmbgStartingWith(jmbg);
			return new ResponseEntity<List<StudentEntity>>(students, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(2, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{studentId}/school_class")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<?> addClassToStudent(@PathVariable Integer studentId, @RequestParam Integer classId) {
		try {
			logger.info("dodela1");
			if (studentRepository.exists(studentId)) {
				logger.info("dodela2");
				if (classRepository.exists(classId)) {
					logger.info("dodela3");
					ClassEntity cEnt = classRepository.findOne(classId);
					logger.info("dodela4");
					StudentEntity student = studentRepository.findOne(studentId);
					logger.info("dodela5");
					if (student.getSchoolClass()==cEnt) {
						logger.info("dodela6");
						return new ResponseEntity<RESTError>(new RESTError(6, "Odeljenje je već dodeljeno tom učeniku"),
								HttpStatus.BAD_REQUEST);
					} else {
						student.setSchoolClass(cEnt);
						logger.info("dodela7");
						studentRepository.save(student);
						logger.info("dodela8");
						return new ResponseEntity<StudentEntity>(student, HttpStatus.OK);
					}
				} else {
					return new ResponseEntity<RESTError>(new RESTError(2, "Class not found"), HttpStatus.NOT_FOUND);
				}
			} else {
				return new ResponseEntity<RESTError>(new RESTError(3, "Student not found"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(4, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/by-class")
	@CrossOrigin(origins = "http://localhost:4200")
	@JsonView(Views.Public.class)
	public ResponseEntity<?> getStudentsByClass(@RequestParam String classSign) {
		try {
			if (classRepository.findClassByClassSign(classSign) != null) {
				List<StudentEntity> students = studentDao.findStudentsByClass(classSign);
				if (students.isEmpty()) {
					return new ResponseEntity<RESTError>(new RESTError(1, "No students found"), HttpStatus.NOT_FOUND);
				} else {
					return new ResponseEntity<List<StudentEntity>>(students, HttpStatus.OK);
				}
			} else {
				return new ResponseEntity<RESTError>(new RESTError(2, "Class not found"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(3, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "{subjectId}/by-subject")
	@CrossOrigin(origins = "http://localhost:4200")
	@JsonView(Views.Public.class)
	public ResponseEntity<?> getStudentsBySubject(@PathVariable Integer subjectId) {
		logger.info("uso");
		try {
			if (subjectRepository.exists(subjectId)) {
				List<StudentEntity> students = studentDao.findStudentsBySubject(subjectId);
				if (students.isEmpty()) {
					return new ResponseEntity<RESTError>(new RESTError(1, "No students found"), HttpStatus.NOT_FOUND);
				} else {
					return new ResponseEntity<List<StudentEntity>>(students, HttpStatus.OK);
				}
			} else {
				return new ResponseEntity<RESTError>(new RESTError(2, "Subject not found"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(3, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{teacherId}/subject")
	@CrossOrigin(origins = "http://localhost:4200")
	@JsonView(Views.Public.class)
	public ResponseEntity<?> getStudentsForSubjectAndTeacher(@PathVariable Integer teacherId,
			@RequestParam Integer subjectId, @RequestParam Integer classId) {
		logger.info("uso");
		try {
			if (subjectRepository.exists(subjectId)) {
				List<StudentEntity> students = studentDao.findStudentsBySubjectAndTeacher(subjectId, teacherId, classId);
				if (students.isEmpty()) {
					return new ResponseEntity<RESTError>(new RESTError(1, "No students found"), HttpStatus.NOT_FOUND);
				} else {
					return new ResponseEntity<List<StudentEntity>>(students, HttpStatus.OK);
				}
			} else {
				return new ResponseEntity<RESTError>(new RESTError(2, "Subject not found"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(3, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{parentId}/students")
	@CrossOrigin(origins = "http://localhost:4200")
	@JsonView(Views.Public.class)
	public ResponseEntity<?> getStudentsByParent(@PathVariable Integer parentId) {
		try {
			if (parentRepository.exists(parentId)) {
				List<StudentEntity> students = studentDao.findStudentsByParentId(parentId);
				if (students.isEmpty()) {
					return new ResponseEntity<RESTError>(new RESTError(1, "No students found"), HttpStatus.NOT_FOUND);
				} else {
					return new ResponseEntity<List<StudentEntity>>(students, HttpStatus.OK);
				}
			} else {
				return new ResponseEntity<RESTError>(new RESTError(2, "Parent not found"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(3, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{studentId}/user")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<?> addUserToStudent(@PathVariable Integer studentId, @RequestParam Integer userId) {
		try {
			if (studentRepository.exists(studentId)) {
				if (userRepository.exists(userId)) {
					UserEntity uEnt = userRepository.findOne(userId);
					StudentEntity student = studentRepository.findOne(studentId);
					student.setUser(uEnt);
					studentRepository.save(student);
					return new ResponseEntity<StudentEntity>(student, HttpStatus.OK);
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
