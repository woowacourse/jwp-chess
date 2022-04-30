package chess.controller;

import chess.domain.chessboard.ChessBoard;
import chess.domain.piece.generator.NormalPiecesGenerator;
import chess.dto.BoardDto;
import chess.dto.GameDto;
import chess.dto.MoveDto;
import chess.dto.StatusDto;
import chess.service.ChessService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WebController {

    private final ChessService chessService;

    public WebController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public ModelAndView selectGame() {
        Map<String, Object> model = new HashMap<>();
        List<GameDto> games = chessService.findGame();
        model.put("game", games);
        return new ModelAndView("index", model);
    }

    @PutMapping("/game")
    public ModelAndView insertGame(String title, String password) {
        chessService.insertGame(title, password, new ChessBoard(new NormalPiecesGenerator()));
        return new ModelAndView("redirect:/");
    }

    @GetMapping("/game/{gameId}")
    public ModelAndView startGame(@PathVariable int gameId) {
        BoardDto boardDto = chessService.selectBoard(gameId);
        String winner = chessService.selectWinner(gameId);
        String state = chessService.selectState(gameId);

        Map<String, Object> model = new HashMap<>();
        model.put("board", boardDto);
        model.put("id", gameId);
        model.put("winner", winner);
        model.put("state", state);

        return new ModelAndView("game", model);
    }

    @PutMapping("/game/{gameId}")
    public ModelAndView updateBoard(@PathVariable int gameId, MoveDto moveDto) {
        chessService.updateBoard(gameId, moveDto.getFrom(), moveDto.getTo());

        return new ModelAndView("redirect:/game/" + gameId);
    }

    @PostMapping("/game/{gameId}")
    public ModelAndView restartGame(@PathVariable int gameId) {
        chessService.restartGame(gameId);
        return new ModelAndView("redirect:/game/" + gameId);
    }

    @GetMapping("/game/status/{gameId}")
    @ResponseBody
    public ResponseEntity<StatusDto> selectStatus(@PathVariable int gameId) {
        StatusDto statusDto = chessService.selectStatus(gameId);
        return ResponseEntity.ok().body(statusDto);
    }

    @PutMapping("/game/end/{gameId}")
    public ModelAndView endGame(@PathVariable int gameId) {
        chessService.endGame(gameId);
        return new ModelAndView("redirect:/");
    }

    @DeleteMapping("/game/{gameId}")
    public ModelAndView deleteGame(@PathVariable int gameId, String password) {
        chessService.deleteGame(gameId, password);
        return new ModelAndView("redirect:/");
    }
}
