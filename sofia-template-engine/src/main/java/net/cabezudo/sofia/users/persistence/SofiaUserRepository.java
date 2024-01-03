package net.cabezudo.sofia.users.persistence;

import net.cabezudo.sofia.emails.persistence.EMailEntity;
import net.cabezudo.sofia.persistence.DatabaseManager;
import net.cabezudo.sofia.users.service.Password;

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
public class SofiaUserRepository {
  private static final Logger log = LoggerFactory.getLogger(SofiaUserRepository.class);

  private @Autowired DatabaseManager databaseManager;

  @Transactional
  public SofiaUserEntity findByEmailAndAccount(int account_id, String email) {
    log.debug("Search users for account " + account_id + " and " + email);

    List<Map<String, Object>> list = new ArrayList<>(databaseManager.getJDBCTemplate().queryForList(
        "SELECT u.id AS id, a.id AS accountId, a.site_id AS accountSiteId, a.name AS accountName, e.id AS eMailId, e.email AS email, u.password, u.locale AS locale, u.enabled, authority " +
            "FROM accounts AS a " +
            "LEFT JOIN accounts_users AS au ON a.id = au.account_id " +
            "LEFT JOIN users AS u ON u.id = au.user_id " +
            "LEFT JOIN emails AS e ON u.email_id = e.id " +
            "LEFT JOIN authorities AS t ON au.id = t.account_user_id " +
            "WHERE u.email_id = e.id AND a.id = ? AND e.email = ?", account_id, email));

    if (list.size() == 0) {
      return null;
    }

    Map<String, Object> firstRecord = list.get(0);
    EMailEntity eMailEntity = new EMailEntity((int) firstRecord.get("eMailId"), (String) firstRecord.get("email"));
    SofiaUserEntity sofiaUserEntity = newUserEntity(firstRecord, eMailEntity);
    for (Map<String, Object> rs : list) {
      String groupName = (String) rs.get("authority");
      if (groupName != null) {
        sofiaUserEntity.add(new GroupEntity((int) firstRecord.get("id"), groupName));
      }
    }
    return sofiaUserEntity;
  }

  private SofiaUserEntity newUserEntity(Map<String, Object> map, EMailEntity eMailEntity) {
    return new SofiaUserEntity(
        (int) map.get("id"),
        (Integer) map.get("accountId"),
        (Integer) map.get("accountSiteId"),
        (String) map.get("accountName"),
        eMailEntity,
        (String) map.get("password"),
        (String) map.get("locale"),
        (boolean) map.get("enabled")
    );
  }

