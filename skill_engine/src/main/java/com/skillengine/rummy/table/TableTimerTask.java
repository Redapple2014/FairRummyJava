package com.skillengine.rummy.table;

import java.util.TimerTask;

public abstract class TableTimerTask extends TimerTask
{
	private int taskType;

	public TableTimerTask( int taskType )
	{
		this.taskType = taskType;
	}

	public int getTaskType()
	{
		return taskType;
	}

}
