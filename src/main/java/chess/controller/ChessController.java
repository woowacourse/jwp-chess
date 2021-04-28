package chess.controller;

import chess.service.ChessService;
import chess.webdto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
public class ChessController {
    private final ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @PostMapping(value = "/signup")
    public ResponseEntity<String> signup(@RequestBody UserInfoDto userInfoDto) {
        final String id = userInfoDto.getId();
        final String password = userInfoDto.getPassword();
        chessService.createUser(id, password);
        return ResponseEntity.ok("success");
    }

    @PostMapping(value = "/login")
    public ResponseEntity<String> login(@RequestBody UserInfoDto userInfoDto, final HttpServletRequest request) {
        final String id = userInfoDto.getId();
        final String password = userInfoDto.getPassword();
        if (chessService.validateUser(id, password)) {
            generateSession(request, id);
            return ResponseEntity.ok("success");
        }
        return ResponseEntity.status(NOT_FOUND).body("not-found");
    }

    private void generateSession(final HttpServletRequest request, final String id) {
        final HttpSession session = request.getSession();
        session.setAttribute("id", id);
        session.setMaxInactiveInterval(60 * 30);
    }

    @GetMapping(value = "/login")
    public ResponseEntity<String> login(final HttpServletRequest request) {
        final HttpSession session = request.getSession();
        final String id = (String) session.getAttribute("id");
        if (Objects.isNull(id) || id.length() == 0) {
            return ResponseEntity.status(NOT_FOUND).body("not-found");
        }
        return ResponseEntity.ok(id);
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<String> logout(final HttpServletRequest request) {
        final HttpSession session = request.getSession();
        session.setAttribute("id", null);
        return ResponseEntity.ok("success");
    }

    @PostMapping(value = "/games")
    public GameRoomDto createGameRoom(@RequestBody GameRoomNameDto gameRoomNameDto, final HttpServletRequest request)
            throws LoginException {
        validateSession(request);
        final String roomName = gameRoomNameDto.getName();
        return chessService.createGameRoom(roomName);
    }

    @GetMapping(value = "/games")
    public GameRoomListDto loadGameRooms(final HttpServletRequest request) throws LoginException {
        validateSession(request);
        return chessService.loadGameRooms();
    }

    @PostMapping(value = "/games/{roomId}")
    public ChessGameDto startNewGame(@PathVariable int roomId, final HttpServletRequest request) throws LoginException {
        validateSession(request);
        return chessService.createChessGame(roomId);
    }

    @GetMapping(value = "/games/{roomId}")
    public ChessGameDto loadSavedGame(@PathVariable int roomId, final HttpServletRequest request) throws LoginException {
        validateSession(request);
        return chessService.readChessGame(roomId);
    }

    @DeleteMapping(value = "/games/{roomId}")
    public ResponseEntity<String> deleteGame(@PathVariable int roomId, final HttpServletRequest request)
            throws LoginException {
        validateSession(request);
        chessService.deleteChessGame(roomId);
        return ResponseEntity.ok("success");
    }

    @PostMapping(value = "/games/{roomId}/move")
    public ChessGameDto move(@RequestBody MoveRequestDto moveRequestDto, @PathVariable int roomId,
                             final HttpServletRequest request) throws LoginException {
        validateSession(request);
        final String start = moveRequestDto.getStart();
        final String destination = moveRequestDto.getDestination();
        return chessService.move(roomId, start, destination);
    }

    private void validateSession(final HttpServletRequest request) throws LoginException {
        final HttpSession session = request.getSession();
        final String id = (String) session.getAttribute("id");
        if (Objects.isNull(id) || id.length() == 0) {
            throw new LoginException();
        }
    }
}