  @Transactional
  public SofiaUserEntityList findAll(int accountId, int siteId) {
    log.debug("Search users for account " + accountId);

    Map<Integer, SofiaUserEntity> map = new TreeMap<>();
    SofiaUserEntityList list = new SofiaUserEntityList();
    databaseManager.getJDBCTemplate().queryForList(
        "SELECT u.id AS id, a.id AS accountId, a.site_id AS accountSiteId, a.name AS accountName, e.id AS eMailId, e.email AS email, u.password, u.locale AS locale, u.enabled, authority " +
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
      SofiaUserEntity sofiaUserEntityFromMap = map.get(id);
      SofiaUserEntity newSofiaUserEntity;
      if (sofiaUserEntityFromMap == null) {
        // TODO user a row mapper
        EMailEntity eMailEntity = new EMailEntity((int) rs.get("eMailId"), (String) rs.get("email"));
        newSofiaUserEntity = newUserEntity(rs, eMailEntity);
        map.put(id, newSofiaUserEntity);
        list.add(newSofiaUserEntity);
        String authority = (String) rs.get("authority");
        if (authority != null) {
          newSofiaUserEntity.add(new GroupEntity(newSofiaUserEntity.getId(), authority));
        }
      } else {
        String authority = (String) rs.get("authority");
        if (authority != null) {
          sofiaUserEntityFromMap.add(new GroupEntity(sofiaUserEntityFromMap.getId(), authority));
        }
      }
    });
    list.setTotal(list.size()); // The total is the same as the size because there isn't pagination.
    return list;
  }

  public SofiaUserEntity get(int id) {
    log.debug("Search user for with id " + id);

    List<Map<String, Object>> list = new ArrayList<>(databaseManager.getJDBCTemplate().queryForList(
        "SELECT u.id AS id, a.id AS accountId, a.site_id AS accountSiteId, a.name AS accountName, e.id AS eMailId, e.email AS email, u.password, u.locale AS locale, u.enabled, authority " +
            "FROM accounts AS a " +
            "LEFT JOIN accounts_users AS au ON a.id = au.account_id " +
            "LEFT JOIN users AS u ON au.user_id = u.id " +
            "LEFT JOIN emails AS e ON u.email_id = e.id " +
            "LEFT JOIN authorities AS t ON au.id = t.account_user_id " +
            "WHERE au.owner = true AND u.id = ?", id));

    if (list.size() == 0) {
      return null;
    }

    Map<String, Object> firstRecord = list.get(0);

    EMailEntity eMailEntity = new EMailEntity((int) firstRecord.get("eMailId"), (String) firstRecord.get("email"));
    SofiaUserEntity sofiaUserEntity = newUserEntity(firstRecord, eMailEntity);
    for (Map<String, Object> rs : list) {
      String authority = (String) rs.get("authority");
      if (authority != null) {
        sofiaUserEntity.add(new GroupEntity(sofiaUserEntity.getId(), authority));
      }
    }
    return sofiaUserEntity;
  }

  public SofiaUserEntity findByAccount(int accountId, int id) {
    log.debug("Search user for with id " + id);

    List<Map<String, Object>> list = new ArrayList<>(databaseManager.getJDBCTemplate().queryForList(
        "SELECT u.id AS id, a.id AS accountId, a.site_id AS accountSiteId, a.name AS accountName, e.id AS eMailId, e.email AS email, u.password, u.locale AS locale, u.enabled, authority " +
            "FROM accounts AS a " +
            "LEFT JOIN accounts_users AS au ON a.id = au.account_id " +
            "LEFT JOIN users AS u ON au.user_id = u.id " +
            "LEFT JOIN emails AS e ON u.email_id = e.id " +
            "LEFT JOIN authorities AS t ON au.id = t.account_user_id " +
            "WHERE a.id = ? AND u.id = ?", accountId, id));

    if (list.size() == 0) {
      return null;
    }

    Map<String, Object> firstRecord = list.get(0);

    EMailEntity eMailEntity = new EMailEntity((int) firstRecord.get("eMailId"), (String) firstRecord.get("email"));
    SofiaUserEntity sofiaUserEntity = newUserEntity(firstRecord, eMailEntity);
    for (Map<String, Object> rs : list) {
      String authority = (String) rs.get("authority");
      if (authority != null) {
        sofiaUserEntity.add(new GroupEntity(sofiaUserEntity.getId(), authority));
      }
    }
    return sofiaUserEntity;
  }

  public SofiaUserEntity getForAccount(int accountId, int userId) {
    log.debug("Search user for account " + accountId + " with id " + userId);

    List<Map<String, Object>> list = new ArrayList<>(databaseManager.getJDBCTemplate().queryForList(
        "SELECT u.id AS id, a.id AS accountId, a.site_id AS accountSiteId, a.name AS accountName, e.id AS eMailId, e.email AS email, u.password, u.locale AS locale, u.enabled, authority " +
            "FROM accounts AS a " +
            "LEFT JOIN accounts_users AS au ON a.id = au.account_id " +
            "LEFT JOIN users AS u ON au.user_id = u.id " +
            "LEFT JOIN emails AS e ON u.email_id = e.id " +
            "LEFT JOIN authorities AS t ON au.id = t.account_user_id " +
            "WHERE a.id = ? AND u.id = ?", accountId, userId));

    if (list.size() == 0) {
      return null;
    }

    Map<String, Object> firstRecord = list.get(0);

    EMailEntity eMailEntity = new EMailEntity((int) firstRecord.get("eMailId"), (String) firstRecord.get("email"));
    SofiaUserEntity sofiaUserEntity = newUserEntity(firstRecord, eMailEntity);
    for (Map<String, Object> rs : list) {
      String authority = (String) rs.get("authority");
      if (authority != null) {
        sofiaUserEntity.add(new GroupEntity(sofiaUserEntity.getId(), authority));
      }
    }
    return sofiaUserEntity;
  }

  public SofiaUserEntity get(String eMail) {
    log.debug("Search user for account with e-mail " + eMail);

    List<Map<String, Object>> list = new ArrayList<>(databaseManager.getJDBCTemplate().queryForList(
        "SELECT u.id AS id, a.id AS accountId, a.site_id AS accountSiteId, a.name AS accountName, e.id AS eMailId, e.email AS email, u.password, u.locale AS locale, u.enabled, authority " +
            "FROM accounts AS a " +
            "LEFT JOIN accounts_users AS au ON a.id = au.account_id " +
            "LEFT JOIN users AS u ON au.user_id = u.id " +
            "LEFT JOIN emails AS e ON u.email_id = e.id " +
            "LEFT JOIN authorities AS t ON au.id = t.account_user_id " +
            "WHERE au.owner = true AND email = ?", eMail));

    if (list.size() == 0) {
      return null;
    }

    Map<String, Object> firstRecord = list.get(0);

    EMailEntity eMailEntity = new EMailEntity((int) firstRecord.get("eMailId"), (String) firstRecord.get("email"));
    SofiaUserEntity sofiaUserEntity = newUserEntity(firstRecord, eMailEntity);
    for (Map<String, Object> rs : list) {
      String authority = (String) rs.get("authority");
      if (authority != null) {
        sofiaUserEntity.add(new GroupEntity(sofiaUserEntity.getId(), authority));
      }
    }
    return sofiaUserEntity;
  }

  public SofiaUserEntity create(EMailEntity eMailEntity, String locale, boolean enabled) {
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
    return new SofiaUserEntity(Objects.requireNonNull(keyHolder.getKey()).intValue(), 0, 0, null, eMailEntity, null, locale, enabled);
  }

  public SofiaUserEntity update(SofiaUserEntity entity) {
    int id = entity.getId();
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

    return new SofiaUserEntity(id, entity.getAccount(), eMailEntity, password, locale, enabled);
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

  public SofiaUserEntity findByEmail(String email) {
    log.debug("Search user for email " + email);

    List<Map<String, Object>> list = new ArrayList<>(databaseManager.getJDBCTemplate().queryForList(
        "SELECT u.id AS id, a.id AS accountId, a.site_id AS accountSiteId, a.name AS accountName, e.id AS eMailId, e.email AS email, u.password, u.locale AS locale, u.enabled, authority " +
            "FROM accounts AS a " +
            "LEFT JOIN accounts_users AS au ON a.id = au.account_id " +
            "LEFT JOIN users AS u ON u.id = au.user_id " +
            "LEFT JOIN emails AS e ON u.email_id = e.id " +
            "LEFT JOIN authorities AS t ON au.id = t.account_user_id " +
            "WHERE u.email_id = e.id AND au.owner = true AND e.email = ?", email));

    if (list.size() == 0) {
      return null;
    }

    Map<String, Object> firstRecord = list.get(0);
    EMailEntity eMailEntity = new EMailEntity((int) firstRecord.get("eMailId"), (String) firstRecord.get("email"));
    SofiaUserEntity sofiaUserEntity = newUserEntity(firstRecord, eMailEntity);
    for (Map<String, Object> rs : list) {
      String groupName = (String) rs.get("authority");
      if (groupName != null) {
        sofiaUserEntity.add(new GroupEntity((int) firstRecord.get("id"), groupName));
      }
    }
    return sofiaUserEntity;
  }

  public SofiaUserEntity getByEMail(int id) {
    log.debug("Search user for email " + id);

    List<Map<String, Object>> list = new ArrayList<>(databaseManager.getJDBCTemplate().queryForList(
        "SELECT u.id AS id, a.id AS accountId, a.site_id AS accountSiteId, a.name AS accountName, e.id AS eMailId, e.email AS email, u.password, u.locale AS locale, u.enabled, authority " +
            "FROM accounts AS a " +
            "LEFT JOIN accounts_users AS au ON a.id = au.account_id " +
            "LEFT JOIN users AS u ON u.id = au.user_id " +
            "LEFT JOIN emails AS e ON u.email_id = e.id " +
            "LEFT JOIN authorities AS t ON au.id = t.account_user_id " +
            "WHERE u.email_id = e.id AND au.owner = true AND e.id = ?", id));

    if (list.size() == 0) {
      return null;
    }

    Map<String, Object> firstRecord = list.get(0);
    EMailEntity eMailEntity = new EMailEntity((int) firstRecord.get("eMailId"), (String) firstRecord.get("email"));
    SofiaUserEntity sofiaUserEntity = newUserEntity(firstRecord, eMailEntity);
    for (Map<String, Object> rs : list) {
      String groupName = (String) rs.get("authority");
      if (groupName != null) {
        sofiaUserEntity.add(new GroupEntity((int) firstRecord.get("id"), groupName));
      }
    }
    return sofiaUserEntity;
  }

  public Integer getIdByEMail(int emailId) {
    String query = "SELECT id FROM users WHERE email_id = ?";
    return databaseManager.getJDBCTemplate().query(query, (resultSet, rowNum) -> resultSet.getInt("id"), emailId).stream().findFirst().orElse(null);
  }
}
