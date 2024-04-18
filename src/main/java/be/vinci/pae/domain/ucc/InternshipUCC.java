package be.vinci.pae.domain.ucc;

import be.vinci.pae.domain.dto.InternshipDTO;

/**
 * InternshipUCC interface.
 */
public interface InternshipUCC {


  /**
   * Get an internship by a student id.
   *
   * @param student the student id.
   * @return the internship found.
   */
  InternshipDTO getOneByStudent(int student);


  /**
   * Get an internship by his id.
   *
   * @param id            the internship id.
   * @param actualStudent the student id.
   * @return the internship found.
   */
  InternshipDTO getOneById(int id, int actualStudent);

  /**
   * update the internship's subject.
   *
   * @param project      the internship subject.
   * @param version      the version of the internship
   * @param internshipId the internship id.
   * @return the internship edited.
   */
  InternshipDTO editProject(String project, int version, int internshipId);
}
