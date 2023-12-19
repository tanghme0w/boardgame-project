package refactor;

import globals.ChessType;

class Piece {
    ChessType chessType;
    Integer moveCount;
    //deep copy constructor - MUST CHECK FOR NULL BEFORE CALLING
    Piece(Piece piece) {
        chessType = piece.chessType;
        moveCount = piece.moveCount;
    }
    Piece(ChessType ct, Integer mId) {
        chessType = ct;
        moveCount = mId;
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
    public ChessType getChessTypeAt(Integer x, Integer y) {
        if(x < 0 || xSize < x || y < 0 || ySize < y ) return null;
        else return pieceArray[x][y] == null ? null : pieceArray[x][y].chessType;
    }
    public Integer getMoveIdAt(Integer x, Integer y) {
        if(x < 0 || xSize < x || y < 0 || ySize < y ) return null;
        else return pieceArray[x][y] == null ? null : pieceArray[x][y].moveCount;
    }
}
