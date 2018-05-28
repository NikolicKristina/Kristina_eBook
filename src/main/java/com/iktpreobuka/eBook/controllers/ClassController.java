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
import com.iktpreobuka.eBook.entities.ClassEntity;
import com.iktpreobuka.eBook.entities.YearEntity;
import com.iktpreobuka.eBook.entities.dto.SchoolRegisterDTO;
import com.iktpreobuka.eBook.repositories.ClassRepository;
import com.iktpreobuka.eBook.repositories.TeacherRepository;
import com.iktpreobuka.eBook.repositories.YearRepository;
import com.iktpreobuka.eBook.security.Views;
import com.iktpreobuka.eBook.services.ClassDao;

@RestController
@RequestMapping(path = "/api/v1/class")
public class ClassController {

	@Autowired
	private ClassRepository classRepository;

	@Autowired
	private YearRepository yearRepository;

	@Autowired
	private TeacherRepository teacherRepository;

	@Autowired
	private ClassDao classDao;

	@RequestMapping(method = RequestMethod.POST)
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<?> createClass(@RequestBody SchoolRegisterDTO schoolDTO) {
		if (classRepository.findClassByClassSign(schoolDTO.getClassSign()) == null) {
			ClassEntity cEnt = new ClassEntity();
			cEnt.setClassSign(schoolDTO.getClassSign());
			classRepository.save(cEnt);
			return new ResponseEntity<ClassEntity>(cEnt, HttpStatus.OK);
		} else {
			return new ResponseEntity<RESTError>(new RESTError(1, "Class already exists"), HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/admin")
	@CrossOrigin(origins = "http://localhost:4200")
	@JsonView(Views.Admin.class)
	public ResponseEntity<?> getAllClassesAdmin() {
		return new ResponseEntity<Iterable<ClassEntity>>(classRepository.findAll(), HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	@CrossOrigin(origins = "http://localhost:4200")
	@JsonView(Views.Public.class)
	public ResponseEntity<?> getAllClasses() {
		return new ResponseEntity<Iterable<ClassEntity>>(classRepository.findAll(), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{classId}")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<?> updateClass(@PathVariable Integer classId, @RequestBody SchoolRegisterDTO schoolDTO) {
		try {
			if (classRepository.exists(classId)) {
				ClassEntity cEnt = classRepository.findOne(classId);
				if (schoolDTO.getClassSign() != null) {
					cEnt.setClassSign(schoolDTO.getClassSign());
				}
				classRepository.save(cEnt);
				return new ResponseEntity<ClassEntity>(cEnt, HttpStatus.OK);
			} else {
				return new ResponseEntity<RESTError>(new RESTError(1, "Class not found"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(2, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{classId}/year")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<?> addYearToClass(@PathVariable Integer classId, @RequestParam Integer yearId) {
		try {
			if (classRepository.exists(classId)) {
				if (yearRepository.exists(yearId)) {
					YearEntity year = yearRepository.findOne(yearId);
					ClassEntity cEnt = classRepository.findOne(classId);
					if (cEnt.getYear() != null && cEnt.getYear().equals(year)) {
						return new ResponseEntity<RESTError>(new RESTError(1, "Class already exists on Year"),
								HttpStatus.FORBIDDEN);
					} else {
						if (cEnt.getClassSign().contains(year.getYear().toString())) {
							cEnt.setYear(year);
							classRepository.save(cEnt);
							return new ResponseEntity<ClassEntity>(cEnt, HttpStatus.OK);
						} else {
							return new ResponseEntity<RESTError>(new RESTError(2, "Invalid Year"),
									HttpStatus.FORBIDDEN);
						}
					}
				} else {
					return new ResponseEntity<RESTError>(new RESTError(3, "Year not found"), HttpStatus.NOT_FOUND);
				}
			} else {
				return new ResponseEntity<RESTError>(new RESTError(4, "Class not found"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(5, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{classId}")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<?> deleteClass(@PathVariable Integer classId) {
		try {
			if (classRepository.exists(classId)) {
				ClassEntity cEnt = classRepository.findOne(classId);
				cEnt.setDeleted(true);
				classRepository.save(cEnt);
				return new ResponseEntity<ClassEntity>(cEnt, HttpStatus.OK);
			} else {
				return new ResponseEntity<RESTError>(new RESTError(1, "Class not found"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(2, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{classId}/by-id")
	@CrossOrigin(origins = "http://localhost:4200")
	@JsonView(Views.Admin.class)
	public ResponseEntity<?> getClassById(@PathVariable Integer classId) {
		try {
			if (classRepository.exists(classId)) {
				ClassEntity cEnt = classRepository.findOne(classId);
				return new ResponseEntity<ClassEntity>(cEnt, HttpStatus.OK);
			} else {
				return new ResponseEntity<RESTError>(new RESTError(1, "Class not found"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(2, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{teacherId}/by-teacher")
	@CrossOrigin(origins = "http://localhost:4200")
	@JsonView(Views.Public.class)
	public ResponseEntity<?> getClassesByTeacher(@PathVariable Integer teacherId) {
		try {
			if (teacherRepository.exists(teacherId)) {
				List<ClassEntity> classes = classDao.findClassesForTeacher(teacherId);
				return new ResponseEntity<List<ClassEntity>>(classes, HttpStatus.OK);
			} else {
				return new ResponseEntity<RESTError>(new RESTError(1, "Teacher not found"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(2, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{teacherId}/by-teacher/admin")
	@CrossOrigin(origins = "http://localhost:4200")
	@JsonView(Views.Admin.class)
	public ResponseEntity<?> getClassesByTeacherAdmin(@PathVariable Integer teacherId) {
		try {
			if (teacherRepository.exists(teacherId)) {
				List<ClassEntity> classes = classDao.findClassesForTeacher(teacherId);
				return new ResponseEntity<List<ClassEntity>>(classes, HttpStatus.OK);
			} else {
				return new ResponseEntity<RESTError>(new RESTError(1, "Teacher not found"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(2, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/by-sign")
	@CrossOrigin(origins = "http://localhost:4200")
	@JsonView(Views.Public.class)
	public ResponseEntity<?> getClassBySign(@RequestParam String classSign) {
		try {
			List<ClassEntity> classes = classRepository.findClassByClassSignStartingWith(classSign);
			return new ResponseEntity<List<ClassEntity>>(classes, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(2, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/by-sign/admin")
	@CrossOrigin(origins = "http://localhost:4200")
	@JsonView(Views.Admin.class)
	public ResponseEntity<?> getClassBySignAdmin(@RequestParam String classSign) {
		try {
			List<ClassEntity> classes = classRepository.findClassByClassSignStartingWith(classSign);
			return new ResponseEntity<List<ClassEntity>>(classes, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(2, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
