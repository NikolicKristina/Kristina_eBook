package com.iktpreobuka.eBook.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iktpreobuka.eBook.controllers.util.RESTError;
import com.iktpreobuka.eBook.entities.YearEntity;
import com.iktpreobuka.eBook.entities.dto.SchoolRegisterDTO;
import com.iktpreobuka.eBook.repositories.YearRepository;

@RestController
@RequestMapping(path = "/api/v1/year")
public class YearController {

	@Autowired
	private YearRepository yearRepository;

	@RequestMapping(method = RequestMethod.POST)
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<?> createYear(@RequestBody SchoolRegisterDTO schoolDTO) {
		if (yearRepository.findYearByYear(schoolDTO.getYear()) == null) {
			YearEntity yEnt = new YearEntity();
			yEnt.setYear(schoolDTO.getYear());
			yearRepository.save(yEnt);
			return new ResponseEntity<YearEntity>(yEnt, HttpStatus.OK);
		} else {
			return new ResponseEntity<RESTError>(new RESTError(1, "Year already exists"), HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<?> getAllYears() {
		return new ResponseEntity<Iterable<YearEntity>>(yearRepository.findAll(), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{yearId}")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<?> updateYear(@PathVariable Integer yearId, @RequestBody SchoolRegisterDTO schoolDTO) {
		try {
			if (yearRepository.exists(yearId)) {
				YearEntity year = yearRepository.findOne(yearId);
				if (schoolDTO.getYear() != null) {
					if (yearRepository.findYearByYear(schoolDTO.getYear()) == null) {
						year.setYear(schoolDTO.getYear());
					} else {
						return new ResponseEntity<RESTError>(new RESTError(1, "Year already exists in DataBase"),
								HttpStatus.FORBIDDEN);
					}
				}
				yearRepository.save(year);
				return new ResponseEntity<YearEntity>(year, HttpStatus.OK);
			} else {
				return new ResponseEntity<RESTError>(new RESTError(2, "Year not found"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(3, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{yearId}")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<?> deleteYear(@PathVariable Integer yearId) {
		try {
			if (yearRepository.exists(yearId)) {
				YearEntity year = yearRepository.findOne(yearId);
				year.setDeleted(true);
				yearRepository.save(year);
				return new ResponseEntity<YearEntity>(year, HttpStatus.OK);
			} else {
				return new ResponseEntity<RESTError>(new RESTError(1, "Year not found"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(2, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{yearId}/by-id")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<?> getYearById(@PathVariable Integer yearId) {
		try {
			if (yearRepository.exists(yearId)) {
				YearEntity year = yearRepository.findOne(yearId);
				return new ResponseEntity<YearEntity>(year, HttpStatus.OK);
			} else {
				return new ResponseEntity<RESTError>(new RESTError(1, "Year not found"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(2, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{yearId}/restore")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<?> restoreEntity(@PathVariable Integer yearId) {
		try {
			if (yearRepository.exists(yearId)) {
				YearEntity year = yearRepository.findOne(yearId);
				year.setDeleted(false);
				yearRepository.save(year);
				return new ResponseEntity<YearEntity>(year, HttpStatus.OK);
			} else {
				return new ResponseEntity<RESTError>(new RESTError(1, "Year not found"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(2, "Exception occured: " + e.getMessage()),
					HttpStatus.NOT_FOUND);
		}
	}

}
