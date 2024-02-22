package be.vinci.pae.domain.dto;

import java.time.LocalDate;

public interface UserDTO {

  int getId();

  String getEmail();

  String getNom();

  String getPrenom();

  String getTelephone();

  String getMotDePasse();

  LocalDate getDateInscription();

  String getAnneeAcademique();

  String getRole();


  void setId(int id);

  void setEmail(String email);

  void setNom(String nom);

  void setPrenom(String prenom);

  void setTelephone(String telephone);

  void setMotDePasse(String motDePasse);

  void setDateInscription(LocalDate dateInscription);

  void setAnneeAcademique(String anneeAcademique);

  void setRole(String role);
}
