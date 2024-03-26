package be.vinci.pae.domain.ucc;

import be.vinci.pae.domain.dto.InternshipDTO;

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
   * @param id the internship id.
   * @return the internship found.
   */
  InternshipDTO getOneById(int id, int actualStudent);
}
