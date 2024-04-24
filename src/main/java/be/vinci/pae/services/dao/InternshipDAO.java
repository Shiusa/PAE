package be.vinci.pae.services.dao;

import be.vinci.pae.domain.dto.InternshipDTO;
import java.util.List;
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
   * Get all internships.
   *
   * @return all internships.
   */
  List<InternshipDTO> getAllInternships();

  /**
   * Get one internship by a contact id.
   *
   * @param id the contact id.
   * @return the internship found.
   */
  InternshipDTO getOneByContact(int id);

  /**
   * Create one internship.
   *
   * @param internshipDTO the dto containing the internship datas.
   * @return the created internship.
   */
  InternshipDTO createInternship(InternshipDTO internshipDTO);

  /**
   * Get internship count by year.
   *
   * @return map with number of internship and total student by year.
   */
  Map<String, Integer[]> getInternshipCountByYear();
}
