package com.skillengine.message.queue;

import java.io.Closeable;

public interface Publisher extends Closeable
{
	public boolean publishMessage( String message );
}
