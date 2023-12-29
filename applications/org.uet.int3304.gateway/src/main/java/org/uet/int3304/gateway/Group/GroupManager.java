package org.uet.int3304.gateway.Group;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupManager {
  private static final Object lock = new Object();
  private static GroupManager instance;

  private final Map<String, Map<String, Long>> groups;

  public GroupManager() {
    groups = new HashMap<>();
  }

  public synchronized int registerNode(String groupName, String bucketId, long connectionId) {
    var group = groups.get(groupName);

    if (group == null) {
      group = new HashMap<>();
      group.put(bucketId, connectionId);
      groups.put(groupName, group);

      return 2;
    }

    if (!group.containsKey(bucketId)) {
      group.put(bucketId, connectionId);

      return 1;
    }

    return 0;
  }

  public synchronized void unregisterNode(String groupName, String bucketId) {
    var group = groups.get(groupName);

    if (group == null)
      return;

    if (!group.containsKey(bucketId))
      return;

    group.remove(bucketId);

    if (group.size() == 0)
      groups.remove(groupName);
  }

  public synchronized List<String> getGroups() {
    return Arrays.asList(groups.keySet().toArray(new String[0]));
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