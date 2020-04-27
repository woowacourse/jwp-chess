package wooteco.chess.service;

import wooteco.chess.dao.BoardDao;
import wooteco.chess.dao.RoomDao;
import wooteco.chess.domain.Board;
import wooteco.chess.domain.Pieces;
import wooteco.chess.domain.Position;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.Team;

public class ChessService {
    private final BoardDao boardDao;
    private final RoomDao roomDao;

    public ChessService(BoardDao boardDao, RoomDao roomDao) {
        this.boardDao = boardDao;
        this.roomDao = roomDao;
    }

    public void initBoard(int roomId) {
        roomDao.updateTurn(roomId, Team.WHITE);
        boardDao.removeAll(roomId);
        boardDao.saveBoard(new Board(), roomId);
    }

    public int createBoard(String name) {
        roomDao.createRoom(name);
        int roomId = roomDao.findRoomIdByName(name);
        boardDao.removeAll(roomId);
        boardDao.saveBoard(new Board(), roomId);
        return roomId;
    }

    public Board getSavedBoard(int roomId) {
        return new Board(boardDao.findByRoomId(roomId), roomDao.findTurnById(roomId));
    }

    public void processMoveInput(Board board, String source, String destination, int roomId) {
        board.movePiece(new Position(source), new Position(destination));
        Pieces pieces = board.getPieces();
        Piece destinationPiece = pieces.findByPosition(new Position(destination));
        if (destinationPiece == null) {
            boardDao.editPiece(source, destination, roomId);
        }
        if (destinationPiece != null) {
            boardDao.removePiece(destination, roomId);
            boardDao.editPiece(source, destination, roomId);
        }
        roomDao.updateTurn(roomId, board.getTurn());
    }
}
