package chess.web.service;

import chess.board.Board;
import chess.board.Room;
import chess.board.Team;
import chess.board.Turn;
import chess.board.piece.Empty;
import chess.board.piece.Piece;
import chess.board.piece.Pieces;
import chess.board.piece.position.Position;
import chess.web.dao.PieceDao;
import chess.web.dao.RoomDao;
import chess.web.service.dto.MoveDto;
import chess.web.service.dto.ScoreDto;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChessService {

    private final PieceDao pieceDao;
    private final RoomDao roomDao;

    @Autowired
    public ChessService(PieceDao pieceDao, RoomDao roomDao) {
        this.pieceDao = pieceDao;
        this.roomDao = roomDao;
    }

    public Board loadGame(Long roomId) {
        Room room = roomDao.findById(roomId)
                .orElseThrow(() -> new NoSuchElementException("없는 체스방입니다."));
        Turn turn = new Turn(Team.from(room.getTurn()));

        List<Piece> pieces = pieceDao.findAllByRoomId(roomId);
        Board board = Board.create(Pieces.from(pieces), turn);

        if (board.isDeadKing() || pieces.isEmpty()) {
            return initBoard(roomId);
        }
        return board;
    }

    public Board move(final MoveDto moveDto, final Long roomId) {
        Room room = roomDao.findById(roomId)
                .orElseThrow(() -> new NoSuchElementException("없는 체스방입니다."));
        Turn turn = new Turn(Team.from(room.getTurn()));

        Board board = Board.create(Pieces.from(pieceDao.findAllByRoomId(roomId)), turn);
        Pieces pieces = board.getPieces();

        Piece piece = pieces.findByPosition(Position.from(moveDto.getFrom()));
        board.move(List.of(moveDto.getFrom(), moveDto.getTo()), turn);
        Turn changedTurn = updatePieces(moveDto, turn, piece, roomId);

        return Board.create(pieces, changedTurn);
    }

    private Turn updatePieces(MoveDto moveDto, Turn turn, Piece piece, final Long roomId) {
        Turn changedTurn = changeTurn(turn, roomId);
        Empty empty = new Empty(Position.from(moveDto.getFrom()));
        pieceDao.updatePieceByPositionAndRoomId(empty.getType(), empty.getTeam().value(), moveDto.getFrom(), roomId);
        pieceDao.updatePieceByPositionAndRoomId(piece.getType(), piece.getTeam().value(), moveDto.getTo(), roomId);
        return changedTurn;
    }

    private Turn changeTurn(Turn turn, Long roomId) {
        Turn change = turn.change();
        roomDao.updateTurnById(roomId, change.getTeam().value());
        return change;
    }

    public Board initBoard(Long roomId) {
        initTurn(roomId);
        initPiece(roomId);
        return loadGame(roomId);
    }

    private void initTurn(Long roomId) {
        roomDao.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("없는 체스방 id 입니다."));
        roomDao.updateTurnById(roomId, Turn.init().getTeam().value());
    }

    private void initPiece(Long roomId) {
        pieceDao.deleteAllByRoomId(roomId);

        Pieces pieces = Pieces.createInit();
        for (Piece piece : pieces.getPieces()) {
            insertOrUpdatePiece(roomId, piece);
        }
    }

    private void insertOrUpdatePiece(Long roomId, Piece piece) {
        String position = piece.getPosition().name();
        String type = piece.getType();
        String team = piece.getTeam().value();

        if (pieceDao.findByPositionAndRoomId(position, roomId).isPresent()) {
            pieceDao.updatePieceByPositionAndRoomId(type, team, position, roomId);
            return;
        }
        pieceDao.save(piece, roomId);
    }

    public ScoreDto getStatus(Long roomId) {
        List<Piece> found = pieceDao.findAllByRoomId(roomId);
        Pieces pieces = Pieces.from(found);

        double blackScore = pieces.getTotalScore(Team.BLACK);
        double whiteScore = pieces.getTotalScore(Team.WHITE);
        return new ScoreDto(blackScore, whiteScore);
    }

    public Long createRoom(String title, String password) {
        Long id = roomDao.save(Turn.init().getTeam().value(), title, password);
        pieceDao.save(Pieces.createInit().getPieces(), id);
        return id;
    }

    public List<Room> getRooms() {
        return roomDao.findAll();
    }

    public boolean removeRoom(Long roomId, String password) {
        if (!hasRoom(roomId) || isRunningChess(roomId) || !matchPassword(roomId, password)) {
            return false;
        }
        pieceDao.deleteAllByRoomId(roomId);
        roomDao.delete(roomId, password);
        return true;
    }

    private boolean hasRoom(Long roomId) {
        return roomDao.findById(roomId).isPresent();
    }

    private boolean isRunningChess(Long roomId) {
        long kingCount = pieceDao.findAllByRoomId(roomId).stream()
                .filter(Piece::isKing)
                .count();

        return kingCount == 2;
    }

    private boolean matchPassword(Long roomId, String password) {
        Room room = roomDao.findById(roomId).get();
        return password.equals(room.getPassword());
    }
}
