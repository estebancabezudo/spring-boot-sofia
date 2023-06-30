package net.cabezudo.sofia.users.persistence;

import net.cabezudo.sofia.config.DatabaseConfiguration;
import net.cabezudo.sofia.emails.persistence.EMailEntity;
import net.cabezudo.sofia.persistence.DatabaseManager;
import net.cabezudo.sofia.users.Password;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

@Repository
public class UserRepository {
  private static final Logger log = LoggerFactory.getLogger(UserRepository.class);

  private @Autowired DatabaseManager databaseManager;

  @Transactional
  public UserEntity findByEmailAndAccount(int account_id, String email) {
    log.debug("Search users for account " + account_id + " and " + email);

    List<Map<String, Object>> list = new ArrayList<>(databaseManager.getJDBCTemplate().queryForList(
        "SELECT u.id AS id, a.site_id, a.id AS account_user_id, e.id AS eMailId, e.email AS email, u.password, u.locale AS locale, u.enabled, authority " +
            "FROM accounts AS a " +
            "LEFT JOIN accounts_users AS au ON a.id = au.account_id " +
            "LEFT JOIN users AS u ON u.id = au.user_id " +
            "LEFT JOIN `" + DatabaseConfiguration.DEFAULT_SCHEMA + "`.emails AS e ON u.email_id = e.id " +
            "LEFT JOIN authorities AS t ON au.id = t.account_user_id " +
            "WHERE u.email_id = e.id AND a.id = ? AND e.email = ?", account_id, email));

    if (list.size() == 0) {
      return null;
    }

    Map<String, Object> firstRecord = list.get(0);
    EMailEntity eMailEntity = new EMailEntity((int) firstRecord.get("eMailId"), (String) firstRecord.get("email"));
    UserEntity userEntity = newUserEntity(firstRecord, eMailEntity);
    for (Map<String, Object> rs : list) {
      String groupName = (String) rs.get("authority");
      if (groupName != null) {
        userEntity.add(new GroupEntity((int) firstRecord.get("id"), groupName));
      }
    }
    return userEntity;
  }

  private UserEntity newUserEntity(Map<String, Object> firstRecord, EMailEntity eMailEntity) {
    return new UserEntity(
        (int) firstRecord.get("id"),
        (Integer) firstRecord.get("site_id"),
        (Integer) firstRecord.get("account_user_id"),
        eMailEntity, (String) firstRecord.get("password"),
        (String) firstRecord.get("locale"),
        (boolean) firstRecord.get("enabled")
    );
  }

  @Transactional
  public UserEntityList findAll(int accountId, int siteId) {
    log.debug("Search users for account " + accountId);

    Map<Integer, UserEntity> map = new TreeMap<>();
    UserEntityList list = new UserEntityList();
    databaseManager.getJDBCTemplate().queryForList(
        "SELECT u.id AS id, a.site_id, au.id AS account_user_id, e.id AS eMailId, e.email AS email, u.password, u.locale AS locale, u.enabled, authority " +
            "FROM users AS u " +
            "LEFT JOIN accounts_users AS au ON u.id = au.user_id " +
            "LEFT JOIN accounts AS a ON au.account_id = a.id " +
            "LEFT JOIN emails AS e ON u.email_id = e.id " +
            "LEFT JOIN authorities AS t ON au.id = t.account_user_id " +
            "WHERE a.id = ? AND a.site_id = ? " +
            "ORDER BY email"
        , accountId, siteId
    ).forEach(rs -> {
      int id = (Integer) rs.get("id");
      UserEntity userEntityFromMap = map.get(id);
      UserEntity newUserEntity;
      if (userEntityFromMap == null) {
        // TODO user a row mapper
        EMailEntity eMailEntity = new EMailEntity((int) rs.get("eMailId"), (String) rs.get("email"));
        newUserEntity = new UserEntity(
            (int) rs.get("id"),
            (Integer) rs.get("site_id"),
            (Integer) rs.get("account_user_id"),
            eMailEntity,
            (String) rs.get("password"),
            (String) rs.get("locale"),
            (boolean) rs.get("enabled")
        );
        map.put(id, newUserEntity);
        list.add(newUserEntity);
        String authority = (String) rs.get("authority");
        if (authority != null) {
          newUserEntity.add(new GroupEntity(newUserEntity.getId(), authority));
        }
      } else {
        String authority = (String) rs.get("authority");
        if (authority != null) {
          userEntityFromMap.add(new GroupEntity(userEntityFromMap.getId(), authority));
        }
      }
    });
    list.setTotal(list.size()); // The total is the same as the size because there isn't pagination.
    return list;
  }

