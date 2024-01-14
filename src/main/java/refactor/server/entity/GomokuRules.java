package refactor.server.entity;

import refactor.server.dto.BoardScanResult;
import refactor.server.dto.StepResult;

import java.util.Stack;

public class GomokuRules implements Ruleset {
    Board boardCache;

    @Override
    public String getRuleName() {
        return "Gomoku";
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

    enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT,
        UPPER_LEFT,
        UPPER_RIGHT,
        LOWER_LEFT,
        LOWER_RIGHT
    }

    private int countTowards(Position p, Direction d) {
        Position tempPosition = new Position(p.x, p.y);
        int count = 0;
        while (boardCache.getStoneColorAt(p).equals(boardCache.getStoneColorAt(tempPosition))) {
            count++;
            tempPosition = switch (d) {
                case UP -> tempPosition.up();
                case DOWN -> tempPosition.down();
                case LEFT -> tempPosition.left();
                case RIGHT -> tempPosition.right();
                case UPPER_LEFT -> tempPosition.upperLeft();
                case UPPER_RIGHT -> tempPosition.upperRight();
                case LOWER_LEFT -> tempPosition.lowerLeft();
                case LOWER_RIGHT -> tempPosition.lowerRight();
            };
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
