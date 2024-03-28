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
  int getContact();

  /**
   * Set the internship's contact.
   *
   * @param contact contact to set.
   */
  void setContact(int contact);

  /**
   * Get the internship's supervisor.
   *
   * @return the internship's supervisor.
   */
  int getSupervisor();

  /**
   * Set the internship's supervisor.
   *
   * @param supervisor supervisor to set.
   */
  void setSupervisor(int supervisor);

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
   * Get the supervisor's firstname.
   *
   * @return the supervisor's firstname.
   */
  String getFirstnameSupervisor();

  /**
   * Set the supervisor's firstname.
   *
   * @param firstnameSupervisor the supervisor's firstname.
   */
  void setFirstnameSupervisor(String firstnameSupervisor);

  /**
   * Get the supervisor's lastname.
   *
   * @return the supervisor's lastname.
   */
  String getLastnameSupervisor();

  /**
   * Set the supervisor's lastname.
   *
   * @param lastnameSupervisor lastname to set.
   */
  void setLastnameSupervisor(String lastnameSupervisor);

  /**
   * Get the supervisor's email.
   *
   * @return the supervisor's email.
   */
  String getEmailSupervisor();

  /**
   * Set the supervisor's email.
   *
   * @param emailSupervisor email to set.
   */
  void setEmailSupervisor(String emailSupervisor);

  /**
   * Get the internship's name.
   *
   * @return the internship's name.
   */
  String getNameInternship();

  /**
   * Set the internship's name.
   *
   * @param nameInternship name to set.
   */
  void setNameInternship(String nameInternship);

  /**
   * Get the internship's designation.
   *
   * @return the designation.
   */
  String getDesignationInternship();

  /**
   * Set the designation internship.
   *
   * @param designationInternship the designation of the company.
   */
  void setDesignationInternship(String designationInternship);

  /**
   * Get the address.
   *
   * @return the address.
   */
  String getAddressInternship();

  /**
   * Set the internship's address.
   *
   * @param addressInternship the address.
   */
  void setAddressInternship(String addressInternship);
}
