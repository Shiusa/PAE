package be.vinci.pae.domain.ucc;

import be.vinci.pae.domain.dto.SupervisorDTO;
import be.vinci.pae.services.dao.SupervisorDAO;
import be.vinci.pae.utils.Logs;
import jakarta.inject.Inject;
import java.lang.module.ResolutionException;
import org.apache.logging.log4j.Level;

/**
 * Implementation of SupervisorUCC.
 */
public class SupervisorUCCImpl implements SupervisorUCC {

  @Inject
  private SupervisorDAO supervisorDAO;

  @Override
  public SupervisorDTO getOneById(int id) {
    Logs.log(Level.INFO, "SupervisorUCCImpl (getOneById) : entrance");
    SupervisorDTO supervisorDTO = supervisorDAO.getOneById(id);
    if (supervisorDTO == null) {
      Logs.log(Level.ERROR, "SupervisorUCCImpl (getOneById) : supervisor not found.");
      throw new ResolutionException();
    }
    Logs.log(Level.DEBUG, "SupervisorUCCImpl (getOneById) : success!");
    return supervisorDTO;
  }
}
