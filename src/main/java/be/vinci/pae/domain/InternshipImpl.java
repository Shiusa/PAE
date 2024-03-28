package be.vinci.pae.domain;

import java.util.Date;

/**
 * Implementation of Internship.
 */
class InternshipImpl implements Internship {

  private int id;
  private int contact;
  private int supervisor;
  private Date signatureDate;
  private String project;
  private String schoolYear;

  private String nameInternship;
  private String designationInternship;

  private String addressInternship;
  private String firstnameSupervisor;
  private String lastnameSupervisor;
  private String emailSupervisor;

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  @Override
  public int getContact() {
    return contact;
  }

  @Override
  public void setContact(int contact) {
    this.contact = contact;
  }

  @Override
  public int getSupervisor() {
    return supervisor;
  }

  @Override
  public void setSupervisor(int supervisor) {
    this.supervisor = supervisor;
  }

  @Override
  public Date getSignatureDate() {
    return signatureDate;
  }

  @Override
  public void setSignatureDate(Date signatureDate) {
    this.signatureDate = signatureDate;
  }

  @Override
  public String getProject() {
    return project;
  }

  @Override
  public void setProject(String project) {
    this.project = project;
  }

  @Override
  public String getSchoolYear() {
    return schoolYear;
  }

  @Override
  public void setSchoolYear(String schoolYear) {
    this.schoolYear = schoolYear;
  }

  @Override
  public String getNameInternship() {
    return nameInternship;
  }

  @Override
  public void setNameInternship(String nameInternship) {
    this.nameInternship = nameInternship;
  }

  @Override
  public String getDesignationInternship() {
    return designationInternship;
  }

  @Override
  public void setDesignationInternship(String designationInternship) {
    this.designationInternship = designationInternship;
  }

  @Override
  public String getFirstnameSupervisor() {
    return firstnameSupervisor;
  }

  @Override
  public void setFirstnameSupervisor(String firstnameSupervisor) {
    this.firstnameSupervisor = firstnameSupervisor;
  }

  @Override
  public String getLastnameSupervisor() {
    return lastnameSupervisor;
  }

  @Override
  public void setLastnameSupervisor(String lastnameSupervisor) {
    this.lastnameSupervisor = lastnameSupervisor;
  }

  @Override
  public String getEmailSupervisor() {
    return emailSupervisor;
  }

  @Override
  public void setEmailSupervisor(String emailSupervisor) {
    this.emailSupervisor = emailSupervisor;
  }

  @Override
  public String getAddressInternship() {
    return addressInternship;
  }

  @Override
  public void setAddressInternship(String addressInternship) {
    this.addressInternship = addressInternship;
  }

}
