package chess.controller;

import chess.service.SpringChessService;
import chess.webdto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
public class SpringChessController {
    private final SpringChessService springChessService;

    public SpringChessController(SpringChessService springChessService) {
        this.springChessService = springChessService;
    }

    private boolean generateSession(final HttpServletRequest request, final String id, final String password) {
        final HttpSession session = request.getSession();
        if (springChessService.loginUser(id, password)) {
            session.setAttribute("id", id);
            return true;
        }
        return false;
    }

    private void validateSession(final HttpServletRequest request) throws LoginException {
        final HttpSession session = request.getSession();
        final String id = (String) session.getAttribute("id");
        if (Objects.isNull(id) || id.length() == 0) {
            throw new LoginException();
        }
    }

    @PostMapping(value = "/signup")
    public ResponseEntity<String> signup(@RequestBody UserInfoDto userInfoDto) {
        final String id = userInfoDto.getId();
        final String password = userInfoDto.getPassword();
        springChessService.createUser(id, password);
        return ResponseEntity.ok("success");
    }

    @PostMapping(value = "/login")
    public ResponseEntity<String> login(@RequestBody UserInfoDto userInfoDto, final HttpServletRequest request) {
        final String id = userInfoDto.getId();
        final String password = userInfoDto.getPassword();
        if (generateSession(request, id, password)) {
            return ResponseEntity.ok("success");
        }
        return ResponseEntity.status(NOT_FOUND).body("not-found");
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
        return springChessService.createGameRoom(roomName);
    }

    @GetMapping(value = "/games")
    public GameRoomListDto loadGameRooms(final HttpServletRequest request) throws LoginException {
        validateSession(request);
        return springChessService.loadGameRooms();
    }

    @PostMapping(value = "/games/{roomId}")
    public ChessGameDto startNewGame(@PathVariable int roomId, final HttpServletRequest request) throws LoginException {
        validateSession(request);
        return springChessService.createChessGame(roomId);
    }

    @GetMapping(value = "/games/{roomId}")
    public ChessGameDto loadSavedGame(@PathVariable int roomId, final HttpServletRequest request) throws LoginException {
        validateSession(request);
        return springChessService.readChessGame(roomId);
    }

    @DeleteMapping(value = "/games/{roomId}")
    public ResponseEntity<String> deleteGame(@PathVariable int roomId, final HttpServletRequest request)
            throws LoginException {
        validateSession(request);
        springChessService.deleteChessGame(roomId);
        return ResponseEntity.ok("success");
    }

    @PostMapping(value = "/games/{roomId}/move")
    public ChessGameDto move(@RequestBody MoveRequestDto moveRequestDto, @PathVariable int roomId,
                             final HttpServletRequest request) throws LoginException {
        validateSession(request);
        final String start = moveRequestDto.getStart();
        final String destination = moveRequestDto.getDestination();
        return springChessService.move(roomId, start, destination);
    }
}
