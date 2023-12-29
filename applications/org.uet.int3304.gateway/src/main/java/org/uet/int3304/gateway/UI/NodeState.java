package org.uet.int3304.gateway.UI;

import java.util.List;

public class NodeState {
	private static final Object lock = new Object();
	private static  NodeState instance;
	private NodeList nodeList;

	public NodeState() {
		nodeList = new NodeList(null);
	}

	public void registerNode(Node node) {
		nodeList.addNode(node);
	}

	public static NodeState getInstance() {
		if (instance == null)
			synchronized (lock) {
				if (instance == null)
					instance = new NodeState();
			}

		return instance;
	}

	public class NodeList {
		private List<Node> nodes;

		public NodeList(List<Node> nodes) {
			this.nodes = nodes;
		}

		public List<Node> getNodes() {
			return nodes;
		}

		public void setNodes(List<Node> nodes) {
			this.nodes = nodes;
		}

		public void addNode(Node node) {
			nodes.add(node);
		}

		public void removeNode(Node node) {
			nodes.remove(node);
		}

		public String toString() {
			String result = "";
			for (Node node : nodes) {
				result += node.getName() + ": " + node.getStatus() + "\n";
			}
			return result;
		}
	}

	public class Node {
		private String name;
		private String status;

		public Node(String name, String status) {
			this.name = name;
			this.status = status;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}
	}
}
