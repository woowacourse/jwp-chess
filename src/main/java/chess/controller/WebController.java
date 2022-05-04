package chess.controller;

import chess.domain.chessboard.ChessBoard;
import chess.domain.piece.generator.NormalPiecesGenerator;
import chess.dto.BoardDto;
import chess.dto.GameDto;
import chess.dto.MoveDto;
import chess.dto.RoomDto;
import chess.dto.StatusDto;
import chess.service.ChessService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WebController {

    private final ChessService chessService;

    public WebController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String selectGame(Model model) {
        List<GameDto> games = chessService.findGame();
        model.addAttribute("game", games);
        return "index";
    }

    @PostMapping("/game")
    public String insertGame(RoomDto roomDto) {
        chessService.insertGame(roomDto, new ChessBoard(new NormalPiecesGenerator()));
        return "redirect:/";
    }

    @GetMapping("/game/{gameId}")
    public String startGame(@PathVariable int gameId, Model model) {
        BoardDto boardDto = chessService.selectBoard(gameId);
        String winner = chessService.selectWinner(gameId);
        String state = chessService.selectState(gameId);

        model.addAttribute("board", boardDto);
        model.addAttribute("id", gameId);
        model.addAttribute("winner", winner);
        model.addAttribute("state", state);

        return "game";
    }

    @PutMapping("/game/board/{gameId}")
    public String movePiece(@PathVariable int gameId, MoveDto moveDto) {
        chessService.movePiece(gameId, moveDto.getFrom(), moveDto.getTo());
        return "redirect:/game/" + gameId;
    }

    @PutMapping("/game/{gameId}")
    public String restartGame(@PathVariable int gameId) {
        chessService.restartGame(gameId);
        return "redirect:/game/" + gameId;
    }

    @GetMapping("/game/status/{gameId}")
    @ResponseBody
    public ResponseEntity<StatusDto> selectStatus(@PathVariable int gameId) {
        StatusDto statusDto = chessService.selectStatus(gameId);
        return ResponseEntity.ok().body(statusDto);
    }

    @PutMapping("/game/end/{gameId}")
    public String endGame(@PathVariable int gameId) {
        chessService.endGame(gameId);
        return "redirect:/";
    }

    @DeleteMapping("/game/{gameId}")
    public String deleteGame(@PathVariable int gameId, String password) {
        chessService.deleteGame(gameId, password);
        return "redirect:/";
    }
}
