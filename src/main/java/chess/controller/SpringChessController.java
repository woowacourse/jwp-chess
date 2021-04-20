package chess.controller;

import chess.dto.PlayerDto;
import chess.dto.ScoreDto;
import chess.dto.request.MoveRequestDto;
import chess.dto.request.TurnChangeRequestDto;
import chess.dto.response.MoveResponseDto;
import chess.service.ChessService;
import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

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

    @GetMapping("/start")
    public String start() {
        chessService.start();
        return "redirect:/chess";
    }

    @GetMapping("/reset")
    public String reset() {
        chessService.reset();
        return "redirect:/chess";
    }

    @GetMapping("/chess")
    public String chess(final Model model) {
        Map<String, String> loadedBoard = chessService.getStoredBoard();

        String jsonFormatChessBoard = GSON.toJson(loadedBoard);
        model.addAttribute("jsonFormatChessBoard", jsonFormatChessBoard);

        //TODO: Room 도입하면서 고칠 수 있을 듯
        String currentTurn = chessService.getCurrentTurn();
        model.addAttribute("currentTurn", currentTurn);
        PlayerDto playerDto = chessService.playerDto();
        chessService.changeRoundToEnd(playerDto);

        ScoreDto scoreDto = chessService.scoreDto(playerDto);
        model.addAttribute("score", scoreDto);
        return "chess";
    }

    @PostMapping(value = "/move", produces = "application/json")
    @ResponseBody
    public MoveResponseDto move(@RequestBody MoveRequestDto moveRequestDto) {
        return chessService.move(moveRequestDto);
    }

    @PostMapping(value = "/turn", produces = "application/json")
    @ResponseBody
    public void turn(@RequestBody TurnChangeRequestDto turnChangeRequestDto) {
        chessService.changeTurn(turnChangeRequestDto);
    }
}