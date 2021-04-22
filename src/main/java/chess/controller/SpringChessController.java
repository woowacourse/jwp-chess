package chess.controller;

import chess.domain.player.Round;
import chess.dto.ScoreDto;
import chess.dto.request.MoveRequestDto;
import chess.dto.request.TurnChangeRequestDto;
import chess.dto.response.MoveResponseDto;
import chess.service.ChessService;
import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class SpringChessController {
    public static final Gson GSON = new Gson();

    private final ChessService chessService;

    public SpringChessController(final ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/makeRoom")
    @ResponseBody
    public int makeRoom() {
        return chessService.makeRoom();
    }

    @GetMapping("/chess/{roomId}")
    public String chess(final Model model, @PathVariable int roomId) {
        Round loadedRound = chessService.getStoredRound(roomId);
        String board = GSON.toJson(loadedRound.getBoard());
        model.addAttribute("jsonFormatChessBoard", board);

        String currentTurn = loadedRound.getCurrentTurn();
        model.addAttribute("currentTurn", currentTurn);

        Double whiteScore = chessService.getWhiteScore(roomId);
        Double blackScore = chessService.getBlackScore(roomId);

        ScoreDto scoreDto = new ScoreDto(whiteScore, blackScore);
        model.addAttribute("score", scoreDto);
        return "chess";
    }

    @PostMapping(value = "/move", produces = "application/json")
    @ResponseBody
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

    @PostMapping(value = "/turn", produces = "application/json")
    @ResponseBody
    public void turn(@RequestBody TurnChangeRequestDto turnChangeRequestDto) {
        String nextTurn = turnChangeRequestDto.getNextTurn();
        String currentTurn = turnChangeRequestDto.getCurrentTurn();
        int roomId = turnChangeRequestDto.getRoomId();
        chessService.changeTurn(nextTurn, currentTurn, roomId);
    }
}