package refactor.server.entity;

import globals.Direction;
import globals.StoneColor;
import refactor.server.dto.BoardScanResult;
import refactor.server.dto.StepResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

public class GomokuRules implements Ruleset {
    Board boardCache;

    @Override
    public String getRuleName() {
        return "gomoku";
    }

    @Override
    public List<Action> evaluateActions(Board board) {
        List<Action> actions = new ArrayList<>();
        boardCache = new Board(board);
        //any empty cell would be available
        for (int i = 1; i <= board.xSize; i++) {
            for (int j = 1; j <= board.ySize; j++) {
                Position currentPosition = new Position(i, j);
                if (!boardCache.pieceExistAt(currentPosition)) {
                    boardCache.addPieceAt(currentPosition, boardCache.actingStoneColor, 0);
                    //calculate action score
                    double score = 0;
                    score += getScore(currentPosition, 2);
                    boardCache.pieceArray[i][j].stoneColor = switch (boardCache.actingStoneColor) {
                        case BLACK -> StoneColor.WHITE;
                        case WHITE -> StoneColor.BLACK;
                    };
                    score += 1.5 * getScore(currentPosition, 2);
                    boardCache.pieceArray[i][j].stoneColor = null;
                    score += 0.1 * getScore(currentPosition, 1);
                    actions.add(new Action(currentPosition, score));
                }
            }
        }
        return actions;
    }

    private double getScore(Position currentPosition, Integer power) {
        double score = 0;
        score += Math.pow(countTowards(currentPosition, Direction.UP) + countTowards(currentPosition, Direction.DOWN) - 2, power);
        score += Math.pow(countTowards(currentPosition, Direction.LEFT) + countTowards(currentPosition, Direction.RIGHT) - 2, power);
        score += Math.pow(countTowards(currentPosition, Direction.UPPER_LEFT) + countTowards(currentPosition, Direction.LOWER_RIGHT) - 2, power);
        score += Math.pow(countTowards(currentPosition, Direction.UPPER_RIGHT) + countTowards(currentPosition, Direction.LOWER_LEFT) - 2, power);
        return score;
    }

    @Override
    public void init(Board board) {
        return;
    }

    @Override
    public BoardScanResult scanBoard(Board board) {
        boardCache = board;
        //scan the whole board for winning pieces
        for (int i = 1; i <= board.xSize; i++) {
            for (int j = 1; j <= board.ySize; j++) {
                if(isFiveConnected(new Position(i, j))) return new BoardScanResult(true, true, board.getStoneColorAt(new Position(i, j)));
            }
        }
        return new BoardScanResult(true, true, null);
    }

    @Override
    public StepResult takeStep(Board board, Position position, Stack<Board> boardHistory) {
        this.boardCache = new Board(board);

        //landing on other pieces prohibited
        if (boardCache.pieceExistAt(position)) return new StepResult(false, false, board, null);

        //update board
        boardCache.addPieceAt(position, boardCache.actingStoneColor, boardHistory.size() + 1);

        //if 5-connected pieces are formed, return current player as winner
        if (isFiveConnected(position)) return new StepResult(true, true, boardCache, boardCache.actingStoneColor);

        return new StepResult(true, false, boardCache, null);
    }

    private int countTowards(Position p, Direction d) {
        Position tempPosition = new Position(p.x, p.y);
        int count = 0;
        while (!boardCache.outOfBound(tempPosition) && Objects.equals(boardCache.getStoneColorAt(p), (boardCache.getStoneColorAt(tempPosition)))) {
            count++;
            tempPosition = tempPosition.nextPosition(d);
        }
        return count;
    }

    private boolean isFiveConnected(Position position) {
        if(!boardCache.pieceExistAt(position)) return false;
        //scan vertical
        if (countTowards(position, Direction.UP) + countTowards(position, Direction.DOWN) - 1 >= 5) return true;
        //scan horizontal
        else if (countTowards(position, Direction.LEFT) + countTowards(position, Direction.RIGHT) - 1 >= 5) return true;
        //scan primary diagonal
        else if (countTowards(position, Direction.UPPER_LEFT) + countTowards(position, Direction.LOWER_RIGHT) - 1 >= 5) return true;
        //scan auxiliary diagonal
        else return countTowards(position, Direction.UPPER_RIGHT) + countTowards(position, Direction.LOWER_LEFT) - 1 >= 5;
    }
}
