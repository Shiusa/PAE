package be.vinci.pae.services.dao;

import be.vinci.pae.domain.dto.InternshipDTO;

/**
 * InternshipDAO interface.
 */
public interface InternshipDAO {


  /**
   * Get one internship by id then set the internshipDTO if intership exist.
   *
   * @param student student's id.
   * @return internshipDTO with setter corresponding to the id, null otherwise.
   */
  InternshipDTO getOneInternshipByIdUser(int student);


  /**
   * Get one internship by id then set the internshipDTO if intership exist.
   *
   * @param id internship's id.
   * @return internshipDTO with setter corresponding to the id, null otherwise.
   */
  InternshipDTO getOneInternshipById(int id);

  /**
   * update the internship's subject.
   *
   * @param subject      the internship subject.
   * @param version      the version of the internship
   * @param internshipId the internship's id.
   * @return the internship edited.
   */
  InternshipDTO editProject(String subject, int version, int internshipId);
}
