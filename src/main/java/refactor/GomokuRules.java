package refactor;

import java.util.Stack;

public class GomokuRules implements Ruleset {
    Board boardCache;

    @Override
    public String getRuleName() {
        return "Gomoku";
    }

    @Override
    public BoardScanResult scanBoard(Board board) {
        return new BoardScanResult();
    }

    @Override
    public StepResult takeStep(Board board, Position position, Stack<Board> boardHistory) {
        this.boardCache = new Board(board);

        //landing on other pieces prohibited
        if (boardCache.pieceExistAt(position)) return new StepResult(false, false, board, null);

        //update board
        boardCache.addPieceAt(position, boardCache.nextChessType, boardHistory.size() + 1);

        //if 5-connected pieces are formed, return current player as winner
        if (isFiveConnected(position)) return new StepResult(true, true, boardCache, boardCache.nextChessType);

        return new StepResult(true, false, boardCache, null);
    }

    private boolean isAllyPiece(Position position) {
        return boardCache.pieceExistAt(position) && boardCache.nextChessType.equals(boardCache.getChessTypeAt(position));
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
        while (isAllyPiece(tempPosition)) {
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
