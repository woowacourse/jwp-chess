package chess.controller;

import static java.util.stream.Collectors.toList;

import chess.controller.dto.RoomDeleteRequest;
import chess.controller.dto.RoomSaveRequest;
import chess.controller.dto.RoomResponse;
import chess.dao.JdbcChessPieceDao;
import chess.dao.JdbcRoomDao;
import chess.dao.dto.RoomSaveDto;
import chess.domain.GameStatus;
import chess.domain.chesspiece.Color;
import chess.entity.RoomEntity;
import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

    private RoomEntity getRoomEntity(int id) {
        return jdbcRoomDao.findById(id).orElseThrow(() -> new NoSuchElementException());
    }

    @PostMapping
    public ResponseEntity<Void> createRoom(@RequestBody final RoomSaveRequest request) {
        int id = jdbcRoomDao.save(
                new RoomSaveDto(request.getName(), request.getPassword(), GameStatus.READY, Color.WHITE));

        return ResponseEntity.created(URI.create("/api/rooms/" + id)).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable final int id, @RequestBody final RoomDeleteRequest request) {
        jdbcRoomDao.deleteByIdAndPassword(id, request.getPassword());

        return ResponseEntity.noContent().build();
    }
}
