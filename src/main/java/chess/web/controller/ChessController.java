package chess.web.controller;

import chess.domain.ChessGame;
import chess.domain.Color;
import chess.domain.position.Position;
import chess.web.dto.MovePositionsDto;
import chess.web.dto.MoveResultDto;
import chess.web.service.ChessService;
import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ChessController {

    private final ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String rendIndexPage(final Model model) {
        if (chessService.isNotRunning()) {
            return "redirect:/start";
        }
        model.addAttribute("chessStatus", chessService.getChessStatus());
        return "index";
    }

    @ResponseBody
    @PostMapping("/move")
    public String movePiece(@RequestBody MovePositionsDto movePositionsDto) {
        ChessGame chessGame = chessService.getChessGame();
        Gson gson = new Gson();

        try {
            chessGame.move(movePositionsDto.getSource(), movePositionsDto.getTarget());
            Position sourcePosition = new Position(movePositionsDto.getSource());
            Position targetPosition = new Position(movePositionsDto.getTarget());
            chessService.move(chessGame, sourcePosition, targetPosition);


        } catch (Exception ex) {
            return gson.toJson(new MoveResultDto(400, ex.getMessage(), chessGame.isFinished()));
        }
        return gson.toJson(new MoveResultDto(200, "", chessGame.isFinished()));
    }

    @GetMapping("/start")
    public String startChess() {
        chessService.start();

        return "redirect:/";
    }

    @GetMapping("/result")
    public String rendResultPage(final Model model) {
        ChessGame chessGame = chessService.getChessGame();
        endGame(chessGame);

        model.addAttribute("black-score", chessService.getScore(Color.WHITE));
        model.addAttribute("white-score", chessService.getScore(Color.BLACK));
        model.addAttribute("winner", chessGame.result().toString());

        chessService.end();

        return "result";
    }

    private void endGame(ChessGame chessGame) {
        if (!chessGame.isFinished()) {
            chessGame.end();
        }
    }
}
