package com.iktpreobuka.eBook.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.iktpreobuka.eBook.controllers.util.RESTError;
import com.iktpreobuka.eBook.entities.RoleEntity;
import com.iktpreobuka.eBook.entities.dto.RoleDTO;
import com.iktpreobuka.eBook.repositories.RoleRepository;

@Controller
@RequestMapping("/api/v1/role")
public class RoleController {

	@Autowired
	private RoleRepository roleRepository;

	@RequestMapping(method = RequestMethod.POST)
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<?> createRole(@RequestBody RoleDTO role) {
		if (roleRepository.findByRoleName(role.getRoleName()) == null) {
			RoleEntity rEnt = new RoleEntity();
			rEnt.setRoleName(role.getRoleName());
			roleRepository.save(rEnt);
			return new ResponseEntity<RoleEntity>(rEnt, HttpStatus.OK);
		} else {
			return new ResponseEntity<RESTError>(new RESTError(1, "Role already exists"), HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<?> getAllRoles(Pageable pageable) {
		return new ResponseEntity<Page<RoleEntity>>(roleRepository.findAll(pageable), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{roleId}")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<?> updateRole(@PathVariable Integer roleId, @RequestBody RoleDTO role) {
		try {
			if (roleRepository.exists(roleId)) {
				RoleEntity rEnt = roleRepository.findOne(roleId);
				if (role.getRoleName() != null) {
					if (roleRepository.findByRoleName(role.getRoleName()) == null) {
						rEnt.setRoleName(role.getRoleName());
						;
					} else {
						return new ResponseEntity<RESTError>(new RESTError(1, "Rolename already exists"),
								HttpStatus.BAD_REQUEST);
					}
				}
				roleRepository.save(rEnt);
				return new ResponseEntity<RoleEntity>(rEnt, HttpStatus.OK);
			} else {
				return new ResponseEntity<RESTError>(new RESTError(3, "Role not found"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(4, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{roleId}")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<?> deleteRole(@PathVariable Integer roleId) {
		try {
			if (roleRepository.exists(roleId)) {
				RoleEntity role = roleRepository.findOne(roleId);
				role.setDeleted(true);
				roleRepository.save(role);
				return new ResponseEntity<RoleEntity>(role, HttpStatus.OK);
			} else {
				return new ResponseEntity<RESTError>(new RESTError(1, "Role not found"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(2, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{roleId}/by-id")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<?> getRoleById(@PathVariable Integer roleId) {
		try {
			if (roleRepository.exists(roleId)) {
				RoleEntity role = roleRepository.findOne(roleId);
				return new ResponseEntity<RoleEntity>(role, HttpStatus.OK);
			} else {
				return new ResponseEntity<RESTError>(new RESTError(1, "Role not found"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(2, "Exception occured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
