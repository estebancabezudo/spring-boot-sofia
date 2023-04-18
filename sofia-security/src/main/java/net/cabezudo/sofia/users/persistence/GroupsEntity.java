package net.cabezudo.sofia.users.persistence;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GroupsEntity implements Iterable<GroupEntity> {
  private final List<GroupEntity> groupEntities = new ArrayList<>();

  public void add(GroupEntity groupEntity) {
    groupEntities.add(groupEntity);
  }

  public int size() {
    return groupEntities.size();
  }

  @NotNull
  @Override
  public Iterator<GroupEntity> iterator() {
    return groupEntities.iterator();
  }
}
