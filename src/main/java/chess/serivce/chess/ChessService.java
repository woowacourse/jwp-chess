package chess.serivce.chess;

import chess.dao.piece.PieceDao;
import chess.dao.room.RoomDao;
import chess.domain.board.Board;
import chess.domain.dto.PieceDto;
import chess.domain.dto.RoomDto;
import chess.domain.dto.RoomsDto;
import chess.domain.dto.move.MoveResponseDto;
import chess.domain.game.Room;
import chess.domain.gamestate.State;
import chess.domain.gamestate.running.Ready;
import chess.domain.location.Location;
import chess.domain.piece.Piece;
import chess.domain.team.Team;
import chess.exceptions.DuplicateRoomException;
import chess.exceptions.NoRoomException;
import chess.utils.BoardUtil;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChessService {

    private final RoomDao roomDao;
    private final PieceDao pieceDao;

    public ChessService(final RoomDao roomDao,
            final PieceDao pieceDao) {
        this.roomDao = roomDao;
        this.pieceDao = pieceDao;
    }

    public void createRoom(String roomName) {
        if (roomDao.roomExists(roomName)) {
            throw new DuplicateRoomException("이미 존재하는 방입니다. 다른 이름을 사용해주세요.");
        }

        Room room = new Room(0, roomName, new Ready(BoardUtil.generateInitialBoard()), Team.WHITE);
        room.play("start");
        long roomId = roomDao.insert(room);
        Board board = BoardUtil.generateInitialBoard();
        for (Piece piece : board.getPieces()) {
            pieceDao.insert(roomId, piece);
        }
    }

    @Transactional
    public MoveResponseDto move(String roomName, String source, String target) {
        Room room = findRoomByName(roomName);
        Board board = room.getBoard();
        Piece sourcePiece = board.find(Location.of(source));
        List<Piece> beforeMovePieces = board.getPieces();

        room.play("move " + source + " " + target);
        roomDao.update(room);
        List<Piece> afterMovePieces = board.getPieces();

        if (beforeMovePieces.size() != afterMovePieces.size()) {
            Piece removedPiece = beforeMovePieces
                    .stream()
                    .filter(piece -> piece.getLocation().equals(Location.of(target)))
                    .filter(piece -> !piece.equals(sourcePiece))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 기물입니다."));
            pieceDao.deletePieceById(removedPiece.getId());
        }

        for (Piece piece : board.getPieces()) {
            pieceDao.update(piece);
        }
        return new MoveResponseDto(
                pieceDtos(room.getBoard()),
                room.getCurrentTeam().getValue(),
                room.judgeResult()
        );
    }

    @Transactional
    public MoveResponseDto end(String roomName) {
        Room room = findRoomByName(roomName);

        room.play("end");
        roomDao.update(room);
        return new MoveResponseDto(
            pieceDtos(room.getBoard()),
            room.getCurrentTeam().getValue(),
            room.judgeResult()
        );
    }

    @Transactional(readOnly = true)
    public MoveResponseDto findPiecesInRoom(String roomName) {
        Room room = findRoomByName(roomName);
        return new MoveResponseDto(
            pieceDtos(room.getBoard()),
            room.getCurrentTeam().getValue(),
            room.judgeResult()
        );
    }

    private Room findRoomByName(String roomName) {
        if (!roomDao.roomExists(roomName)) {
            throw new NoRoomException("존재하지 않는 방입니다.");
        }
        Room room = roomDao.findByName(roomName);
        List<Piece> pieces = pieceDao.findPiecesByRoomId(room.getId());
        return new Room(
                room.getId(),
                roomName,
                State.generateState(room.getState().getValue(), Board.of(pieces)),
                room.getCurrentTeam());
    }

    @Transactional(readOnly = true)
    public RoomsDto findAllRooms() {
        List<Room> rooms = roomDao.findAll();
        List<RoomDto> roomDtos = rooms.stream()
                .map(Room::getName)
                .map(RoomDto::new)
                .collect(Collectors.toList());
        return new RoomsDto(roomDtos);
    }


    private List<PieceDto> pieceDtos(Board board) {
        return board.getPieces()
                .stream()
                .map(piece -> PieceDto.from(piece))
                .collect(Collectors.toList());
    }
}
