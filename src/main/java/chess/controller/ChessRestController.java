package chess.controller;

import chess.domain.board.Team;
import chess.domain.response.ChessResponse;
import chess.domain.response.ErrorResponse;
import chess.domain.response.GameResponse;
import chess.dto.IdRequestDto;
import chess.dto.InitialGameInfoDto;
import chess.dto.MoveRequestDto;
import chess.dto.UserInfoDto;
import chess.service.ChessService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/rooms")
public class ChessRestController {

    private final ChessService chessService;

    public ChessRestController(ChessService chessService) {
        this.chessService = chessService;
    }

    @PostMapping
    public ResponseEntity<String> saveInfo(@RequestBody InitialGameInfoDto initialGameInfoDto,
                                           HttpServletRequest request) {
        final String roomId = chessService.addRoom(initialGameInfoDto.getName());
        chessService.addUser(roomId, initialGameInfoDto.getPassword(), Team.WHITE.team());

        HttpSession session = request.getSession();
        session.setAttribute("password", initialGameInfoDto.getPassword());
        return ResponseEntity.ok(roomId);
    }

    @PostMapping("/user")
    public ResponseEntity<String> saveSecondUser(@RequestBody UserInfoDto userInfoDto,
                                                 HttpServletRequest request) {
        final String roomId = userInfoDto.getId();
        final String password = userInfoDto.getPassword();

        chessService.updateToFull(roomId, password);
        chessService.addUser(roomId, password, Team.BLACK.team());

        HttpSession session = request.getSession();
        session.setAttribute("password", password);
        return ResponseEntity.ok(roomId);
    }

    @PostMapping("/move")
    public ResponseEntity<ChessResponse> move(@RequestBody MoveRequestDto moveRequestDto,
                                              HttpServletRequest request) {
        String id = moveRequestDto.getRoomId();
        HttpSession session = request.getSession();
        final Object password = session.getAttribute("password");
        String command = makeMoveCmd(moveRequestDto.getSource(), moveRequestDto.getTarget());
        chessService.move(id, command, new UserInfoDto(id, password));
        return ResponseEntity.ok(new GameResponse(chessService.gameInfo(id), id));
    }

    private String makeMoveCmd(String source, String target) {
        return String.join(" ", "move", source, target);
    }

    @PostMapping("/{id}/status")
    public ResponseEntity<Void> end(@PathVariable String id) {
        chessService.updateToEnd(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
