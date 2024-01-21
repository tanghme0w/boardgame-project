package refactor.server.entity;

import globals.StoneColor;
import refactor.server.dto.BoardScanResult;
import refactor.server.dto.StepResult;

import java.util.*;

public class GoRules implements Ruleset {
    Board boardCache;   //there will be many recursion functions. cache the board to save memory.
    List<Board> boardHistory = new ArrayList<>();

    @Override
    public String getRuleName() {
        return "go";
    }

    @Override
    public List<Action> evaluateActions(Board board) {
        boardCache = new Board(board);
        List<Action> remainingPositions = new ArrayList<>();
        for (Integer i = 1; i <= boardCache.xSize; i++) {
            for (Integer j = 1; j <= boardCache.ySize; j++) {
                //if there are remaining available positions, return step result and continue the game
                Position p = (new Position(i, j));
                if (isAvailablePosition(p, boardCache.actingStoneColor)) {
                    boardCache.addPieceAt(p, boardCache.actingStoneColor, 0);
                    double score = 0.;
                    score = 0.5 * (getScore(boardCache) - getScore(board));
                    if (Objects.equals(board.actingStoneColor, StoneColor.WHITE)) score *= -1;
                    score += countTotalLiberty(boardCache, boardCache.actingStoneColor) - countTotalLiberty(board, board.actingStoneColor);
                    StoneColor opponentStoneColor = switch (boardCache.actingStoneColor) {
                        case BLACK -> StoneColor.WHITE;
                        case WHITE -> StoneColor.BLACK;
                    };
                    score += countTotalLiberty(board, opponentStoneColor) - countTotalLiberty(boardCache, opponentStoneColor);
                    remainingPositions.add(new Action(p, score));
                    boardCache = new Board(board);
                }
            }
        }
        return remainingPositions;
    }

    @Override
    public void init(Board board) {
        return;
    }

    public double countTotalLiberty(Board board, StoneColor stoneColor) {
        int liberty = 0;
        boolean[][] visited = new boolean[board.xSize + 1][board.ySize + 1];
        for (Integer i = 1; i <= boardCache.xSize; i++) {
            for (Integer j = 1; j <= boardCache.ySize; j++) {
                Position position = (new Position(i, j));
                if (visited[position.x][position.y] || !board.pieceExistAt(position) || !Objects.equals(board.getStoneColorAt(position), stoneColor)) continue;
                else {
                    liberty += getLiberty(board, 0, visited, position);
                }
            }
        }
        return liberty;
    }

