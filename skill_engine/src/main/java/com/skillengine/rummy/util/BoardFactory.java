/**
 * 
 */
package com.skillengine.rummy.util;

import com.skillengine.common.GameTemplates;
import com.skillengine.rummy.globals.VariantTypes;
import com.skillengine.rummy.table.Board;
import com.skillengine.rummy.table.DealsRummyBoard;
import com.skillengine.rummy.table.RummyBoard;

/**
 * 
 */
public class BoardFactory
{
	public static Board createBoard( long tableId, GameTemplates gameTemplates )
	{
		switch( gameTemplates.getVariantType() )
		{
		case VariantTypes.POINTS_RUMMY:
			return new RummyBoard( tableId, gameTemplates );
		case VariantTypes.DEALS_RUMMY:
			return new DealsRummyBoard( tableId, gameTemplates );
		default:
			return null;
		}
	}

	public static Board getBoard( long tableId, int variantType )
	{
		switch( variantType )
		{
		case VariantTypes.POINTS_RUMMY:
			return ActiveBoards.getTable( tableId );
		case VariantTypes.DEALS_RUMMY:
			return ( DealsRummyBoard ) ActiveBoards.getTable( tableId );
		default:
			return null;
		}

	}
}
