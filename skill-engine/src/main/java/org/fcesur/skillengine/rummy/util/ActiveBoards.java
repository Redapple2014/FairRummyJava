package org.fcesur.skillengine.rummy.util;

import org.fcesur.skillengine.dto.BoardInfo;
import org.fcesur.skillengine.rummy.table.Board;
import dev.mccue.josql.Query;
import dev.mccue.josql.QueryExecutionException;
import dev.mccue.josql.QueryParseException;
import dev.mccue.josql.QueryResults;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ActiveBoards {
    private static Map<Long, Board> runningBoards = new ConcurrentHashMap<>();

    private static Map<Integer, List<BoardInfo>> inProgressBoards = new ConcurrentHashMap<>();

    public static boolean addTable(Long boardId, Board board) {
        Board boardDetails = runningBoards.get(boardId);
        if (boardDetails == null) {

            return runningBoards.put(boardId, board) != null;
        }
        return false;
    }

    public static Board getTable(long boardId) {
        return runningBoards.get(boardId);
    }

    public static Set<Long> activeTables() {
        return runningBoards.keySet();
    }

    public static int getSize() {

        return runningBoards.size();
    }

    public static Collection<Board> getAllActiveTable() {
        return runningBoards.values();
    }

    public static void removeTable(long boardId) {
        runningBoards.remove(boardId);
    }

    public static void addTable(int templateId, long tableId, long gameStartTime) {
        BoardInfo boardInfo = new BoardInfo(tableId, gameStartTime);
        List<BoardInfo> boardInfos = null;

        if (inProgressBoards.get(templateId) != null) {
            List<BoardInfo> infos = inProgressBoards.get(templateId);
            infos.add(boardInfo);
            inProgressBoards.put(templateId, infos);
        } else {
            boardInfos = new ArrayList<>();
            boardInfos.add(boardInfo);
            inProgressBoards.put(templateId, boardInfos);
        }
    }

    public static long getTable(int templateId) {
        List<BoardInfo> boardInfos = inProgressBoards.getOrDefault(templateId, Collections.emptyList());
        System.out.println("inProgressBoards" + inProgressBoards);
        if (boardInfos.isEmpty()) {
            return -1l;
        }
        return getTableId(templateId);

    }

    private static long getTableId(int templateId) {
        try {
            List<BoardInfo> boardInfos = inProgressBoards.getOrDefault(templateId, Collections.emptyList());
            Query query = new Query();
            query.parse("select * from com.skillengine.dto.BoardInfo order by gameStartTime desc");
            QueryResults results = query.execute(boardInfos);
            List<BoardInfo> infos = results.getResults();
            return infos.getFirst().getTableId();
        } catch (QueryParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        } catch (QueryExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        }

    }

    public static boolean removeTable(int templateId, long tableId) {
        List<BoardInfo> runningBoardInfo = inProgressBoards.get(templateId);
        if (runningBoardInfo.isEmpty()) {
            return false;
        }
        return runningBoardInfo.removeIf(boardInfo -> boardInfo.getTableId() == tableId);
    }
}
