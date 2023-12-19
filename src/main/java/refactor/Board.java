package refactor;

import globals.ChessType;
import globals.MetadataType;

import java.util.HashMap;
import java.util.Map;

class Piece {
    ChessType chessType;
    Integer stepCount;
    Map<MetadataType, Integer> metadata;   //rule-specific metadata

    //deep copy constructor - MUST CHECK FOR NULL BEFORE CALLING
    Piece(Piece piece) {
        chessType = piece.chessType;
        stepCount = piece.stepCount;
        metadata = new HashMap<>(piece.metadata); //keys and values are immutable types, so this produces a deep copy
    }

    Piece(ChessType ct, Integer mId) {
        chessType = ct;
        stepCount = mId;
        metadata = new HashMap<>();
    }
}

public class Board {
    public Integer xSize;
    public Integer ySize;
    Piece[][] pieceArray;
    public ChessType nextChessType;
    //deep copy construct
    Board(Board board) {
        this.xSize = board.xSize;
        this.ySize = board.ySize;
        this.pieceArray = new Piece[board.pieceArray.length][];
        for (int i = 0; i < board.pieceArray.length; i++) {
            this.pieceArray[i] = new Piece[board.pieceArray[i].length];
            for (int j = 0; j < board.pieceArray[i].length; j++) {
                if (board.pieceArray[i][j] == null) this.pieceArray[i][j] = null;
                else this.pieceArray[i][j] = new Piece(board.pieceArray[i][j]);
            }
        }
        nextChessType = board.nextChessType;
    }
    public Board(Integer boardSizeX, Integer boardSizeY) {
        xSize = boardSizeX;
        ySize = boardSizeY;
        pieceArray = new Piece[xSize + 1][ySize + 1];
    }
    public boolean outOfBound(Position position) {
        return (position.x < 0 || xSize < position.x || position.y < 0 || ySize < position.y);
    }
    public boolean pieceExistAt(Position position) {
        return pieceArray[position.x][position.y] == null;
    }
    public void setPieceAt(Position position, ChessType chessType, Integer stepCount) {
        if(outOfBound(position)) return;
        pieceArray[position.x][position.y] = new Piece(chessType, stepCount);
    }
    public ChessType getChessTypeAt(Position position) {
        if(outOfBound(position) || pieceExistAt(position)) return null;
        return pieceArray[position.x][position.y].chessType;
    }
    public Integer getStepIdAt(Position position) {
        if(outOfBound(position) || pieceExistAt(position)) return null;
        return pieceArray[position.x][position.y].stepCount;
    }
    public void setMetadataAt(Position position, MetadataType key, Integer value) {
        if(outOfBound(position) || pieceExistAt(position)) return;
        pieceArray[position.x][position.y].metadata.put(key, value);
    }
    public Integer getMetadataAt(Position position, MetadataType key) {
        if(outOfBound(position) || pieceExistAt(position)) return null;
        return pieceArray[position.x][position.y].metadata.get(key);
    }
}
