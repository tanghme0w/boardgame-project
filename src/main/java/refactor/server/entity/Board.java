package refactor.server.entity;

import globals.StoneColor;

import java.io.Serializable;

class Piece implements Serializable {
    StoneColor stoneColor;
    Integer stepCount;

    //deep copy constructor - MUST CHECK FOR NULL BEFORE CALLING
    Piece(Piece piece) {
        stoneColor = piece.stoneColor;
        stepCount = piece.stepCount;
    }

    Piece(StoneColor ct, Integer mId) {
        stoneColor = ct;
        stepCount = mId;
    }
}

public class Board implements Serializable {
    public Integer xSize;
    public Integer ySize;
    Piece[][] pieceArray;
    public StoneColor actingStoneColor;
    //deep copy construct
    public Board(Board board) {
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
        actingStoneColor = board.actingStoneColor;
    }

    public boolean equals(Board board) {
        //same board = same size && chess type equals everywhere
        if (!board.xSize.equals(this.xSize) || !board.ySize.equals(this.ySize)) return false;
        for (int i = 0; i < board.pieceArray.length; i++) {
            for (int j = 0; j < board.pieceArray[i].length; j++) {
                if (this.pieceArray[i][j] == null && board.pieceArray[i][j] == null) continue;
                if (this.pieceArray[i][j] == null || board.pieceArray[i][j] == null) return false;
                else if (!board.pieceArray[i][j].stoneColor.equals(this.pieceArray[i][j].stoneColor)) return false;
            }
        }
        return true;
    }

    public Board(Integer boardSizeX, Integer boardSizeY) {
        xSize = boardSizeX;
        ySize = boardSizeY;
        pieceArray = new Piece[xSize + 1][ySize + 1];
    }
    public boolean outOfBound(Position position) {
        return (position.x < 1 || xSize < position.x || position.y < 1 || ySize < position.y);
    }
    public boolean pieceExistAt(Position position) {
        return !outOfBound(position) && pieceArray[position.x][position.y] != null;
    }
    public void addPieceAt(Position position, StoneColor stoneColor, Integer stepCount) {
        if(outOfBound(position) || pieceExistAt(position)) return;
        pieceArray[position.x][position.y] = new Piece(stoneColor, stepCount);
    }
    public void removePieceAt(Position position) {
        if(outOfBound(position)) return;
        pieceArray[position.x][position.y] = null;
    }
    public StoneColor getStoneColorAt(Position position) {
        if(outOfBound(position) || !pieceExistAt(position)) return null;
        return pieceArray[position.x][position.y].stoneColor;
    }
    public Integer getStepIdAt(Position position) {
        if(outOfBound(position) || !pieceExistAt(position)) return null;
        return pieceArray[position.x][position.y].stepCount;
    }
}
