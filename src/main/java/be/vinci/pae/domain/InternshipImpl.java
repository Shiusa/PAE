package be.vinci.pae.domain;

import be.vinci.pae.domain.dto.ContactDTO;
import be.vinci.pae.domain.dto.SupervisorDTO;
import java.util.Date;

/**
 * Implementation of Internship.
 */
class InternshipImpl implements Internship {

  private int id;
  private ContactDTO contact;
  private SupervisorDTO supervisor;
  private Date signatureDate;
  private String project;
  private String schoolYear;

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  @Override
  public ContactDTO getContact() {
    return contact;
  }

  @Override
  public void setContact(ContactDTO contact) {
    this.contact = contact;
  }

  @Override
  public SupervisorDTO getSupervisor() {
    return supervisor;
  }

  @Override
  public void setSupervisor(SupervisorDTO supervisor) {
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
}
