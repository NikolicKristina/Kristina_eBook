package com.iktpreobuka.eBook.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.iktpreobuka.eBook.entities.ClassSubjectTeacherEntity;
import com.iktpreobuka.eBook.entities.SubjectEntity;
import com.iktpreobuka.eBook.entities.YearEntity;
import com.iktpreobuka.eBook.entities.dto.SchoolRegisterDTO;
import com.iktpreobuka.eBook.repositories.ClassRepository;
import com.iktpreobuka.eBook.repositories.ClassSubjectTeacherRepository;
import com.iktpreobuka.eBook.repositories.StudentRepository;
import com.iktpreobuka.eBook.repositories.SubjectRepository;
import com.iktpreobuka.eBook.repositories.TeacherRepository;
import com.iktpreobuka.eBook.repositories.YearRepository;
import com.iktpreobuka.eBook.security.Views;
import com.iktpreobuka.eBook.services.SubjectDao;

@RestController
@RequestMapping(path = "/api/v1/subject")
public class SubjectController {

	@Autowired
	private SubjectRepository subjectRepository;

	@Autowired
	private YearRepository yearRepository;

	@Autowired
	private TeacherRepository teacherRepository;

	@Autowired
	private ClassSubjectTeacherRepository classSubjectTeacherRepository;