  public UserEntity get(int accountId, int userId) {
    log.debug("Search user with id " + userId + " in " + accountId + " account");

    List<Map<String, Object>> list = new ArrayList<>(databaseManager.getJDBCTemplate().queryForList(
        "SELECT u.id AS id, a.site_id, au.id AS account_user_id, e.id AS email_id, e.email AS email, u.password, u.locale AS locale, u.enabled, authority " +
            "FROM accounts AS a " +
            "LEFT JOIN accounts_users AS au ON a.id = au.account_id " +
            "LEFT JOIN users AS u ON au.user_id = u.id " +
            "LEFT JOIN emails AS e ON u.email_id = e.id " +
            "LEFT JOIN authorities AS t ON au.id = t.account_user_id " +
            "WHERE a.id = ? AND u.id = ?", accountId, userId
    ));

    if (list.size() == 0) {
      return null;
    }

    Map<String, Object> firstRecord = list.get(0);

    EMailEntity eMailEntity = new EMailEntity((int) firstRecord.get("email_id"), (String) firstRecord.get("email"));
    // TODO user a row mapper
    UserEntity userEntity = new UserEntity(
        (int) firstRecord.get("id"),
        (Integer) firstRecord.get("site_id"),
        (Integer) firstRecord.get("account_user_id"),
        eMailEntity,
        (String) firstRecord.get("password"),
        (String) firstRecord.get("locale"),
        (boolean) firstRecord.get("enabled")
    );
    for (Map<String, Object> rs : list) {
      String authority = (String) rs.get("authority");
      if (authority != null) {
        userEntity.add(new GroupEntity(userEntity.getId(), authority));
      }
    }
    return userEntity;
  }

  public UserEntity get(int accountId, String eMail) {
    log.debug("Search user for account " + accountId + " with e-mail " + eMail);

    List<Map<String, Object>> list = new ArrayList<>(databaseManager.getJDBCTemplate().queryForList(
        "SELECT u.id AS id, a.site_id, au.id AS account_user_id, e.id AS eMailId, e.email AS email, u.password, u.locale AS locale, u.enabled, authority " +
            "FROM accounts AS a " +
            "LEFT JOIN accounts_users AS au ON a.id = au.account_id " +
            "LEFT JOIN users AS u ON au.user_id = u.id " +
            "LEFT JOIN `" + DatabaseConfiguration.DEFAULT_SCHEMA + "`.emails AS e ON u.email_id = e.id " +
            "LEFT JOIN authorities AS t ON au.id = t.account_user_id " +
            "WHERE a.id = ? AND email = ?",
        accountId, eMail));

    if (list.size() == 0) {
      return null;
    }

    Map<String, Object> firstRecord = list.get(0);

    EMailEntity eMailEntity = new EMailEntity((int) firstRecord.get("eMailId"), (String) firstRecord.get("email"));
    UserEntity userEntity = new UserEntity(
        (int) firstRecord.get("id"),
        (Integer) firstRecord.get("site_id"),
        (Integer) firstRecord.get("account_user_id"),
        eMailEntity,
        (String) firstRecord.get("password"),
        (String) firstRecord.get("locale"),
        (boolean) firstRecord.get("enabled")
    );
    for (Map<String, Object> rs : list) {
      String authority = (String) rs.get("authority");
      if (authority != null) {
        userEntity.add(new GroupEntity(userEntity.getId(), authority));
      }
    }
    return userEntity;
  }

