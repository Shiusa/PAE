package be.vinci.pae.services.dao;

import be.vinci.pae.domain.dto.InternshipDTO;
import java.util.Map;

/**
 * InternshipDAO interface.
 */
public interface InternshipDAO {


  /**
   * Get one internship by id then set the internshipDTO if intership exist.
   *
   * @param student student' id.
   * @return internshipDTO with setter corresponding to the id, null otherwise.
   */
  InternshipDTO getOneInternshipByIdUser(int student);


  /**
   * Get one internship by id then set the internshipDTO if intership exist.
   *
   * @param id intership' id.
   * @return internshipDTO with setter corresponding to the id, null otherwise.
   */
  InternshipDTO getOneInternshipById(int id);

  /**
   * Get internship count by year.
   *
   * @return map with number of internship and total student by year.
   */
  Map<String, Integer[]> getInternshipCountByYear();
}
