package com.iktpreobuka.eBook.entities;

import java.io.Serializable;

public class ClassSubjectTeacherEntityPK implements Serializable {

	private static final long serialVersionUID = -5483246519902866821L;
	private Integer classId;
	private Integer subjectId;
	private Integer teacherId;

	public Integer getYearId() {
		return classId;
	}

	public Integer getSubjectId() {
		return subjectId;
	}

	public Integer getTeacherId() {
		return teacherId;
	}

	public boolean equals(Object obj) {
		if (obj instanceof ClassSubjectTeacherEntityPK) { // proverava da li je prosledjeni argument ispravnog
															// tipa(ispravan tip je tipicno klasa u kojoj se metoda
															// nalazi
			ClassSubjectTeacherEntityPK second = (ClassSubjectTeacherEntityPK) obj; // kastuje argument u ispravan tip
			return (second.getComposite().equals(getComposite()));// za svaki znacajni atribut klase,atribut argumenta
																	// treba da odgovara atributu tekuceg objekta(ovde
																	// se taj atribut sastoji iz 3 id-a
		}
		return super.equals(obj);
	}

	public int hashCode() {
		return getComposite().hashCode();// pretvara atribut u hes-kod, jednaki objekti moraju imati jednake heskodove
	}

	public String getComposite() {
		return this.classId + "_" + this.subjectId + "_" + this.teacherId;
	}

}
