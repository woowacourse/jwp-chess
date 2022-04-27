package chess.controller;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import chess.controller.dto.ChessPieceMoveRequest;
import chess.controller.dto.ChessPieceResponse;
import chess.controller.dto.RoomDeleteRequest;
import chess.controller.dto.RoomResponse;
import chess.controller.dto.RoomSaveRequest;
import chess.dao.ChessPieceMapper;
import chess.dao.JdbcChessPieceDao;
import chess.dao.JdbcRoomDao;
import chess.dao.dto.ChessPieceDeleteDto;
import chess.dao.dto.ChessPieceUpdateDto;
import chess.dao.dto.RoomSaveDto;
import chess.dao.dto.RoomUpdateDto;
import chess.domain.ChessGame;
import chess.domain.GameStatus;
import chess.domain.Score;
import chess.domain.chessboard.ChessBoard;
import chess.domain.chessboard.ChessBoardFactory;
import chess.domain.chesspiece.ChessPiece;
import chess.domain.chesspiece.Color;
import chess.domain.position.Position;
import chess.domain.result.EndResult;
import chess.domain.result.MoveResult;
import chess.domain.result.StartResult;
import chess.entity.ChessPieceEntity;
import chess.entity.RoomEntity;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/rooms", produces = MediaType.APPLICATION_JSON_VALUE)
public class ChessApiController {

    private final JdbcRoomDao jdbcRoomDao;
    private final JdbcChessPieceDao jdbcChessPieceDao;

    public ChessApiController(final JdbcRoomDao jdbcRoomDao, final JdbcChessPieceDao jdbcChessPieceDao) {
        this.jdbcRoomDao = jdbcRoomDao;
        this.jdbcChessPieceDao = jdbcChessPieceDao;
    }

    @GetMapping
    public ResponseEntity<List<RoomResponse>> getRooms() {
        List<RoomEntity> rooms = jdbcRoomDao.findAll();
        List<RoomResponse> roomResponses = rooms.stream()
                .map(RoomResponse::new)
                .collect(toList());

        return ResponseEntity.ok(roomResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> getRoom(@PathVariable final int id) {
        RoomEntity roomEntity = getRoomEntity(id);

        return ResponseEntity.ok(new RoomResponse(roomEntity));
    }

    @PostMapping
    public ResponseEntity<Integer> createRoom(@RequestBody final RoomSaveRequest request) {
        int id = jdbcRoomDao.save(
                new RoomSaveDto(request.getName(), request.getPassword(), GameStatus.READY, Color.WHITE));

        return ResponseEntity.created(URI.create("/api/rooms/" + id)).body(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable final int id, @RequestBody final RoomDeleteRequest request) {
        jdbcRoomDao.deleteByIdAndPassword(id, request.getPassword());

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/pieces")
    public ResponseEntity<Void> createPieces(@PathVariable final int id) {
        final RoomEntity roomEntity = getRoomEntity(id);
        final ChessGame chessGame = getChessGame(roomEntity);

        final StartResult startResult = chessGame.start();
        jdbcChessPieceDao.deleteByRoomId(roomEntity.getId());
        jdbcChessPieceDao.saveAll(roomEntity.getId(), startResult.getPieceByPosition());
        jdbcRoomDao.update(new RoomUpdateDto(roomEntity.getId(), GameStatus.PLAYING, Color.WHITE));

        return ResponseEntity.created(URI.create("/api/rooms/" + id + "/pieces")).build();
    }

    @GetMapping("/{id}/pieces")
    public ResponseEntity<List<ChessPieceResponse>> getPieces(@PathVariable final int id) {
        List<ChessPieceResponse> chessPieces = jdbcChessPieceDao.findByRoomId(id)
                .stream()
                .map(ChessPieceResponse::new)
                .collect(toList());

        return ResponseEntity.ok(chessPieces);
    }

    @PatchMapping("/{id}/pieces")
    public ResponseEntity<MoveResult> movePiece(@PathVariable final int id,
                                                @RequestBody final ChessPieceMoveRequest request) {
        final RoomEntity roomEntity = getRoomEntity(id);
        final ChessGame chessGame = getChessGame(roomEntity);

        final Position from = Position.from(request.getFrom());
        final Position to = Position.from(request.getTo());

        final MoveResult moveResult = chessGame.move(from, to);
        jdbcChessPieceDao.deleteByRoomIdAndPosition(new ChessPieceDeleteDto(roomEntity.getId(), to));
        jdbcChessPieceDao.update(new ChessPieceUpdateDto(roomEntity.getId(), from, to));
        jdbcRoomDao.update(
                new RoomUpdateDto(roomEntity.getId(), moveResult.getGameStatus(), moveResult.getCurrentTurn()));

        return ResponseEntity.ok(moveResult);
    }

    @GetMapping("/{id}/scores")
    public ResponseEntity<Score> getScore(@PathVariable final int id) {
        final ChessGame chessGame = getChessGame(getRoomEntity(id));

        return ResponseEntity.ok(chessGame.calculateScore());
    }

    @GetMapping("/{id}/result")
    public ResponseEntity<EndResult> getResult(@PathVariable final int id) {
        final RoomEntity roomEntity = getRoomEntity(id);
        final ChessGame chessGame = getChessGame(roomEntity);
        final EndResult endResult = chessGame.end();

        jdbcRoomDao.update(new RoomUpdateDto(roomEntity.getId(), GameStatus.END, Color.WHITE));

        return ResponseEntity.ok(endResult);
    }

    private RoomEntity getRoomEntity(int id) {
        return jdbcRoomDao.findById(id).orElseThrow(() -> new NoSuchElementException("방이 존재하지 않습니다."));
    }

    private ChessGame getChessGame(final RoomEntity roomEntity) {
        final Map<Position, ChessPiece> piecesByPosition = initPieces(roomEntity.getId());
        return new ChessGame(new ChessBoard(piecesByPosition, Color.from(roomEntity.getCurrentTurn())),
                GameStatus.from(roomEntity.getGameStatus()));
    }

    private Map<Position, ChessPiece> initPieces(final int roomId) {
        final List<ChessPieceEntity> pieces = jdbcChessPieceDao.findByRoomId(roomId);
        if (pieces.isEmpty()) {
            return ChessBoardFactory.createInitPieceByPosition();
        }

        return pieces.stream()
                .collect(toMap(
                        piece -> Position.from(piece.getPosition()),
                        piece -> ChessPieceMapper.toChessPiece(piece.getChessPiece(), piece.getColor())
                ));
    }
}
