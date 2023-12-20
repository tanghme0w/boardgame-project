package refactor;

import globals.ChessType;

import java.util.Stack;

public class GoRules implements Ruleset {
    Board boardCache;   //there will be many recursion functions. cache the board to save memory.

    @Override
    public BoardScanResult scanBoard(Board board) {
        return null;
    }

    @Override
    public StepResult takeStep(Board board, Position position, Integer stepCount, Stack<Board> boardHistory) {
        boardCache = board;

        //landing on existing piece prohibited
        if (boardCache.pieceExistAt(position)) return new StepResult(false, false, board, null);

        //try adding new piece to board
        boardCache.addPieceAt(position, boardCache.nextChessType, stepCount);

        //remove dead pieces
        for (Position p: position.connectedPositions()) {
            if (isOpponentPiece(p) && libertyCount(p) == 1) {
                removePieces(p);
            }
        }

        //suicide prohibited
        if (libertyCount(position) == 0) {
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
        for (Integer i = 1; i <= boardCache.xSize; i++) {
            for (Integer j = 1; j <= boardCache.ySize; j++) {
                //if there are remaining available positions, return step result and continue the game
                if (libertyCount(new Position(i, j)) > 0) return new StepResult(true, false, boardCache, null);
            }
        }

        //no available position left, find the winner and end the game
        return new StepResult(true, true, boardCache, scanBoard(boardCache).winner);
    }

    private boolean isAllyPiece(Position position) {
        return boardCache.pieceExistAt(position) && boardCache.nextChessType.equals(boardCache.getChessTypeAt(position));
    }

    private boolean isOpponentPiece(Position position) {
        return boardCache.pieceExistAt(position) && boardCache.nextChessType.equals(boardCache.getChessTypeAt(position));
    }

    private int libertyCount(Position position) {
        int liberty = 0;
        boolean[][] visited = new boolean[boardCache.xSize + 1][boardCache.ySize + 1];
        visited[position.x][position.y] = true;
        Stack<Position> traverseStack = new Stack<>();
        traverseStack.push(position);
        while(!traverseStack.isEmpty()) {
            Position currentVisitingPosition = traverseStack.pop();
            for(Position p: currentVisitingPosition.connectedPositions()) {
                //if out of bound or is already visited, ignore.
                if (boardCache.outOfBound(p) || visited[p.x][p.y]) continue;
                //set visited flag
                visited[p.x][p.y] = true;
                //for empty space, add liberty count
                if (!boardCache.pieceExistAt(p)) liberty++;
                //for unvisited allies, push to stack
                else if (isAllyPiece(p)) traverseStack.push(p);
            }
        }
        return liberty;
    }

    private void removePieces(Position position) {
        Stack<Position> traverseStack = new Stack<>();
        traverseStack.push(position);
        while(!traverseStack.isEmpty()) {
            Position currentVisitingPosition = traverseStack.pop();
            for(Position p: currentVisitingPosition.connectedPositions()) {
                if (boardCache.outOfBound(p)) continue;
                //for unvisited allies, push to stack
                if (isAllyPiece(p)) traverseStack.push(p);
            }
            boardCache.removePieceAt(currentVisitingPosition);
        }
    }
}
