package cn.ls.integrator.components.manager.impl;

import cn.ls.integrator.core.log.message.MessageLog;

public class MessageLogManagerImpl extends AbstractMessageLogManagerImpl<MessageLog>{

	@Override
	public Class<MessageLog> getClazz() {
		return MessageLog.class;
	}

}
