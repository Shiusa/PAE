package be.vinci.pae.services.dao;

import be.vinci.pae.domain.dto.SupervisorDTO;

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

}
