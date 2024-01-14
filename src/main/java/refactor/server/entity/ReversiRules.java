package refactor.server.entity;

import globals.Direction;
import globals.StoneColor;
import refactor.server.dto.BoardScanResult;
import refactor.server.dto.StepResult;

import java.util.*;

public class ReversiRules implements Ruleset {
    Board boardCache;
    Map<StoneColor, Integer> pieceCount;
    Map<StoneColor, Boolean> stepAvailable;

    public ReversiRules() {
        pieceCount = new HashMap<>();
        pieceCount.put(StoneColor.BLACK, 0);
        pieceCount.put(StoneColor.WHITE, 0);
        stepAvailable = new HashMap<>();
        stepAvailable.put(StoneColor.WHITE, true);
        stepAvailable.put(StoneColor.BLACK, true);
    }

    @Override
    public String getRuleName() {
        return "reversi";
    }

    @Override
    public BoardScanResult scanBoard(Board board) {
        // check board legitimacy & count number of pieces
        this.pieceCount.put(StoneColor.BLACK, 0);
        this.pieceCount.put(StoneColor.WHITE, 0);
        // check for empty cells in the middle four spaces
        Position centerPos = new Position(board.xSize / 2, board.ySize / 2);
        if (
                !board.pieceExistAt(centerPos) ||
                !board.pieceExistAt(centerPos.right()) ||
                !board.pieceExistAt(centerPos.down()) ||
                !board.pieceExistAt(centerPos.lowerRight())
        ) return new BoardScanResult(false, false, null);

        // check for isolated pieces, count black & white pieces, store empty cells for further analysis
        List<Position> emptyCells = new ArrayList<>();
        for (int i = 1; i <= board.xSize; i++) {
            for (int j = 1; j <= board.ySize; j++) {
                Position currentPosition = new Position(i, j);
                // if the current cell is empty
                if (board.pieceExistAt(currentPosition)) {
                    // check if it is isolated on any direction
                    boolean isolated = true;
                    for (Direction d: Direction.values()) {
                        if (board.pieceExistAt(currentPosition.nextPosition(d))) {
                            isolated = false;
                            break;
                        }
                    }
                    // if an isolated piece is detected, claim that the board is not legit
                    if (isolated) return new BoardScanResult(false, false, null);
                    // if the piece is not isolated, add to pieces count
                    switch (board.getStoneColorAt(new Position(i, j))) {
                        case BLACK: this.pieceCount.put(StoneColor.BLACK, this.pieceCount.get(StoneColor.BLACK) + 1); break;
                        case WHITE: this.pieceCount.put(StoneColor.WHITE, this.pieceCount.get(StoneColor.WHITE) + 1); break;
                    }
                }
                // if the current cell is not empty, record the position
                else emptyCells.add(new Position(i, j));
            }
        }

        // check if game has ended (no available position for all players)
        for (StoneColor thisColor: StoneColor.values()) {
            StoneColor opponentStoneColor = switch (thisColor) {
                case BLACK -> StoneColor.WHITE;
                case WHITE -> StoneColor.BLACK;
            };
            stepAvailable.put(thisColor, false);
            // for every empty cell on board
            for (Position p : emptyCells) {
                // assume current acting side lands here, search in eight directions for occupied pieces
                int totalOccupyCount = 0;
                for (Direction d : Direction.values()) {
                    Position tp = p.nextPosition(d);
                    int localOccupyCount = 0;
                    while (board.getStoneColorAt(tp) == opponentStoneColor) {
                        localOccupyCount++;
                        tp = tp.nextPosition(d);
                    }
                    // can occupy only when reaching ally piece
                    if (board.getStoneColorAt(tp) == thisColor) {
                        totalOccupyCount += localOccupyCount;
                    }
                }
                if (totalOccupyCount > 0) {
                    stepAvailable.put(thisColor, true);
                    break;
                }
            }
        }
        // there are available steps left for at least one player.
        if (stepAvailable.get(StoneColor.WHITE) || stepAvailable.get(StoneColor.BLACK)) return new BoardScanResult(true, false, null);
        // no available spaces left. check winner.
        return new BoardScanResult(true, true, determineWinner());
    }

    private StoneColor determineWinner() {
        if (Objects.equals(pieceCount.get(StoneColor.BLACK), pieceCount.get(StoneColor.WHITE))) return null;
        else return pieceCount.get(StoneColor.BLACK) > pieceCount.get(StoneColor.WHITE) ? StoneColor.BLACK : StoneColor.WHITE;
    }

    @Override
    public void init(Board board) {
        Position centerPos = new Position(board.xSize / 2, board.ySize / 2);
        board.addPieceAt(centerPos, StoneColor.WHITE, 0);
        board.addPieceAt(centerPos.right(), StoneColor.BLACK, 0);
        board.addPieceAt(centerPos.down(), StoneColor.BLACK, 0);
        board.addPieceAt(centerPos.lowerRight(), StoneColor.WHITE, 0);
        this.pieceCount.put(StoneColor.BLACK, 2);
        this.pieceCount.put(StoneColor.WHITE, 2);
    }

    @Override
    public StepResult takeStep(Board board, Position position, Stack<Board> boardHistory) {
        // set cache
        boardCache = new Board(board);
        // find opponent stone color
        StoneColor opponentStoneColor = switch (boardCache.actingStoneColor) {
            case BLACK -> StoneColor.WHITE;
            case WHITE -> StoneColor.BLACK;
        };

        // check board scan result
        BoardScanResult boardScanResult = this.scanBoard(boardCache);
        if (!boardScanResult.gameOver && !this.stepAvailable.get(boardCache.actingStoneColor)) {
            Logger.log("There are no available step for " + boardCache.actingStoneColor.string() + " player, please abstain or surrender.");
            return new StepResult(false, false, board, null);
        }

        //  landing on existing piece prohibited
        if (boardCache.pieceExistAt(position)) {
            return new StepResult(false, false, board, null);
        }

        // try to make step
        //  check for occupied pieces on every direction and flip them
        List<Position> occupiedPieces = new ArrayList<>();
        for (Direction d: Direction.values()) {
            Position p = position.nextPosition(d);
            List<Position> occupiedPiecesCache = new ArrayList<>();
            while (boardCache.pieceExistAt(p) && Objects.equals(boardCache.getStoneColorAt(p), opponentStoneColor)) {
                occupiedPiecesCache.add(p);
                p = p.nextPosition(d);
            }
            if (Objects.equals(boardCache.getStoneColorAt(p), boardCache.actingStoneColor)) {
                occupiedPieces.addAll(occupiedPiecesCache);
            }
        }
        if (occupiedPieces.isEmpty()) {
            //  fail if you cannot occupy other pieces
            Logger.log("Cannot land at " + position.x + "," + position.y + " : cannot occupy opponent piece." );
            return new StepResult(false, false, board, null);
        } else {
            // land on position
            boardCache.addPieceAt(position, boardCache.actingStoneColor, boardHistory.size() + 1);
            // flip all the captured pieces
            for (Position pos: occupiedPieces) boardCache.pieceArray[pos.x][pos.y].stoneColor = boardCache.actingStoneColor;
        }
        // succeed, scan board for available spaces
        boardScanResult = this.scanBoard(boardCache);
        return new StepResult(true, boardScanResult.gameOver, boardCache, boardScanResult.winner);
    }
}
