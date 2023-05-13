package net.cabezudo.sofia.users.persistence;

import net.cabezudo.sofia.emails.persistence.EMailEntity;
import net.cabezudo.sofia.emails.persistence.EMailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Repository
public class UserRepository {
  private static final Logger log = LoggerFactory.getLogger(UserRepository.class);

  private @Autowired JdbcTemplate jdbcTemplate;
  private @Autowired EMailRepository eMailRepository;
  private @Autowired PasswordEncoder passwordEncoder;

  @Transactional
  public UserEntity findByEmail(int account_id, String email) {
    log.debug("Search user for account " + account_id + " and " + email);

    List<Map<String, Object>> list = new ArrayList<>();
    jdbcTemplate.queryForList(
        "SELECT u.id AS id, a.site_id, e.id AS eMailId, e.email AS email, u.password, u.enabled, authority " +
            "FROM accounts AS a " +
            "LEFT JOIN users AS u ON a.id = u.account_id " +
            "LEFT JOIN emails AS e ON u.email_id = e.id " +
            "LEFT JOIN authorities AS t ON u.id = t.user_id " +
            "WHERE u.email_id = e.id AND u.id = a.user_id AND site_id = ? AND e.email = ?", account_id, email).forEach(rs -> {
      list.add(rs);
    });

    if (list.size() == 0) {
      return null;
    }

    Map<String, Object> firstRecord = list.get(0);
    EMailEntity eMailEntity = new EMailEntity((int) firstRecord.get("eMailId"), (String) firstRecord.get("email"));
    UserEntity userEntity = new UserEntity((int) firstRecord.get("id"), (Integer) firstRecord.get("site_id"), eMailEntity, (String) firstRecord.get("password"), (boolean) firstRecord.get("enabled"));
    for (Map<String, Object> rs : list) {
      userEntity.add(new GroupEntity((int) firstRecord.get("id"), (String) rs.get("authority")));
    }
    return userEntity;
  }

