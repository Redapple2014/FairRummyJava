package com.skillengine.rummy.game;

import java.util.Date;
import java.util.TimerTask;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class GameTimer
{
	private Date startTime;
	private Date _endTime;

	public void startAt( Date time )
	{
		if( time == null )
		{
			time = new Date( System.currentTimeMillis() );
		}
		else
		{
			startTime = time;
		}
		long l = time.getTime() - System.currentTimeMillis();
		if( l > 0 )
		{

			TimerTask t1 = new TimerTask()
			{

				public void run()
				{
					try
					{
						startCallback();
					}
					catch( Exception ex )
					{
						log.error( "", ex );
					}
				}
			};

		}
		else
		{
			startCallback();
		}
	}

	public abstract void startCallback();

	public void end( Date endTime )
	{
		if( endTime == null )
		{
			_endTime = new Date( System.currentTimeMillis() );
		}
		else
		{
			_endTime = endTime;
		}
		validate();
	}


	public Date getEndTime()
	{
		validate();
		return _endTime;
	}


	public Date getStartTime()
	{
		validate();
		return startTime;
	}

	public boolean hasEnded()
	{
		validate();
		Date now = new Date( System.currentTimeMillis() );
		if( _endTime != null && ( _endTime.compareTo( now ) <= 0 ) )
		{
			return true;
		}
		return false;
	}

	public boolean hasStarted()
	{
		validate();
		if( startTime != null )
		{
			Date now = new Date( System.currentTimeMillis() );
			if( startTime.compareTo( now ) <= 0 )
			{
				return true;
			}
		}
		return false;
	}

	private void validate()
	{
		boolean isValid = true;
		if( startTime != null )
		{
			if( _endTime != null )
			{
				if( startTime.compareTo( _endTime ) > 0 )
				{
					isValid = false;
				}
			}
		}
		else
		{
			if( _endTime != null )
			{
				isValid = false;
			}
		}

	}
}
