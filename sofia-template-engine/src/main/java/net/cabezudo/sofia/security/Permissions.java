package net.cabezudo.sofia.security;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Permissions {
  private final Logger logger = LoggerFactory.getLogger(Permissions.class);
  private final Map<String, List<Permission>> permissionsByResource = new TreeMap<>();

  public void add(Permission permission) {
    List<Permission> permissions = permissionsByResource.get(permission.getResource());
    if (permissions == null) {
      permissions = new ArrayList<>();
      permissionsByResource.put(permission.getResource(), permissions);
    }
    if (!permissions.contains(permission)) {
      permissions.add(permission);
    }
  }

  public List<Permission> get(String resource) {
    logger.debug("resource: " + resource);
    List<Permission> permissions = null;
    for (String key : permissionsByResource.keySet()) {
      logger.debug("Try with key: " + key);
      if (key.endsWith("**")) {
        String k = key.substring(0, key.length() - 2);
        if (resource.startsWith(k)) {
          logger.debug("resource start with: " + k);
          return permissionsByResource.get(key);
        }
      } else {
        permissions = permissionsByResource.get(resource);
        if (permissions != null) {
          return permissions;
        }
      }
    }
    return new ArrayList<>();
  }
}
