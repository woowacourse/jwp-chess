package chess.serivce.chess;

import chess.domain.board.Board;
import chess.domain.dto.PieceDto;
import chess.domain.dto.move.MoveResponseDto;
import chess.domain.game.Room;
import chess.domain.gamestate.State;
import chess.domain.gamestate.running.Ready;
import chess.domain.location.Location;
import chess.domain.piece.Piece;
import chess.domain.team.Team;
import chess.repository.piece.PieceRepository;
import chess.repository.room.RoomRepository;
import chess.utils.BoardUtil;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChessService {

    private final RoomRepository roomRepository;

    private final PieceRepository pieceRepository;

    public ChessService(final RoomRepository roomRepository,
            final PieceRepository pieceRepository) {
        this.roomRepository = roomRepository;
        this.pieceRepository = pieceRepository;
    }

    @Transactional
    public MoveResponseDto start(String roomName) throws SQLException {
        Room room = findRoomByRoomName(roomName);

        room.play("start");
        roomRepository.update(room);

        Board board = room.getBoard();

        return new MoveResponseDto(
            generatePieceDtosFromPieces(board.getPieces()),
            room.getCurrentTeam().getValue(),
            room.judgeResult()
        );
    }

    @Transactional
    public MoveResponseDto end(String roomName) throws SQLException {
        Room room = findRoomByRoomName(roomName);

        room.play("end");
        roomRepository.update(room);

        Board board = room.getBoard();
        return new MoveResponseDto(
            generatePieceDtosFromPieces(board.getPieces()),
            room.getCurrentTeam().getValue(),
            room.judgeResult()
        );
    }

    @Transactional
    public MoveResponseDto move(String roomName, String source, String target) throws SQLException {
        Room room = findRoomByRoomName(roomName);
        Board board = room.getBoard();
        Piece sourcePiece = board.find(Location.of(source));
        List<Piece> beforeMovePieces = board.getPieces();

        room.play("move " + source + " " + target);
        roomRepository.update(room);
        List<Piece> afterMovePieces = board.getPieces();

        if (beforeMovePieces.size() != afterMovePieces.size()) {
            Piece removedPiece = beforeMovePieces
                .stream()
                .filter(piece -> piece.getLocation().equals(Location.of(target)))
                .filter(piece -> !piece.equals(sourcePiece))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 기물입니다."));
            pieceRepository.deletePieceById(removedPiece.getId());
        }

        for (Piece piece : board.getPieces()) {
            pieceRepository.update(piece);
        }

        return new MoveResponseDto(
            generatePieceDtosFromPieces(board.getPieces()),
            room.getCurrentTeam().getValue(),
            room.judgeResult()
        );
    }

    @Transactional
    public MoveResponseDto findPiecesByRoomName(String roomName) throws SQLException {
        Room room = findRoomByRoomName(roomName);

        return new MoveResponseDto(
            generatePieceDtosFromPieces(pieceRepository.findPiecesByRoomId(room.getId())),
            room.getCurrentTeam().getValue(),
            room.judgeResult()
        );
    }

    public void createRoom(String roomName) throws SQLException {
        if (!roomRepository.isExistRoomName(roomName)) {
            throw new IllegalArgumentException("[ERROR] 이미 존재하는 방입니다. 다른 이름을 사용해주세요.");
        }

        long roomId = roomRepository.insert(new Room(0, roomName, new Ready(BoardUtil.generateInitialBoard()), Team.WHITE));
        Board board = BoardUtil.generateInitialBoard();
        for (Piece piece : board.getPieces()) {
            pieceRepository.insert(roomId, piece);
        }
    }

    private Room findRoomByRoomName(String roomName) throws SQLException {
        if (roomRepository.isExistRoomName(roomName)) {
            throw new IllegalArgumentException("[ERROR] 존재하지 않는 방입니다.");
        }
        Room room = roomRepository.findRoomByRoomName(roomName);
        List<Piece> pieces = pieceRepository.findPiecesByRoomId(room.getId());
        return new Room(
                room.getId(),
                roomName,
                State.generateState(room.getState().getValue(), Board.of(pieces)),
                room.getCurrentTeam());
    }

    private List<PieceDto> generatePieceDtosFromPieces(List<Piece> pieces) {
        return pieces
            .stream()
            .map(piece -> PieceDto.from(piece))
            .collect(Collectors.toList());
    }
}
