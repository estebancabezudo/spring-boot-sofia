package net.cabezudo.sofia.users.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Repository
public class UserRepository {
  private final Logger log = LoggerFactory.getLogger(UserRepository.class);

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Transactional
  public UserEntity findByEmail(int siteId, String email) {
    log.debug("Search user for " + siteId + " and " + email);
    
    List<Map<String, Object>> list = new ArrayList<>();
    jdbcTemplate.queryForList(
        "SELECT u.id AS id, site_id, email AS username, password, enabled, authority " +
            "FROM users AS u, emails AS e, authorities AS a " +
            "WHERE u.email_id = e.id AND u.id = a.user_id AND site_id = ? AND e.email = ?",
        new Object[]{siteId, email}).forEach(rs -> {
      list.add(rs);
    });

    if (list.size() == 0) {
      return null;
    }

    Map<String, Object> firstRecord = list.get(0);
    UserEntity userEntity = new UserEntity((int) firstRecord.get("id"), (Integer) firstRecord.get("site_id"), (String) firstRecord.get("username"), (String) firstRecord.get("password"), (boolean) firstRecord.get("enabled"));
    for (Map<String, Object> rs : list) {
      userEntity.add(new GroupEntity((String) rs.get("authority")));
    }
    return userEntity;
  }

  @Transactional
  public UserEntityList findAll(int siteId, int accountId) {
    Map<Integer, UserEntity> map = new TreeMap<>();
    UserEntityList list = new UserEntityList();

    log.debug("Search users for " + siteId);
    jdbcTemplate.queryForList(
        "SELECT u.id AS id, u.site_id, email AS username, password, enabled, authority " +
            "FROM users AS u, emails AS e, authorities AS a, accounts AS c " +
            "WHERE u.email_id = e.id AND u.id = a.user_id AND c.site_id = u.site_id AND u.site_id = ? AND c.id = ?",
        new Object[]{siteId, accountId}).stream().forEach(rs -> {
      int id = (Integer) rs.get("id");
      UserEntity userEntityFromMap = map.get(id);
      UserEntity newUserEntity;
      if (userEntityFromMap == null) {
        newUserEntity = new UserEntity((int) rs.get("id"), (Integer) rs.get("site_id"), (String) rs.get("username"), (String) rs.get("password"), (boolean) rs.get("enabled"));
        map.put(id, newUserEntity);
        newUserEntity.add(new GroupEntity((String) rs.get("authority")));
      } else {
        userEntityFromMap.add(new GroupEntity((String) rs.get("authority")));
      }
    });
    list.setTotal(list.size()); // The total is the same as the size because there isn't pagination.
    return list;
  }

  public UserEntity get(int id) {
    log.debug("Search user with id " + id);

    List<Map<String, Object>> list = new ArrayList<>();
    jdbcTemplate.queryForList(
        "SELECT u.id AS id, u.site_id, email AS username, password, enabled, authority " +
            "FROM users AS u, emails AS e, authorities AS a " +
            "WHERE u.email_id = e.id AND u.id = ?",
        new Object[]{id}).forEach(rs -> {
      list.add(rs);
    });

    if (list.size() == 0) {
      return null;
    }

    Map<String, Object> firstRecord = list.get(0);
    UserEntity userEntity = new UserEntity((int) firstRecord.get("id"), (Integer) firstRecord.get("site_id"), (String) firstRecord.get("username"), (String) firstRecord.get("password"), (boolean) firstRecord.get("enabled"));
    for (Map<String, Object> rs : list) {
      userEntity.add(new GroupEntity((String) rs.get("authority")));
    }
    return userEntity;
  }
}
