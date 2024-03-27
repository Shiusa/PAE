package be.vinci.pae.domain.ucc;

import be.vinci.pae.domain.dto.InternshipDTO;
import be.vinci.pae.services.dal.DalServices;
import be.vinci.pae.services.dao.ContactDAO;
import be.vinci.pae.services.dao.InternshipDAO;
import be.vinci.pae.utils.exceptions.FatalException;
import be.vinci.pae.utils.exceptions.NotAllowedException;
import be.vinci.pae.utils.exceptions.ResourceNotFoundException;
import jakarta.inject.Inject;

/**
 * Implementation of InternshipUCC.
 */
public class InternshipUCCImpl implements InternshipUCC {

  @Inject
  private InternshipDAO internshipDAO;
  @Inject
  private DalServices dalServices;
  @Inject
  private ContactDAO contactDAO;


  @Override
  public InternshipDTO getOneByStudent(int student) {
    InternshipDTO internship;
    try {
      dalServices.startTransaction();
      internship = internshipDAO.getOneInternshipByIdUser(student);
      if (internship == null) {
        dalServices.rollbackTransaction();
        throw new ResourceNotFoundException();
      }
    } catch (FatalException e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    dalServices.commitTransaction();
    return internship;
  }


  @Override
  public InternshipDTO getOneById(int id, int actualStudent) {
    InternshipDTO internship;
    try {
      dalServices.startTransaction();
      internship = internshipDAO.getOneInternshipById(id);
      if (internship == null) {
        dalServices.rollbackTransaction();
        throw new ResourceNotFoundException();
      } else if (contactDAO.getOneContactById(internship.getContact()).getStudent()
          != actualStudent) {
        dalServices.rollbackTransaction();
        throw new NotAllowedException();
      }
    } catch (FatalException e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    dalServices.commitTransaction();
    return internship;
  }

}
