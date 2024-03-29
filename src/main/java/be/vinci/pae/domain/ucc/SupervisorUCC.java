package be.vinci.pae.domain.ucc;

import be.vinci.pae.domain.dto.SupervisorDTO;

/**
 * SupervisorUCC interface.
 */
public interface SupervisorUCC {

  /**
   * Get a supervisor by his id.
   *
   * @param id the id.
   * @return the supervisorDTO found.
   */
  SupervisorDTO getOneById(int id);

}
