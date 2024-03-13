package be.vinci.pae.domain;

import java.util.Date;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Implementation of User.
 */
class UserImpl implements User {

  private int id;
  private String email;
  private String lastname;
  private String firstname;
  private String phoneNumber;
  private String password;
  private Date registrationDate;
  private String schoolyear;
  private String role;

  @Override
  public int getId() {
    return this.id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  @Override
  public String getEmail() {
    return this.email;
  }

  @Override
  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public String getLastname() {
    return this.lastname;
  }

  @Override
  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  @Override
  public String getFirstname() {
    return this.firstname;
  }

  @Override
  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  @Override
  public String getPhoneNumber() {
    return this.phoneNumber;
  }

  @Override
  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public Date getRegistrationDate() {
    return this.registrationDate;
  }

  @Override
  public void setRegistrationDate(Date registrationDate) {
    this.registrationDate = registrationDate;
  }

  @Override
  public String getSchoolYear() {
    return this.schoolyear;
  }

  @Override
  public void setSchoolYear(String schoolyear) {
    this.schoolyear = schoolyear;
  }

  @Override
  public String getRole() {
    return this.role;
  }

  @Override
  public void setRole(String role) {
    this.role = role;
  }

  @Override
  public boolean checkPassword(String password) {
    return BCrypt.checkpw(password, getPassword());
  }

  @Override
  public void hashPassword() {
    this.password = BCrypt.hashpw(this.password, BCrypt.gensalt());
  }


}
