package net.cabezudo.sofia.users.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Groups implements Iterable<Group> {
  private final Set<String> set = new TreeSet<>();
  private List<Group> list = new ArrayList<>();
  private Collection<GrantedAuthority> authorities;

  public Groups() {
    // Nothing to do
  }


  public Groups(String values) {
    list = Arrays.stream(values.split(","))
        .filter(Objects::nonNull)
        .filter(Predicate.not(String::isEmpty))
        .map(Group::new)
        .collect(Collectors.toList());
  }

  public Groups(Collection<GrantedAuthority> authorities) {
    for (GrantedAuthority authority : authorities) {
      Group group = new Group(authority.getAuthority());
      add(group);
    }

  }

  public int size() {
    return list.size();
  }

  @NotNull
  @Override
  public Iterator<Group> iterator() {
    return list.iterator();
  }

  @Override
  public void forEach(Consumer<? super Group> action) {
    Iterable.super.forEach(action);
  }

  public boolean add(Group group) {
    boolean isSet = set.add(group.getName());
    if (isSet) {
      list.add(group);
    }
    return isSet;
  }

  public Collection<GrantedAuthority> toAuthorities() {
    if (this.authorities == null) {
      authorities = new ArrayList<>();
      for (Group group : list) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(group.getName());
        authorities.add(grantedAuthority);
      }
    }
    return authorities;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (Group group : list) {
      sb.append(group.getName()).append(", ");
    }
    return sb.toString();
  }

  public boolean contains(String groupName) {
    return set.contains(groupName);
  }

  public boolean isEmpty() {
    return list.isEmpty();
  }
}
