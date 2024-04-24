package be.vinci.pae.domain.dto;

import java.util.Date;

/**
 * InternshipDTO interface.
 */

public interface InternshipDTO {

  /**
   * Get the internship's id.
   *
   * @return the internship's.
   */
  int getId();

  /**
   * Set the internship's id.
   *
   * @param id id to set.
   */
  void setId(int id);

  /**
   * Get the internship's contact.
   *
   * @return the internship's contact.
   */
  ContactDTO getContact();

  /**
   * Set the internship's contact.
   *
   * @param contact contact to set.
   */
  void setContact(ContactDTO contact);

  /**
   * Get the internship's supervisor.
   *
   * @return the internship's supervisor.
   */
  SupervisorDTO getSupervisor();

  /**
   * Set the internship's supervisor.
   *
   * @param supervisor supervisor to set.
   */
  void setSupervisor(SupervisorDTO supervisor);

  /**
   * Get the internship's signature date.
   *
   * @return the internship's signature date.
   */
  Date getSignatureDate();

  /**
   * Set the internship's signature date.
   *
   * @param signatureDate signature date to set.
   */
  void setSignatureDate(Date signatureDate);

  /**
   * Get the internship's project.
   *
   * @return the internship's project.
   */
  String getProject();

  /**
   * Set the internship's project.
   *
   * @param project project to set.
   */
  void setProject(String project);

  /**
   * Get the internship's school year.
   *
   * @return the internship's school year.
   */
  String getSchoolYear();

  /**
   * Set the internship's school year.
   *
   * @param schoolYear school year to set.
   */
  void setSchoolYear(String schoolYear);

  /**
   * Get the internship's version.
   *
   * @return the internship's version.
   */
  int getVersion();

  /**
   * Set the internship's version.
   *
   * @param version version to set.
   */
  void setVersion(int version);

}