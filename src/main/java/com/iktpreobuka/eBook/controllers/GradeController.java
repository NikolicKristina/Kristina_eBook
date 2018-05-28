package com.iktpreobuka.eBook.controllers;

import java.util.ArrayList;
import java.util.Date;
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
import com.iktpreobuka.eBook.entities.GradeEntity;
import com.iktpreobuka.eBook.entities.StudentEntity;
import com.iktpreobuka.eBook.entities.SubjectEntity;
import com.iktpreobuka.eBook.entities.TeacherEntity;
import com.iktpreobuka.eBook.entities.dto.GradeDTO;
import com.iktpreobuka.eBook.repositories.GradeRepository;
import com.iktpreobuka.eBook.repositories.StudentRepository;
import com.iktpreobuka.eBook.repositories.SubjectRepository;
import com.iktpreobuka.eBook.repositories.TeacherRepository;
import com.iktpreobuka.eBook.security.Views;
import com.iktpreobuka.eBook.services.GradeDao;
import com.iktpreobuka.eBook.services.SubjectDao;

@RestController
@RequestMapping(path = "/api/v1/grade")
public class GradeController {

	@Autowired
	private GradeRepository gradeRepository;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private TeacherRepository teacherRepository;

	@Autowired
	private SubjectRepository subjectRepository;

	@Autowired
	private GradeDao gradeDao;

	@Autowired
	private SubjectDao subjectDao;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping(method = RequestMethod.POST, value = "/{teacherId}/{subjectId}/{studentId}")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<?> addGrade(@PathVariable Integer teacherId, @PathVariable Integer subjectId,
			@PathVariable Integer studentId, @RequestBody GradeDTO gEnt) {
		logger.info("ocena: " + gEnt.getValue() + " tip: " + gEnt.getType() + " semestar: " + gEnt.getSemester());
		TeacherEntity teacher = teacherRepository.findOne(teacherId);
		logger.info("1");
		SubjectEntity subject = subjectRepository.findOne(subjectId);
		logger.info("2");
		StudentEntity student = studentRepository.findOne(studentId);
		logger.info("3");
		if (teacher != null) {
			logger.info("4");
			if (subject != null) {
				logger.info("5");
				if (student != null) {
					logger.info("6");
					if (gEnt.getValue() >= 1 && gEnt.getValue() <= 5) {
						GradeEntity grade = new GradeEntity();
						logger.info("8");
						grade.setAssignDate(new Date());
						logger.info("9");
						grade.setSemester(gEnt.getSemester());
						logger.info("10");
						grade.setType(gEnt.getType());
						logger.info("11");
						grade.setValue(gEnt.getValue());
						logger.info("13");
						grade.setStudent(student);
						logger.info("14");
						grade.setSubject(subject);
						logger.info("15");
						grade.setTeacher(teacher);
						logger.info("16");
						gradeRepository.save(grade);
						logger.info("17");
						return new ResponseEntity<GradeEntity>(grade, HttpStatus.OK);
					} else {
						return new ResponseEntity<RESTError>(new RESTError(5, "Ocena mora biti od 1 do 5!"),
								HttpStatus.NOT_FOUND);
					}
				} else {
					return new ResponseEntity<RESTError>(new RESTError(2, "Student not found"), HttpStatus.NOT_FOUND);
				}
			} else {
				return new ResponseEntity<RESTError>(new RESTError(3, "Subject not found"), HttpStatus.NOT_FOUND);
			}
		} else {
			return new ResponseEntity<RESTError>(new RESTError(4, "Teacher not found"), HttpStatus.NOT_FOUND);
		}
	}

	/*@RequestMapping(method = RequestMethod.GET)
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<?> getAllGrades(Pageable pageable) {
		return new ResponseEntity<Page<GradeEntity>>(gradeRepository.findAll(pageable), HttpStatus.OK);
	}*/
	
