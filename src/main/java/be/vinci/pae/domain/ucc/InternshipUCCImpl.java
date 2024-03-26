package be.vinci.pae.domain.ucc;

import be.vinci.pae.domain.Internship;
import be.vinci.pae.domain.dto.InternshipDTO;
import be.vinci.pae.services.dal.DalServicesConnection;
import be.vinci.pae.services.dao.InternshipDAO;
import jakarta.inject.Inject;

public class InternshipUCCImpl implements InternshipUCC {

  @Inject
  private InternshipDAO internshipDAO;
  @Inject
  private DalServicesConnection dalServices;


  @Override
  public InternshipDTO getOneByStudent(int student) {
    dalServices.startTransaction();
    InternshipDTO internship = internshipDAO.getOneInternshipByIdUser(student);
    if (internship == null) {
      dalServices.rollbackTransaction();
      throw new IllegalArgumentException("L'étudiant n'a pas de stage");
    }
    dalServices.commitTransaction();
    return internship;
  }


  @Override
  public InternshipDTO getOneById(int id) {
    dalServices.startTransaction();
    InternshipDTO internship = internshipDAO.getOneInternshipById(id);
    if (internship == null) {
      dalServices.rollbackTransaction();
      throw new IllegalArgumentException("id unknown");
    }
    dalServices.commitTransaction();
    return internship;
  }

}