    private int getLiberty(Board board, int liberty, boolean[][] visited, Position position) {
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
                else if (board.getStoneColorAt(p).equals(board.getStoneColorAt(position))) traverseStack.push(p);
            }
        }
        return liberty;
    }

    @Override
    public BoardScanResult scanBoard(Board board) {
        //check if there are available moves left
        boolean gameEnd = true;
        for (StoneColor stoneColor: StoneColor.values()) {
            if (!getRemainingPosition(stoneColor).isEmpty()) {
                gameEnd = false;
                break;
            }
        }
        StoneColor winningStoneColor = getScore(board) > 0 ? StoneColor.BLACK : StoneColor.WHITE;
        return new BoardScanResult(true, gameEnd, winningStoneColor);
    }

    private double getScore(Board board) {
        boolean[][] visited = new boolean[board.xSize + 1][board.ySize + 1];
        Map<StoneColor, Integer> scores = new HashMap<>();
        //initialize scoreboard
        for (StoneColor stoneColor : StoneColor.values()) {
            scores.put(stoneColor, 0);
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
                    StoneColor currentStoneColor = board.getStoneColorAt(currentPosition);
                    while (!positions.isEmpty()) {
                        Position position = positions.pop();
                        for (Position p: position.connectedPositions()) {
                            if (board.outOfBound(p) || visited[p.x][p.y]) continue;
                            if (currentStoneColor.equals(board.getStoneColorAt(p))) {
                                visited[p.x][p.y] = true;
                                positions.push(p);
                                scores.put(currentStoneColor, scores.get(currentStoneColor) + 1);
                            }
                        }
                    }
                } else {
                    StoneColor surroundingStoneColor = null;
                    Integer count = 0;
                    boolean isExclusive = true;
                    while (!positions.isEmpty()) {
                        Position position = positions.pop();
                        for (Position p: position.connectedPositions()) {
                            if (board.outOfBound(p)) continue;
                            if (board.pieceExistAt(p)) {
                                if (surroundingStoneColor == null) {
                                    surroundingStoneColor = board.getStoneColorAt(p);
                                } else if (!surroundingStoneColor.equals(board.getStoneColorAt(p))) {
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
                    if (surroundingStoneColor != null) scores.put(surroundingStoneColor, scores.get(surroundingStoneColor) + count);
                }
            }
        }
        return scores.get(StoneColor.BLACK) - scores.get(StoneColor.WHITE);
    }

    @Override
    public StepResult takeStep(Board board, Position position, Stack<Board> boardHistory) {
        this.boardCache = new Board(board);
        this.boardHistory = boardHistory;

        //landing on existing piece prohibited
        if (boardCache.pieceExistAt(position)) return new StepResult(false, false, board, null);

        //try adding new piece to board
        boardCache.addPieceAt(position, boardCache.actingStoneColor, boardHistory.size() + 1);

        //remove dead pieces
        for (Position p: position.connectedPositions()) {
            if (boardCache.pieceExistAt(p)
                    && !boardCache.actingStoneColor.equals(boardCache.getStoneColorAt(p))
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
        StoneColor opponentStoneColor = switch(boardCache.actingStoneColor) {
            case BLACK -> StoneColor.WHITE;
            case WHITE -> StoneColor.BLACK;
        };
        if (!getRemainingPosition(opponentStoneColor).isEmpty()) return new StepResult(true, false, boardCache, null);
        //no available position left, find the winner and end the game
        else return new StepResult(true, true, boardCache, scanBoard(boardCache).winner);
    }

    private List<Position> getRemainingPosition(StoneColor stoneColor) {
        List<Position> remainingPositions = new ArrayList<>();
        for (Integer i = 1; i <= boardCache.xSize; i++) {
            for (Integer j = 1; j <= boardCache.ySize; j++) {
                //if there are remaining available positions, return step result and continue the game
                Position p = (new Position(i, j));
                if (isAvailablePosition(p, stoneColor)) {
                    remainingPositions.add(p);
                }
            }
        }
        return remainingPositions;
    }

    private boolean isAvailablePosition(Position position, StoneColor stoneColor) {
        Board tempBoard = new Board(boardCache);
        tempBoard.actingStoneColor = stoneColor;

        //landing on existing piece prohibited
        if (tempBoard.pieceExistAt(position)) return false;

        //try adding new piece to board
        tempBoard.addPieceAt(position, stoneColor, 0);

        //remove dead pieces
        for (Position p: position.connectedPositions()) {
            if (tempBoard.pieceExistAt(p) && !tempBoard.getStoneColorAt(p).equals(stoneColor) && libertyCount(tempBoard, p) == 0) {
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
        liberty = getLiberty(board, liberty, visited, position);
        return liberty;
    }

    public void removePieces(Board board, Position position) {
        if (!board.pieceExistAt(position)) return;
        Stack<Position> traverseStack = new Stack<>();
        StoneColor stoneColorToBeRemoved = board.getStoneColorAt(position);
        traverseStack.push(position);
        while(!traverseStack.isEmpty()) {
            Position currentVisitingPosition = traverseStack.pop();
            for(Position p: currentVisitingPosition.connectedPositions()) {
                if (!board.pieceExistAt(p)) continue;
                //for unvisited allies, push to stack
                if (board.getStoneColorAt(p).equals(stoneColorToBeRemoved)) traverseStack.push(p);
            }
            board.removePieceAt(currentVisitingPosition);
        }
    }
}
