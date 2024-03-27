package be.vinci.pae.domain.dto;

/**
 * ContactDTO interface.
 */
public interface ContactDTO {

  /**
   * Get the contact's id.
   *
   * @return the contact's id.
   */
  int getId();

  /**
   * Set the contact's id.
   *
   * @param id id to set.
   */
  void setId(int id);

  /**
   * Get the contact's company.
   *
   * @return the contact's company.
   */
  int getCompany();

  /**
   * Set the contact's company.
   *
   * @param company company to set.
   */
  void setCompany(int company);

  /**
   * Get the contact's student.
   *
   * @return the contact's student.
   */
  int getStudent();

  /**
   * Set the contact's student.
   *
   * @param student student to set.
   */
  void setStudent(int student);

  /**
   * Get the contact's meeting.
   *
   * @return the contact's meeting.
   */
  String getMeeting();

  /**
   * Set the contact's meeting.
   *
   * @param meeting meeting to set.
   */
  void setMeeting(String meeting);

  /**
   * Get the contact's state.
   *
   * @return the contact's state.
   */
  String getState();

  /**
   * Set the contact's state.
   *
   * @param state state to set.
   */
  void setState(String state);

  /**
   * Get the contact's reason for refusal.
   *
   * @return the contact's reason for refusal.
   */
  String getReasonRefusal();

  /**
   * Set the contact's reason for refusal.
   *
   * @param reasonRefusal reason for refusal to set.
   */
  void setReasonRefusal(String reasonRefusal);

  /**
   * Get the contact's school year.
   *
   * @return the contact's school year.
   */
  String getSchoolYear();

  /**
   * Set the contact's school year.
   *
   * @param schoolYear school year to set.
   */
  void setSchoolYear(String schoolYear);

  /**
   * Get the contact's name company.
   *
   * @return the contact's name company.
   */
  String getNameCompany();

  /**
   * Set the contact's name company.
   *
   * @param name name to set.
   */
  void setNameCompany(String nameCompany);

  /**
   * Get the contact's designation company.
   *
   * @return the contact's designation company.
   */
  String getDesignationCompany();

  /**
   * Set the contact's designation company.
   *
   * @param designation designation to set.
   */
  void setDesignationCompany(String designationCompany);
}
