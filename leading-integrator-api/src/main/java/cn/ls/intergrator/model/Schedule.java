/* 
 * Copyright (c) 2008-2011 by LeadingSoft. 
 * All rights reserved. 
 */
package cn.ls.intergrator.model;

import java.util.List;

/**
 * 任务调度
 * 
 * @author zhoumb 2011-5-18
 */
public interface Schedule {

	String getName();

	String getTitle();

	String getDescription();

	List<Task> getTasks();

	List<Timer> getTimers();

	List<Associate> getAssociate();

}
