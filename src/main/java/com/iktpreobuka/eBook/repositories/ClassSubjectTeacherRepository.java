package com.iktpreobuka.eBook.repositories;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.eBook.entities.ClassSubjectTeacherEntity;
import com.iktpreobuka.eBook.entities.ClassSubjectTeacherEntityPK;

public interface ClassSubjectTeacherRepository extends CrudRepository<ClassSubjectTeacherEntity, ClassSubjectTeacherEntityPK> {

}
