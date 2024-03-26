package be.vinci.pae.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import org.mindrot.jbcrypt.BCrypt;

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
}