  public UserEntity create(EMailEntity eMailEntity, String locale, boolean enabled) {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    PreparedStatementCreator preparedStatementCreator = connection -> {
      String query = "INSERT INTO users (email_id, locale, enabled) values(?, ?, ?)";
      PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
      ps.setInt(1, eMailEntity.getId());
      ps.setString(2, locale);
      ps.setBoolean(3, enabled);
      return ps;
    };
    databaseManager.getJDBCTemplate().update(preparedStatementCreator, keyHolder);
    return new UserEntity(Objects.requireNonNull(keyHolder.getKey()).intValue(), 0, 0, eMailEntity, null, locale, enabled);
  }

  public UserEntity update(UserEntity entity) {
    int id = entity.getId();
    int accountId = entity.getSiteId();
    EMailEntity eMailEntity = entity.getEMailEntity();
    String password = entity.getPassword();
    String locale = entity.getLocale();
    boolean enabled = entity.isEnabled();

    PreparedStatementCreator preparedStatementCreator = connection -> {
      String query = "UPDATE users SET email_id = ?, enabled = ? WHERE id = ?";
      PreparedStatement ps = connection.prepareStatement(query);
      ps.setInt(1, eMailEntity.getId());
      ps.setBoolean(2, enabled);
      ps.setInt(3, id);
      return ps;
    };
    databaseManager.getJDBCTemplate().update(preparedStatementCreator);

    return new UserEntity(id, accountId, entity.getAccountUserId(), eMailEntity, password, locale, enabled);
  }

  public void delete(Integer id) {
    PreparedStatementCreator preparedStatementCreator = connection -> {
      String query = "DELETE FROM users WHERE id = ?";
      PreparedStatement ps = connection.prepareStatement(query);
      ps.setInt(1, id);
      return ps;
    };
    databaseManager.getJDBCTemplate().update(preparedStatementCreator);
  }

  public void update(int id, Password password) {
    PreparedStatementCreator preparedStatementCreator = connection -> {
      String query = "UPDATE users SET password = ? WHERE id = ?";
      PreparedStatement ps = connection.prepareStatement(query);
      ps.setString(1, password.toString());
      ps.setInt(2, id);
      return ps;
    };
    databaseManager.getJDBCTemplate().update(preparedStatementCreator);
  }

  public UserEntity findByEmail(String email) {
    log.debug("Search user for email " + email);

    List<Map<String, Object>> list = new ArrayList<>(databaseManager.getJDBCTemplate().queryForList(
        "SELECT u.id AS id, a.site_id, a.id AS account_user_id, e.id AS eMailId, e.email AS email, u.password, u.locale AS locale, u.enabled, authority " +
            "FROM accounts AS a " +
            "LEFT JOIN accounts_users AS au ON a.id = au.account_id " +
            "LEFT JOIN users AS u ON u.id = au.user_id " +
            "LEFT JOIN `" + DatabaseConfiguration.DEFAULT_SCHEMA + "`.emails AS e ON u.email_id = e.id " +
            "LEFT JOIN authorities AS t ON au.id = t.account_user_id " +
            "WHERE u.email_id = e.id AND au.owner = true AND e.email = ?", email));

    if (list.size() == 0) {
      return null;
    }

    Map<String, Object> firstRecord = list.get(0);
    EMailEntity eMailEntity = new EMailEntity((int) firstRecord.get("eMailId"), (String) firstRecord.get("email"));
    UserEntity userEntity = newUserEntity(firstRecord, eMailEntity);
    for (Map<String, Object> rs : list) {
      String groupName = (String) rs.get("authority");
      if (groupName != null) {
        userEntity.add(new GroupEntity((int) firstRecord.get("id"), groupName));
      }
    }
    return userEntity;
  }
}
