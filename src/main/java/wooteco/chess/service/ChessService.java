package wooteco.chess.service;

import java.sql.SQLException;

import wooteco.chess.dao.BoardDao;
import wooteco.chess.domain.Board;
import wooteco.chess.domain.Pieces;
import wooteco.chess.domain.Position;
import wooteco.chess.domain.piece.Piece;

public class ChessService {
    private final BoardDao boardDao;

    public ChessService(BoardDao boardDao) {
        this.boardDao = boardDao;
    }

    public void initBoard() throws SQLException {
        boardDao.save(new Board());
    }

    public Board getSavedBoard() throws SQLException {
        return boardDao.find();
    }

    public void processMoveInput(Board board, String source, String destination) throws SQLException {
        board.movePiece(new Position(source), new Position(destination));
        Pieces pieces = board.getPieces();
        Piece destinationPiece = pieces.findByPosition(new Position(destination));
        if (destinationPiece == null) {
            boardDao.editPiece(source, destination);
        }
        if (destinationPiece != null) {
            boardDao.removePiece(destination);
            boardDao.editPiece(source, destination);
        }
    }
}
