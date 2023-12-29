package org.uet.int3304.gateway.Group;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;

public class GroupManager {
  private static final Object lock = new Object();
  private static GroupManager instance;

  private final Map<String, Map<String, Long>> groups;

  private GroupManager() {
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

    if (group.size() == 0) {
      System.out.printf("Group [%s] has no member left\n", groupName);
      System.out.printf("Pruning group [%s]\n", groupName);
      groups.remove(groupName);
    }
  }

  public synchronized List<String> getGroups() {
    return Arrays.asList(groups.keySet().toArray(new String[0]));
  }

  public List<SimpleEntry<Long, String>> getConnectedInGroup(String groupName) {
    var group = groups.get(groupName);

    if (group == null)
      return Arrays.asList();

    var nodes = new LinkedList<SimpleEntry<Long, String>>();

    for (var entry : group.entrySet())
      nodes.add(new SimpleEntry<>(entry.getValue(), entry.getKey()));

    return nodes;
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
