package users;

public class UserPreferencesManager {
  private static final UserPreferencesManager INSTANCE = new UserPreferencesManager();

  public static UserPreferencesManager getInstance() {
    return INSTANCE;
  }

  public UserPreferences get(String username) {
    return null;
  }

  public void set(String name, UserPreferences userPreferences) {
  }
}
