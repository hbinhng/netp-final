package org.uet.int3304.gateway.UI;

import org.uet.int3304.gateway.AppConfig.Config;

public class NodeState {
	private static final Object lock = new Object();
	private static NodeState instance;

	private long dataInterval;

	public NodeState() {
		dataInterval = Config.getInstance().getDataPollInterval();
	}

	public long getDataInterval() {
		return dataInterval;
	}

	public void setDataInterval(long dataInterval) {
		this.dataInterval = dataInterval;
	}

	public static NodeState getInstance() {
		if (instance == null)
			synchronized (lock) {
				if (instance == null)
					instance = new NodeState();
			}

		return instance;
	}
}
