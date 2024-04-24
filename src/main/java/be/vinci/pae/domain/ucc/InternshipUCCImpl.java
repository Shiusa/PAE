package be.vinci.pae.domain.ucc;

import be.vinci.pae.domain.dto.InternshipDTO;
import be.vinci.pae.services.dal.DalServices;
import be.vinci.pae.services.dao.InternshipDAO;
import be.vinci.pae.utils.exceptions.NotAllowedException;
import be.vinci.pae.utils.exceptions.ResourceNotFoundException;
import jakarta.inject.Inject;
import java.util.List;

/**
 * Implementation of InternshipUCC.
 */
public class InternshipUCCImpl implements InternshipUCC {

  @Inject
  private InternshipDAO internshipDAO;
  @Inject
  private DalServices dalServices;


  @Override
  public InternshipDTO getOneByStudent(int student) {
    InternshipDTO internship;
    try {
      dalServices.startTransaction();
      internship = internshipDAO.getOneInternshipByIdUser(student);
      if (internship == null) {
        throw new ResourceNotFoundException();
      }
      dalServices.commitTransaction();
      return internship;
    } catch (Exception e) {
      dalServices.rollbackTransaction();
      throw e;
    }
  }


  @Override
  public InternshipDTO getOneById(int id, int actualStudent) {
    InternshipDTO internship;
    try {
      dalServices.startTransaction();
      internship = internshipDAO.getOneInternshipById(id);
      if (internship == null) {
        throw new ResourceNotFoundException();
      } else if (internship.getContact().getStudent().getId()
          != actualStudent) {
        throw new NotAllowedException();
      }
      dalServices.commitTransaction();
      return internship;
    } catch (Exception e) {
      dalServices.rollbackTransaction();
      throw e;
    }
  }

  public List<InternshipDTO> getAllInternships() {
    try {
      dalServices.startTransaction();
      List<InternshipDTO> internshipDTOList = internshipDAO.getAllInternships();
      dalServices.commitTransaction();
      return internshipDTOList;
    } catch (Exception e) {
      dalServices.rollbackTransaction();
      throw e;
    }
  }

}