  @Transactional
  public UserEntityList findAll(int accountId) {
    log.debug("Search users for account " + accountId);

    Map<Integer, UserEntity> map = new TreeMap<>();
    UserEntityList list = new UserEntityList();
    jdbcTemplate.queryForList(
        "SELECT u.id AS id, a.site_id, e.id AS eMailId, e.email AS email, u.password, u.enabled, authority " +
            "FROM accounts AS a " +
            "LEFT JOIN users AS u ON a.id = u.account_id " +
            "LEFT JOIN emails AS e ON u.email_id = e.id " +
            "LEFT JOIN authorities AS t ON u.id = t.user_id " +
            "WHERE a.id = ? " +
            "ORDER BY email"
        , accountId
    ).stream().forEach(rs -> {
      int id = (Integer) rs.get("id");
      UserEntity userEntityFromMap = map.get(id);
      UserEntity newUserEntity;
      if (userEntityFromMap == null) {
        EMailEntity eMailEntity = new EMailEntity((int) rs.get("eMailId"), (String) rs.get("email"));
        newUserEntity = new UserEntity((int) rs.get("id"), (Integer) rs.get("site_id"), eMailEntity, (String) rs.get("password"), (boolean) rs.get("enabled"));
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

  public UserEntity get(int id) {
    log.debug("Search user with id " + id);

    List<Map<String, Object>> list = new ArrayList<>();
    jdbcTemplate.queryForList(
        "SELECT u.id AS id, a.site_id, e.id AS eMailId, e.email AS email, u.password, u.enabled, authority " +
            "FROM accounts AS a " +
            "LEFT JOIN users AS u ON a.id = u.account_id " +
            "LEFT JOIN emails AS e ON u.email_id = e.id " +
            "LEFT JOIN authorities AS t ON u.id = t.user_id " +
            "WHERE u.id = ?", id
    ).forEach(rs -> {
      list.add(rs);
    });

    if (list.size() == 0) {
      return null;
    }

    Map<String, Object> firstRecord = list.get(0);

    EMailEntity eMailEntity = new EMailEntity((int) firstRecord.get("eMailId"), (String) firstRecord.get("email"));
    UserEntity userEntity = new UserEntity((int) firstRecord.get("id"), (Integer) firstRecord.get("site_id"), eMailEntity, (String) firstRecord.get("password"), (boolean) firstRecord.get("enabled"));
    for (Map<String, Object> rs : list) {
      String authority = (String) rs.get("authority");
      if (authority != null) {
        userEntity.add(new GroupEntity(userEntity.getId(), authority));
      }
    }
    return userEntity;
  }

  public UserEntity get(int accountId, String eMail) {
    log.debug("Search user with e-mail " + eMail + " for account " + accountId);

    List<Map<String, Object>> list = new ArrayList<>();
    jdbcTemplate.queryForList(
        "SELECT u.id AS id, a.site_id, e.id AS eMailId, e.email AS email, u.password, u.enabled, authority " +
            "FROM accounts AS a " +
            "LEFT JOIN users AS u ON a.id = u.account_id " +
            "LEFT JOIN emails AS e ON u.email_id = e.id " +
            "LEFT JOIN authorities AS t ON u.id = t.user_id " +
            "WHERE account_id= ? AND email = ?",
        accountId, eMail).forEach(rs -> {
      list.add(rs);
    });

    if (list.size() == 0) {
      return null;
    }

    Map<String, Object> firstRecord = list.get(0);

    EMailEntity eMailEntity = new EMailEntity((int) firstRecord.get("eMailId"), (String) firstRecord.get("email"));
    UserEntity userEntity = new UserEntity((int) firstRecord.get("id"), (Integer) firstRecord.get("site_id"), eMailEntity, (String) firstRecord.get("password"), (boolean) firstRecord.get("enabled"));
    for (Map<String, Object> rs : list) {
      String authority = (String) rs.get("authority");
      if (authority != null) {
        userEntity.add(new GroupEntity(userEntity.getId(), authority));
      }
    }
    return userEntity;
  }

  public UserEntity create(int accountId, EMailEntity eMailEntity, String password, boolean enabled) {
    String encodedPassword = password == null ? null : passwordEncoder.encode(password);
    KeyHolder keyHolder = new GeneratedKeyHolder();
    PreparedStatementCreator preparedStatementCreator = connection -> {
      String query = "INSERT INTO users (account_id, email_id, password, enabled) values(?, ?, ?, ?)";
      PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
      ps.setInt(1, accountId);
      ps.setInt(2, eMailEntity.id());
      ps.setString(3, encodedPassword);
      ps.setBoolean(4, enabled);
      return ps;
    };
    jdbcTemplate.update(preparedStatementCreator, keyHolder);

    return new UserEntity(keyHolder.getKey().intValue(), accountId, eMailEntity, password, enabled);
  }

  public UserEntity update(UserEntity entity) {
    int id = entity.getId();
    int accountId = entity.getAccountId();
    EMailEntity eMailEntity = entity.getEMailEntity();
    String password = entity.getPassword();
    boolean enabled = entity.isEnabled();

    PreparedStatementCreator preparedStatementCreator = connection -> {
      String query = "UPDATE users SET email_id = ?, enabled = ? WHERE id = ?";
      PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
      ps.setInt(1, eMailEntity.id());
      ps.setBoolean(2, enabled);
      ps.setInt(3, id);
      return ps;
    };
    jdbcTemplate.update(preparedStatementCreator);

    return new UserEntity(id, accountId, eMailEntity, password, enabled);
  }

  public void delete(Integer id) {
    PreparedStatementCreator preparedStatementCreator = connection -> {
      String query = "DELETE FROM users WHERE id = ?";
      PreparedStatement ps = connection.prepareStatement(query);
      ps.setInt(1, id);
      return ps;
    };
    jdbcTemplate.update(preparedStatementCreator);
  }
}
