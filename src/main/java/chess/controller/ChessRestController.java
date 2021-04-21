package chess.controller;

import chess.domain.board.Team;
import chess.domain.response.ChessResponse;
import chess.domain.response.ErrorResponse;
import chess.domain.response.GameResponse;
import chess.dto.MoveRequestDto;
import chess.dto.InitialGameInfoDto;
import chess.dto.UserInfoDto;
import chess.service.ChessService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/rooms")
public class ChessRestController {

    private final ChessService chessService;

    public ChessRestController(ChessService chessService) {
        this.chessService = chessService;
    }

    @PostMapping("/first")
    public ResponseEntity<String> saveInfo(@RequestBody InitialGameInfoDto initialGameInfoDto,
                                           HttpServletRequest request) {
        final String roomId = chessService.addRoom(initialGameInfoDto.getName());
        chessService.addUser(roomId, initialGameInfoDto.getPassword(), Team.WHITE.team());

        HttpSession session = request.getSession();
        session.setAttribute("password",initialGameInfoDto.getPassword());
        return ResponseEntity.ok(roomId);
    }

    @PostMapping("/second")
    public ResponseEntity<String> saveSecondUser(@RequestBody UserInfoDto userInfoDto,
                                                 HttpServletRequest request) {
        final String roomId = userInfoDto.getId();
        final String password = userInfoDto.getPassword();
        if (chessService.checkRoomFull(roomId)) {
            throw new IllegalArgumentException("이미 꽉 찬 방이에요 😅");
        }
        if (chessService.checkSamePassword(roomId,password)) {
            throw new IllegalArgumentException("굉장하군요. 백팀 참가자와 같은 비밀번호를 입력했어요😲 다른 비밀번호로 부탁해요~");
        }
        chessService.updateRoomState(roomId);
        chessService.addUser(roomId, password, Team.BLACK.team());

        HttpSession session = request.getSession();
        session.setAttribute("password",password);
        return ResponseEntity.ok(roomId);
    }

    @PostMapping("/move")
    public ResponseEntity<ChessResponse> move(@RequestBody MoveRequestDto moveRequestDto,
                                              HttpServletRequest request) {
        String id = moveRequestDto.getGameId();
        if (!chessService.checkRoomFull(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("흑팀 참가자가 아직 입장하지 않았습니다😞"));
        }

        HttpSession session = request.getSession();
        final Object password = session.getAttribute("password");
        String command = makeMoveCmd(moveRequestDto.getSource(), moveRequestDto.getTarget());
        chessService.move(id, command, new UserInfoDto(id, password));
        return ResponseEntity.ok(new GameResponse(chessService.gameInfo(id), id));
    }

    private String makeMoveCmd(String source, String target) {
        return String.join(" ", "move", source, target);
    }
}
