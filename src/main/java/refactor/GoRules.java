package refactor;

import globals.BoardMode;
import globals.ChessType;
import refactor.client.Client;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        //check if there are available moves left
        boolean gameEnd = !hasRemainingPosition(board.nextChessType);
        ChessType winningChessType = findWinner(board);
        return new BoardScanResult(gameEnd, winningChessType);
    }

    private ChessType findWinner(Board board) {
        boolean[][] visited = new boolean[board.xSize + 1][board.ySize + 1];
        Map<ChessType, Integer> scores = new HashMap<>();
        //initialize scoreboard
        for (ChessType chessType: ChessType.values()) {
            scores.put(chessType, 0);
        }
        //count all scores
        for (int i = 1; i <= board.xSize; i++) {
            for (int j = 1; j <= board.ySize; j++) {
                if (visited[i][j]) continue;
                visited[i][j] = true;
                Position currentPosition = new Position(i, j);
                Stack<Position> positions = new Stack<>();
                positions.push(currentPosition);
                //if is black, search for all adjacent black pieces and add black point, do the same for white
                if (board.pieceExistAt(currentPosition)) {
                    ChessType currentChessType = board.getChessTypeAt(currentPosition);
                    while (!positions.isEmpty()) {
                        Position position = positions.pop();
                        for (Position p: position.connectedPositions()) {
                            if (board.outOfBound(p) || visited[p.x][p.y]) continue;
                            if (currentChessType.equals(board.getChessTypeAt(p))) {
                                visited[p.x][p.y] = true;
                                positions.push(p);
                                scores.put(currentChessType, scores.get(currentChessType) + 1);
                            }
                        }
                    }
                } else {
                    ChessType surroundingChessType = null;
                    Integer count = 0;
                    boolean isExclusive = true;
                    while (!positions.isEmpty()) {
                        Position position = positions.pop();
                        for (Position p: position.connectedPositions()) {
                            if (board.outOfBound(p)) continue;
                            if (board.pieceExistAt(p)) {
                                if (surroundingChessType == null) {
                                    surroundingChessType = board.getChessTypeAt(p);
                                } else if (!surroundingChessType.equals(board.getChessTypeAt(p))) {
                                    isExclusive = false;
                                    count = 0;
                                }
                            } else {
                                if(visited[p.x][p.y]) continue;
                                visited[p.x][p.y] = true;
                                positions.push(p);
                                if (isExclusive) count++;
                            }
                        }
                    }
                    if (surroundingChessType != null) scores.put(surroundingChessType, scores.get(surroundingChessType) + count);
                }
            }
        }
        return scores.get(ChessType.BLACK) > scores.get(ChessType.WHITE) ? ChessType.BLACK : ChessType.WHITE;
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
        if (hasRemainingPosition(opponentChessType)) return new StepResult(true, false, boardCache, null);
        //no available position left, find the winner and end the game
        else return new StepResult(true, true, boardCache, scanBoard(boardCache).winner);
    }

    private boolean hasRemainingPosition(ChessType chessType) {
        for (Integer i = 1; i <= boardCache.xSize; i++) {
            for (Integer j = 1; j <= boardCache.ySize; j++) {
                //if there are remaining available positions, return step result and continue the game
                if (isAvailablePosition(new Position(i, j), chessType)) {
                    return true;
                }
            }
        }
        //no available position left, find the winner and end the game
        return false;
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

    public void removePieces(Board board, Position position) {
        if (!board.pieceExistAt(position)) return;
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
