package be.vinci.pae.domain;

import java.util.Date;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Implementation of User.
 */
class UserImpl implements User {

  private int id;
  private String email;
  private String nom;
  private String prenom;
  private String telephone;
  private String motDePasse;
  private Date dateInscription;
  private String anneeAcademique;
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
  public String getNom() {
    return this.nom;
  }

  @Override
  public void setNom(String nom) {
    this.nom = nom;
  }

  @Override
  public String getPrenom() {
    return this.prenom;
  }

  @Override
  public void setPrenom(String prenom) {
    this.prenom = prenom;
  }

  @Override
  public String getTelephone() {
    return this.telephone;
  }

  @Override
  public void setTelephone(String telephone) {
    this.telephone = telephone;
  }

  @Override
  public String getMotDePasse() {
    return this.motDePasse;
  }

  @Override
  public void setMotDePasse(String motDePasse) {
    this.motDePasse = motDePasse;
  }

  @Override
  public Date getDateInscription() {
    return this.dateInscription;
  }

  @Override
  public void setDateInscription(Date dateInscription) {
    this.dateInscription = dateInscription;
  }

  @Override
  public String getAnneeAcademique() {
    return this.anneeAcademique;
  }

  @Override
  public void setAnneeAcademique(String anneeAcademique) {
    this.anneeAcademique = anneeAcademique;
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
  public boolean checkMotDePasse(String motDePasse) {
    return BCrypt.checkpw(motDePasse, getMotDePasse());
  }

  @Override
  public void hasherMotDePasse() {
    this.motDePasse = BCrypt.hashpw(this.motDePasse, BCrypt.gensalt());
  }


}
