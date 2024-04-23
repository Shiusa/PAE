package be.vinci.pae.services.dao;

import be.vinci.pae.domain.dto.SupervisorDTO;
import java.util.List;

/**
 * SupervisorDAO interface.
 */
public interface SupervisorDAO {

  /**
   * Get one supervisor by his id.
   *
   * @param id the id.
   * @return the supervisorDTO.
   */
  SupervisorDTO getOneById(int id);

  /**
   * Get all the supervisors of a company.
   *
   * @param companyId the company's id.
   * @return all the supervisors from a company.
   */
  List<SupervisorDTO> getAllByCompany(int companyId);

}