	@RequestMapping(method = RequestMethod.GET)
	@CrossOrigin(origins = "http://localhost:4200")
	@JsonView(Views.Admin.class)
	public ResponseEntity<?> getAllGrades() {
		return new ResponseEntity<Iterable<GradeEntity>>(gradeRepository.findAll(), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{gradeId}/by-id")
	@CrossOrigin(origins = "http://localhost:4200")
	@JsonView(Views.Admin.class)
	public ResponseEntity<?> getGradeById(@PathVariable Integer gradeId) {
		try {
			if (gradeRepository.exists(gradeId)) {
				GradeEntity grade = gradeRepository.findOne(gradeId);
				return new ResponseEntity<GradeEntity>(grade, HttpStatus.OK);
			} else {
				return new ResponseEntity<RESTError>(new RESTError(1, "Grade not found"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(2, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{gradeId}")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<?> updateGrade(@PathVariable Integer gradeId, @RequestBody GradeDTO gradeDTO) {
		try {
			logger.info("update");
			if (gradeRepository.exists(gradeId)) {
				GradeEntity grade = gradeRepository.findOne(gradeId);
				if (gradeDTO.getValue() != null) {
					grade.setValue(gradeDTO.getValue());
				}
				if (gradeDTO.getType() != null) {
					grade.setType(gradeDTO.getType());
				}
				gradeRepository.save(grade);
				return new ResponseEntity<GradeEntity>(grade, HttpStatus.OK);
			} else {
				return new ResponseEntity<RESTError>(new RESTError(1, "Grade not found"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(2, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{gradeId}")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<?> deleteGrade(@PathVariable Integer gradeId) {
		try {
			if (gradeRepository.exists(gradeId)) {
				GradeEntity grade = gradeRepository.findOne(gradeId);
				grade.setDeleted(true);
				gradeRepository.save(grade);
				return new ResponseEntity<GradeEntity>(grade, HttpStatus.OK);
			} else {
				return new ResponseEntity<RESTError>(new RESTError(1, "Grade not found"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(2, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{studentId}/students")
	@CrossOrigin(origins = "http://localhost:4200")
	@JsonView(Views.Public.class)
	public ResponseEntity<?> getGradesForSubject(@PathVariable Integer studentId, @RequestParam Integer subjectId) {
		try {
			logger.info("ocene");
			if (studentRepository.exists(studentId)) {
				if (subjectRepository.exists(subjectId)) {
					if (!subjectDao.findSubjectsForStudent(studentId).isEmpty()) {
						return new ResponseEntity<List<GradeEntity>>(
								gradeDao.findGradesForStudent(studentId, subjectId), HttpStatus.OK);
					} else {
						return new ResponseEntity<RESTError>(new RESTError(2, "Ovaj učenik ne sluša ovaj predmet"),
								HttpStatus.NOT_FOUND);
					}
				} else {
					return new ResponseEntity<RESTError>(new RESTError(1, "Subject not found"), HttpStatus.NOT_FOUND);
				}
			} else {
				return new ResponseEntity<RESTError>(new RESTError(2, "Student not found"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(3, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{studentId}/students/admin")
	@CrossOrigin(origins = "http://localhost:4200")
	@JsonView(Views.Admin.class)
	public ResponseEntity<?> getGradesForSubjectAdmin(@PathVariable Integer studentId, @RequestParam Integer subjectId) {
		try {
			if (studentRepository.exists(studentId)) {
				if (subjectRepository.exists(subjectId)) {
					if (!subjectDao.findSubjectsForStudent(studentId).isEmpty()) {
						return new ResponseEntity<List<GradeEntity>>(
								gradeDao.findGradesForStudent(studentId, subjectId), HttpStatus.OK);
					} else {
						return new ResponseEntity<RESTError>(new RESTError(2, "Ovaj učenik ne sluša ovaj predmet"),
								HttpStatus.NOT_FOUND);
					}
				} else {
					return new ResponseEntity<RESTError>(new RESTError(1, "Subject not found"), HttpStatus.NOT_FOUND);
				}
			} else {
				return new ResponseEntity<RESTError>(new RESTError(2, "Student not found"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(3, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{studentId}/final_grade_sem1")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<?> suggestFinalGradeForSemester1(@PathVariable Integer studentId,
			@RequestParam Integer subjectId) {
		try {
			if (studentRepository.exists(studentId)) {
				if (subjectRepository.exists(subjectId)) {
					if (subjectDao.findSubjectsForStudent(studentId).contains(subjectRepository.findOne(subjectId))) {
						List<GradeEntity> grades = gradeDao.findGradesForSemester1(studentId, subjectId);
						Double sum = 0.;
						for (GradeEntity g : grades) {
							sum += g.getValue();
						}
						return new ResponseEntity<Double>(sum / grades.size(), HttpStatus.OK);
					} else {
						return new ResponseEntity<RESTError>(new RESTError(2, "Ovaj učenik ne sluša ovaj predmet"),
								HttpStatus.NOT_FOUND);
					}
				} else {
					return new ResponseEntity<RESTError>(new RESTError(1, "Subject not found"), HttpStatus.NOT_FOUND);
				}
			} else {
				return new ResponseEntity<RESTError>(new RESTError(2, "Student not found"), HttpStatus.NOT_FOUND);
			}
		} catch (

		Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(3, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{studentId}/final_grade")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<?> suggestFinalGrade(@PathVariable Integer studentId, @RequestParam Integer subjectId) {
		try {
			if (studentRepository.exists(studentId)) {
				if (subjectRepository.exists(subjectId)) {
					if (subjectDao.findSubjectsForStudent(studentId).contains(subjectRepository.findOne(subjectId))) {
						if (!gradeDao.findFinalGradeForSemester1(studentId, subjectId).isEmpty()) {
							List<GradeEntity> grades = new ArrayList<>();
							grades = gradeDao.findGradesForSemester2(studentId, subjectId);
							Double sum = 0.;
							for (GradeEntity g : grades) {
								sum += g.getValue();
							}
							return new ResponseEntity<Double>(sum / grades.size(), HttpStatus.OK);
						} else {
							return new ResponseEntity<RESTError>(
									new RESTError(1, "Final Grade for Semester 1 not found"), HttpStatus.NOT_FOUND);
						}
					} else {
						return new ResponseEntity<RESTError>(new RESTError(2, "Ovaj učenik ne sluša ovaj predmet"),
								HttpStatus.NOT_FOUND);
					}
				} else {
					return new ResponseEntity<RESTError>(new RESTError(3, "Subject not found"), HttpStatus.NOT_FOUND);
				}
			} else {
				return new ResponseEntity<RESTError>(new RESTError(4, "Student not found"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(5, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