	@Autowired
	private ClassRepository classRepository;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private SubjectDao subjectDao;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping(method = RequestMethod.POST)
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<?> createSubject(@RequestBody SchoolRegisterDTO schoolDTO) {
		logger.info("dodaj");
		if (subjectRepository.findSubjectByNameIgnoreCase(schoolDTO.getName())==null) {
			logger.info("2");
			SubjectEntity sEnt = new SubjectEntity();
			logger.info("2");
			sEnt.setName(schoolDTO.getName());
			logger.info("3");
			sEnt.setWeeklyHours(schoolDTO.getWeeklyHours());
			logger.info("4");
			subjectRepository.save(sEnt);
			logger.info("5");
			return new ResponseEntity<SubjectEntity>(sEnt, HttpStatus.OK);
		} else {
			return new ResponseEntity<RESTError>(new RESTError(1, "Subject name already exists"),
					HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/admin")
	@CrossOrigin(origins = "http://localhost:4200")
	@JsonView(Views.Admin.class)
	public ResponseEntity<?> getAllSubjectsAdmin() {
		return new ResponseEntity<Iterable<SubjectEntity>>(subjectRepository.findAll(), HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	@CrossOrigin(origins = "http://localhost:4200")
	@JsonView(Views.Public.class)
	public ResponseEntity<?> getAllSubjects() {
		return new ResponseEntity<Iterable<SubjectEntity>>(subjectRepository.findAll(), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{subjectId}/by-id")
	@CrossOrigin(origins = "http://localhost:4200")
	@JsonView(Views.Admin.class)
	public ResponseEntity<?> getSubjectById(@PathVariable Integer subjectId) {
		try {
			if (subjectRepository.exists(subjectId)) {
				SubjectEntity subject = subjectRepository.findOne(subjectId);
				return new ResponseEntity<SubjectEntity>(subject, HttpStatus.OK);
			} else {
				return new ResponseEntity<RESTError>(new RESTError(1, "Subject not found"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(2, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/by-name")
	@CrossOrigin(origins = "http://localhost:4200")
	@JsonView(Views.Public.class)
	public ResponseEntity<?> getSubjectByName(@RequestParam String name) {
		try {
			logger.info("pronadji");
			List<SubjectEntity> subjects = subjectRepository.findSubjectByNameStartingWith(name);
			return new ResponseEntity<List<SubjectEntity>>(subjects, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(2, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/by-name/admin")
	@CrossOrigin(origins = "http://localhost:4200")
	@JsonView(Views.Admin.class)
	public ResponseEntity<?> getSubjectByNameAdmin(@RequestParam String name) {
		try {
			logger.info("pronadji");
			List<SubjectEntity> subjects = subjectRepository.findSubjectByNameStartingWith(name);
			return new ResponseEntity<List<SubjectEntity>>(subjects, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(2, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{studentId}/student")
	@CrossOrigin(origins = "http://localhost:4200")
	@JsonView(Views.Public.class)
	public ResponseEntity<?> getSubjectsForStudent(@PathVariable Integer studentId) {
		try {
			if (studentRepository.exists(studentId)) {
				List<SubjectEntity> subjects = subjectDao.findSubjectsForStudent(studentId);
				if (subjects.isEmpty()) {
					return new ResponseEntity<RESTError>(new RESTError(1, "No subjects found"), HttpStatus.NOT_FOUND);
				} else {
					return new ResponseEntity<List<SubjectEntity>>(subjects, HttpStatus.OK);
				}
			} else {
				return new ResponseEntity<RESTError>(new RESTError(2, "Student not found"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(3, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

//	@RequestMapping(method = RequestMethod.GET, value = "/{teacherId}/teacher")
//	@CrossOrigin(origins = "http://localhost:4200")
//	public ResponseEntity<?> getSubjectsForTeacher(@PathVariable Integer teacherId) {
//		try {
//			if (teacherRepository.exists(teacherId)) {
//				List<SubjectEntity> subjects = subjectDao.findSubjectsForTeacherAndClass(teacherId, classId);
//				if (subjects.isEmpty()) {
//					return new ResponseEntity<RESTError>(new RESTError(1, "No subjects found"), HttpStatus.NOT_FOUND);
//				} else {
//					return new ResponseEntity<List<SubjectEntity>>(subjects, HttpStatus.OK);
//				}
//			} else {
//				return new ResponseEntity<RESTError>(new RESTError(2, "Teacher not found"), HttpStatus.NOT_FOUND);
//			}
//		} catch (Exception e) {
//			return new ResponseEntity<RESTError>(new RESTError(3, "Exception occured: " + e.getMessage()),
//					HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//
//	}

	@RequestMapping(method = RequestMethod.GET, value = "/{teacherId}/class")
	@CrossOrigin(origins = "http://localhost:4200")
	@JsonView(Views.Public.class)
	public ResponseEntity<?> getSubjectsForClassAndTeacher(@PathVariable Integer teacherId, @RequestParam Integer classId) {
		logger.info("uso");
		try {
			if (classRepository.exists(classId)) {
				List<SubjectEntity> subjects = subjectDao.findSubjectsForTeacherAndClass(teacherId, classId);
				if (subjects.isEmpty()) {
					return new ResponseEntity<RESTError>(new RESTError(1, "No subjects found"), HttpStatus.NOT_FOUND);
				} else {
					return new ResponseEntity<List<SubjectEntity>>(subjects, HttpStatus.OK);
				}
			} else {
				return new ResponseEntity<RESTError>(new RESTError(2, "Schoolclass not found"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(3, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{subjectId}")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<?> deleteSubject(@PathVariable Integer subjectId) {
		try {
			logger.info("obrisi");
			if (subjectRepository.exists(subjectId)) {
				SubjectEntity subject = subjectRepository.findOne(subjectId);
				subject.setDeleted(true);
				subjectRepository.save(subject);
				return new ResponseEntity<SubjectEntity>(subject, HttpStatus.OK);
			} else {
				return new ResponseEntity<RESTError>(new RESTError(1, "Subject not found"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(2, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{subjectId}")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<?> updateSubject(@PathVariable Integer subjectId, @RequestBody SchoolRegisterDTO schoolDTO) {
		try {
			logger.info("edit");
			if (subjectRepository.exists(subjectId)) {
				logger.info("edit1");
				SubjectEntity subject = subjectRepository.findOne(subjectId);
				logger.info("edit2");
				if (schoolDTO.getName() != null) {
					logger.info("edit3");
					subject.setName(schoolDTO.getName());
					logger.info("edit4");
				}
				if (schoolDTO.getWeeklyHours() != null) {
					logger.info("edit5");
					subject.setWeeklyHours(schoolDTO.getWeeklyHours());
					logger.info("edit6");
				}
				subjectRepository.save(subject);
				logger.info("edit7");
				return new ResponseEntity<SubjectEntity>(subject, HttpStatus.OK);
			} else {
				return new ResponseEntity<RESTError>(new RESTError(1, "Subject not found"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(2, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{subjectId}/teacher/class")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<?> addTeacherAndClassToSubject(@PathVariable Integer subjectId,
			@RequestParam Integer teacherId, @RequestParam Integer classId) {
		try {
			logger.info("d1");
			if (subjectRepository.exists(subjectId)) {
				if (teacherRepository.exists(teacherId)) {
					if (classRepository.exists(classId)) {
						ClassSubjectTeacherEntity cst = new ClassSubjectTeacherEntity(classId, subjectId, teacherId);
						classSubjectTeacherRepository.save(cst);
						SubjectEntity subject = subjectRepository.findOne(subjectId);
						return new ResponseEntity<SubjectEntity>(subject, HttpStatus.OK);
					} else {
						return new ResponseEntity<RESTError>(new RESTError(1, "Class not found"), HttpStatus.NOT_FOUND);
					}
				} else {
					return new ResponseEntity<RESTError>(new RESTError(2, "Teacher not found"), HttpStatus.NOT_FOUND);
				}
			} else {
				return new ResponseEntity<RESTError>(new RESTError(3, "Subject not found"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(4, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{subjectId}/year")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<?> addYearToSubject(@PathVariable Integer subjectId, @RequestParam Integer yearId) {
		try {
			if (subjectRepository.exists(subjectId)) {
				if (yearRepository.exists(yearId)) {
					YearEntity year = yearRepository.findOne(yearId);
					SubjectEntity subject = subjectRepository.findOne(subjectId);
					if (subject.getYear() != null && subject.getYear().equals(year)) {
						return new ResponseEntity<RESTError>(new RESTError(1, "Subject already exists on Year"),
								HttpStatus.FORBIDDEN);
					} else {
						if (subject.getName().contains(year.getYear().toString())) {
							subject.setYear(year);
							subjectRepository.save(subject);
							return new ResponseEntity<SubjectEntity>(subject, HttpStatus.OK);
						} else {
							return new ResponseEntity<RESTError>(new RESTError(2, "Invalid Year"),
									HttpStatus.FORBIDDEN);
						}
					}
				} else {
					return new ResponseEntity<RESTError>(new RESTError(3, "Year not found"), HttpStatus.NOT_FOUND);
				}
			} else {
				return new ResponseEntity<RESTError>(new RESTError(4, "Subject not found"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(5, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}

