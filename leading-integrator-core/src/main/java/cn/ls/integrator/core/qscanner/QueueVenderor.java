package cn.ls.integrator.core.qscanner;

import java.util.List;

public interface QueueVenderor<T extends Queue> {

	List<T> getRecieveQueueList();
	
	QueueScanner createQueueScanner(T queue);
	
	QueueState getQueueState();
}
