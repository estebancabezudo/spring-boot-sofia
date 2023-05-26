package net.cabezudo.sofia.userpreferences.persistence;

import net.cabezudo.sofia.core.SofiaRuntimeException;
import net.cabezudo.sofia.userpreferences.UserPreferences;
import org.springframework.stereotype.Repository;

@Repository
public class AccountPreferencesRepository {
  public UserPreferences get(int accountId, String name) {
    throw new SofiaRuntimeException("Not implemented");
  }
}
