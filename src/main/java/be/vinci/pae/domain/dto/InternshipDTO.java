package be.vinci.pae.domain.dto;

import java.util.Date;

/**
 * InternshipDTO interface.
 */

public interface InternshipDTO {

  int getId();

  void setId(int id);

  int getContact();

  void setContact(int contact);

  int getSupervisor();

  void setSupervisor(int supervisor);

  Date getSignatureDate();

  void setSignatureDate(Date signatureDate);

  String getProject();

  void setProject(String project);

  String getSchoolYear();

  void setSchoolYear(String schoolYear);
}
