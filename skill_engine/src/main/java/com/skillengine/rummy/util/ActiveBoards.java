package com.skillengine.rummy.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.skillengine.rummy.table.Board;

public class ActiveBoards {
	private static  Map< Long, Board > runningBoards = new ConcurrentHashMap< >();

	public static boolean addTable( Long boardId, Board board )
	{
		Board boardDetails = runningBoards.get( boardId );
		if( boardDetails == null )
		{
			
			return runningBoards.put( boardId, board ) != null;
		}
			return false;
	}

	public static Board getTable( long boardId )
	{
		return runningBoards.get(boardId);
	}

	public static Set< Long > activeTables()
	{
		return runningBoards.keySet();
	}

	public static int getSize()
	{
		
		return runningBoards.size();
	}

	public static Collection< Board > getAllActiveTable( )
	{
	  return runningBoards.values();
	}

	public static void removeTable(long boardId) {
		runningBoards.remove(boardId);
	}
}
