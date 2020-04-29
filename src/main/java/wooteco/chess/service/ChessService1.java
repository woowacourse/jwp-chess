package wooteco.chess.service;

import org.springframework.stereotype.Service;

import wooteco.chess.dao.BoardDao;
import wooteco.chess.dao.RoomDao;
import wooteco.chess.domain.Board;
import wooteco.chess.domain.Pieces;
import wooteco.chess.domain.Position;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.exception.DuplicateRoomNameException;

@Service
public class ChessService1 {
    private final BoardDao boardDao;
    private final RoomDao roomDao;

    public ChessService1(BoardDao boardDao, RoomDao roomDao) {
        this.boardDao = boardDao;
        this.roomDao = roomDao;
    }

    public void initBoard(int roomId) {
        roomDao.updateTurn(roomId, Team.WHITE);
        boardDao.removeAll(roomId);
        boardDao.saveBoard(new Board(), roomId);
    }

    public boolean isPresentRoom(String name) {
        return roomDao.findRoomIdByName(name).isPresent();
    }

    public int createBoard(String name) {
        validateRoomName(name);
        roomDao.createRoom(name);
        int roomId = roomDao.findRoomIdByName(name)
            .orElseThrow(AssertionError::new);
        boardDao.saveBoard(new Board(), roomId);
        return roomId;
    }

    private void validateRoomName(String name) {
        if (isPresentRoom(name)) {
            throw new DuplicateRoomNameException("존재하는 방 이름입니다.");
        }
        if (name.isEmpty()) {
            throw new IllegalArgumentException("방 이름을 입력해주세요.");
        }
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

    public int getIdByName(String name) {
        return roomDao.findRoomIdByName(name)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 방 이름입니다."));
    }

    public String findNameById(int roomId) {
        return roomDao.findRoomNameById(roomId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 방입니다."));
    }
}
