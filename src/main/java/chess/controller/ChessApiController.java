package chess.controller;

import chess.dto.request.MoveRequestDto;
import chess.dto.request.TurnChangeRequestDto;
import chess.dto.response.MoveResponseDto;
import chess.service.ChessService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChessApiController {
    private final ChessService chessService;

    public ChessApiController(final ChessService chessService) {
        this.chessService = chessService;
    }

    @PostMapping("/rooms")
    public int makeRoom() {
        return chessService.makeRoom();
    }

    @PostMapping(value = "/move", produces = MediaType.APPLICATION_JSON_VALUE)
    public MoveResponseDto move(@RequestBody MoveRequestDto moveRequestDto) {
        String source = moveRequestDto.getSource();
        String target = moveRequestDto.getTarget();
        int roomId = moveRequestDto.getRoomId();
        try {
            chessService.move(source, target, roomId);
        } catch (RuntimeException runtimeException) {
            return new MoveResponseDto(true, runtimeException.getMessage());
        }
        return new MoveResponseDto(false);
    }

    @PostMapping(value = "/turn", produces = MediaType.APPLICATION_JSON_VALUE)
    public void turn(@RequestBody TurnChangeRequestDto turnChangeRequestDto) {
        String nextTurn = turnChangeRequestDto.getNextTurn();
        String currentTurn = turnChangeRequestDto.getCurrentTurn();
        int roomId = turnChangeRequestDto.getRoomId();
        chessService.changeTurn(nextTurn, currentTurn, roomId);
    }
}
