package be.vinci.pae.domain.dto;

import java.sql.Date;

/**
 * UserDTO interface containing only getters and setters of a User.
 */
public interface UserDTO {

  /**
   * Get the user's id.
   *
   * @return the user's id.
   */
  int getId();

  /**
   * Set the user's id.
   *
   * @param id id to set.
   */
  void setId(int id);

  /**
   * Get the user's email.
   *
   * @return the user's email.
   */
  String getEmail();

  /**
   * Set the user's email.
   *
   * @param email email to set.
   */
  void setEmail(String email);

  /**
   * Get the user's lastname.
   *
   * @return the user's lastname.
   */
  String getNom();

  /**
   * Set the user's lastname.
   *
   * @param nom lastname to set.
   */
  void setNom(String nom);

  /**
   * Get the user's firstname.
   *
   * @return the user's firstname.
   */
  String getPrenom();

  /**
   * Set the user's firstname.
   *
   * @param prenom firstname to set.
   */
  void setPrenom(String prenom);

  /**
   * Get the user's phone number.
   *
   * @return the user's phone number.
   */
  String getTelephone();

  /**
   * Set the user's phone number.
   *
   * @param telephone phone number to set.
   */
  void setTelephone(String telephone);

  /**
   * Get the user's password.
   *
   * @return the user's password.
   */
  String getMotDePasse();

  /**
   * Set the user's password.
   *
   * @param motDePasse password to set.
   */
  void setMotDePasse(String motDePasse);

  /**
   * Get the user's password.
   *
   * @return the user's register date.
   */
  Date getDateInscription();

  /**
   * Set the user's register date.
   *
   * @param dateInscription password to set.
   */
  void setDateInscription(Date dateInscription);

  /**
   * Get the user's academic year.
   *
   * @return the user's register date.
   */
  String getAnneeAcademique();

  /**
   * Set the user's academic year.
   *
   * @param anneeAcademique password to set.
   */
  void setAnneeAcademique(String anneeAcademique);

  /**
   * Get the user's role.
   *
   * @return the user's register date.
   */
  String getRole();

  /**
   * Set the user's role.
   *
   * @param role password to set.
   */
  void setRole(String role);
}
