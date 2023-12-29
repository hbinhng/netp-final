package org.uet.int3304.gateway.Group;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GroupManager {
  private static final Object lock = new Object();
  private static GroupManager instance;

  private final Map<String, Set<String>> groups;

  public GroupManager() {
    groups = new HashMap<>();
  }

  public synchronized int registerNode(String groupName, String bucketId) {
    var group = groups.get(groupName);

    if (group == null) {
      group = new HashSet<>();
      group.add(bucketId);
      groups.put(groupName, group);

      return 2;
    }

    if (!group.contains(bucketId)) {
      group.add(bucketId);

      return 1;
    }

    return 0;
  }

  public synchronized void unregisterNode(String groupName, String bucketId) {
    var group = groups.get(groupName);

    if (group == null)
      return;

    if (!group.contains(bucketId))
      return;

    group.remove(bucketId);

    if (group.size() == 0)
      groups.remove(groupName);
  }

  public static GroupManager getInstance() {
    if (instance == null)
      synchronized (lock) {
        if (instance == null)
          instance = new GroupManager();
      }

    return instance;
  }
}
