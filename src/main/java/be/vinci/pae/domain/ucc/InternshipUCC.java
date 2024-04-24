package be.vinci.pae.domain.ucc;

import be.vinci.pae.domain.dto.InternshipDTO;
import java.util.List;
import java.util.Map;

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
   * Get all internships.
   *
   * @return all the internships.
   */
  List<InternshipDTO> getAllInternships();

  /**
   * Create an internship.
   *
   * @param internshipDTO the dto containing the new internship datas.
   * @param studentId     the student id.
   * @return the created internship.
   */
  InternshipDTO createInternship(InternshipDTO internshipDTO, int studentId);

  /**
   * Get number of student with internship.
   *
   * @return map with number of internship and total student by year.
   */
  Map<String, Integer[]> getInternshipCountByYear();
}
