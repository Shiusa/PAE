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

  String getFirstnameSupervisor();

  void setFirstnameSupervisor(String firstnameSupervisor);

  String getLastnameSupervisor();

  void setLastnameSupervisor(String lastnameSupervisor);

  String getEmailSupervisor();

  void setEmailSupervisor(String emailSupervisor);

  String getNameInternship();

  void setNameInternship(String nameInternship);

  String getDesignationInternship();

  void setDesignationInternship(String designationInternship);

  String getAddressInternship();

  void setAddressInternship(String addressInternship);
}
