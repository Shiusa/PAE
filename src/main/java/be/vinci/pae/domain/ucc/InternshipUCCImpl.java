package be.vinci.pae.domain.ucc;

import be.vinci.pae.domain.Contact;
import be.vinci.pae.domain.dto.InternshipDTO;
import be.vinci.pae.services.dal.DalServices;
import be.vinci.pae.services.dao.ContactDAO;
import be.vinci.pae.services.dao.InternshipDAO;
import be.vinci.pae.utils.Logs;
import be.vinci.pae.utils.exceptions.DuplicateException;
import be.vinci.pae.utils.exceptions.InvalidRequestException;
import be.vinci.pae.utils.exceptions.NotAllowedException;
import be.vinci.pae.utils.exceptions.ResourceNotFoundException;
import be.vinci.pae.utils.exceptions.UnauthorizedAccessException;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Level;

/**
 * Implementation of InternshipUCC.
 */
public class InternshipUCCImpl implements InternshipUCC {

  @Inject
  private InternshipDAO internshipDAO;
  @Inject
  private DalServices dalServices;
  @Inject
  private ContactUCC contactUCC;


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

  @Override
  public InternshipDTO createInternship(InternshipDTO internshipDTO, int studentId) {
    Logs.log(Level.INFO, "InternshipUCC (createInternship) : entrance");
    if (studentId != internshipDTO.getContact().getStudent().getId()) {
      Logs.log(Level.ERROR, "InternshipUCC (createInternship) : wrong student");
      throw new UnauthorizedAccessException("This user can't create this internship");
    }
    internshipDTO.setSchoolYear(internshipDTO.getContact().getSchoolYear());
    try {
      if (!((Contact) internshipDTO.getContact()).isAccepted()) {
        Logs.log(Level.ERROR, "InternshipUCC (createInternship) : contact is not accepted");
        throw new InvalidRequestException("Contact is not accepted");
      }

      dalServices.startTransaction();
      InternshipDTO existingInternship = internshipDAO.getOneInternshipByIdUser(
          internshipDTO.getContact().getStudent().getId());
      if (existingInternship != null) {
        Logs.log(Level.ERROR, "InternshipUCC (createInternship) : internship already created");
        throw new DuplicateException("Cannot add existing internship");
      }

      InternshipDTO internship = internshipDAO.createInternship(internshipDTO);
      dalServices.commitTransaction();
      contactUCC.putStudentContactsOnHold(internshipDTO.getContact().getStudent().getId());
      return internship;
    } catch (Exception e) {
      Logs.log(Level.ERROR, "InternshipUCC (createInternship) : creation failed " + e);
      dalServices.rollbackTransaction();
      throw e;
    }
  }

  @Override
  public Map<String, Integer[]> getInternshipCountByYear() {
    try {
      dalServices.startTransaction();
      Map<String, Integer[]> returnedMap = internshipDAO.getInternshipCountByYear();
      dalServices.commitTransaction();
      return returnedMap;
    } catch (Exception e) {
      dalServices.rollbackTransaction();
      throw e;
    }
  }

}
