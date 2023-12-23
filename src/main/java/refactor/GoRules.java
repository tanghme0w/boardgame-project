package refactor;

import globals.ChessType;

import java.util.List;
import java.util.Stack;

public class GoRules implements Ruleset {
    Board boardCache;   //there will be many recursion functions. cache the board to save memory.
    List<Board> boardHistory;

    @Override
    public String getRuleName() {
        return "Go";
    }

    @Override
    public BoardScanResult scanBoard(Board board) {
        return new BoardScanResult();
    }

    @Override
    public StepResult takeStep(Board board, Position position, Stack<Board> boardHistory) {
        this.boardCache = new Board(board);
        this.boardHistory = boardHistory;

        //landing on existing piece prohibited
        if (boardCache.pieceExistAt(position)) return new StepResult(false, false, board, null);

        //try adding new piece to board
        boardCache.addPieceAt(position, boardCache.nextChessType, boardHistory.size() + 1);

        //remove dead pieces
        for (Position p: position.connectedPositions()) {
            if (boardCache.pieceExistAt(p)
                    && !boardCache.nextChessType.equals(boardCache.getChessTypeAt(p))
                    && libertyCount(boardCache, p) == 0) {
                removePieces(boardCache, p);
            }
        }

        //suicide prohibited
        if (libertyCount(boardCache, position) == 0) {
            Logger.log("Cannot land at " + position.x + "," + position.y + " : suicide prohibited." );
            return new StepResult(false, false, board, null);
        }

        //global repetition prohibited
        for (Board historyBoard: boardHistory) {
            if (historyBoard.equals(boardCache)) {
                Logger.log("Cannot land at " + position.x + "," + position.y + " : global repetition prohibited");
                return new StepResult(false, false, board, null);
            }
        }

        //check for remaining positions
        ChessType opponentChessType = switch(boardCache.nextChessType) {
            case BLACK -> ChessType.WHITE;
            case WHITE -> ChessType.BLACK;
        };
        for (Integer i = 1; i <= boardCache.xSize; i++) {
            for (Integer j = 1; j <= boardCache.ySize; j++) {
                //if there are remaining available positions, return step result and continue the game
                if (isAvailablePosition(new Position(i, j), opponentChessType)) {
                    return new StepResult(true, false, boardCache, null);
                }
            }
        }
        //no available position left, find the winner and end the game
        return new StepResult(true, true, boardCache, scanBoard(boardCache).winner);
    }

    private boolean isAvailablePosition(Position position, ChessType chessType) {
        Board tempBoard = new Board(boardCache);
        tempBoard.nextChessType = chessType;

        //landing on existing piece prohibited
        if (tempBoard.pieceExistAt(position)) return false;

        //try adding new piece to board
        tempBoard.addPieceAt(position, chessType, 0);

        //remove dead pieces
        for (Position p: position.connectedPositions()) {
            if (tempBoard.pieceExistAt(p) && !tempBoard.getChessTypeAt(p).equals(chessType) && libertyCount(tempBoard, p) == 0) {
                removePieces(tempBoard, p);
            }
        }

        //suicide prohibited
        if (libertyCount(tempBoard, position) == 0) {
            return false;
        }

        //global repetition prohibited
        for (Board historyBoard: boardHistory) {
            if (historyBoard.equals(tempBoard)) return false;
        }
        return true;
    }

    private int libertyCount(Board board, Position position) {
        int liberty = 0;
        boolean[][] visited = new boolean[board.xSize + 1][board.ySize + 1];
        visited[position.x][position.y] = true;
        Stack<Position> traverseStack = new Stack<>();
        traverseStack.push(position);
        while(!traverseStack.isEmpty()) {
            Position currentVisitingPosition = traverseStack.pop();
            for(Position p: currentVisitingPosition.connectedPositions()) {
                //if out of bound or is already visited, ignore.
                if (board.outOfBound(p) || visited[p.x][p.y]) continue;
                //set visited flag
                visited[p.x][p.y] = true;
                //for empty space, add liberty count
                if (!board.pieceExistAt(p)) liberty++;
                //for unvisited allies, push to stack
                else if (board.getChessTypeAt(p).equals(board.getChessTypeAt(position))) traverseStack.push(p);
            }
        }
        return liberty;
    }

    private void removePieces(Board board, Position position) {
        Stack<Position> traverseStack = new Stack<>();
        ChessType chessTypeToBeRemoved = board.getChessTypeAt(position);
        traverseStack.push(position);
        while(!traverseStack.isEmpty()) {
            Position currentVisitingPosition = traverseStack.pop();
            for(Position p: currentVisitingPosition.connectedPositions()) {
                if (!board.pieceExistAt(p)) continue;
                //for unvisited allies, push to stack
                if (board.getChessTypeAt(p).equals(chessTypeToBeRemoved)) traverseStack.push(p);
            }
            board.removePieceAt(currentVisitingPosition);
        }
    }
}
