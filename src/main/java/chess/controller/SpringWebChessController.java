package chess.controller;

import chess.controller.dto.MoveRequestDto;
import chess.controller.dto.RemoveRequestDto;
import chess.controller.dto.ResponseDto;
import chess.domain.game.Status;
import chess.domain.game.board.ChessBoard;
import chess.service.ChessService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@Controller
public class SpringWebChessController {

    private final ChessService chessService;

    public SpringWebChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("games", chessService.findAllGame());
        return "lobby";
    }

    @PostMapping("/create")
    public String create(@RequestParam String title, @RequestParam String password) throws IllegalArgumentException {
        long gameId = chessService.create(title, password);
        return "redirect:/play/" + gameId;
    }

    @GetMapping("/play/{gameId}")
    public String play(Model model, @PathVariable int gameId) {
        ChessBoard chessBoard = chessService.findBoard(gameId);
        if (chessService.checkStatus(chessBoard, Status.END)) {
            return "redirect:/result/" + gameId;
        }
        model.addAttribute("play", true);
        model.addAttribute("board", chessService.currentBoardForUI(chessBoard));
        return "game";
    }

    @PostMapping(value = "/move/{gameId}")
    public ResponseEntity<ResponseDto> move(@RequestBody MoveRequestDto moveRequestDto, @PathVariable int gameId) throws SQLException, IllegalArgumentException {
        chessService.move(moveRequestDto.getSource(), moveRequestDto.getTarget(), gameId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/status/{gameId}")
    public String status(Model model, @PathVariable int gameId) {
        ChessBoard chessBoard = chessService.findBoard(gameId);
        if (chessService.checkStatus(chessBoard, Status.PLAYING)) {
            model.addAttribute("play", true);
            model.addAttribute("status", chessService.status(chessBoard));
            model.addAttribute("board", chessService.currentBoardForUI(chessBoard));
            return "game";
        }
        return "redirect:/end";
    }

    @DeleteMapping("/delete/{gameId}")
    public String delete(@PathVariable int gameId, @RequestBody RemoveRequestDto removeRequestDto) throws IllegalArgumentException {
        chessService.deleteGame(gameId, removeRequestDto.getPassword());
        return "lobby";
    }

    @GetMapping("/end/{gameId}")
    public ResponseEntity<ResponseDto> end(@PathVariable int gameId) throws SQLException, IllegalArgumentException {
        chessService.end(gameId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/result/{gameId}")
    public String result(Model model, @PathVariable int gameId) throws SQLException {
        ChessBoard chessBoard = chessService.findBoard(gameId);
        model.addAttribute("play", true);
        model.addAttribute("status", chessService.status(chessBoard));
        model.addAttribute("board", chessService.currentBoardForUI(chessBoard));
        model.addAttribute("winner", chessService.findWinner(chessBoard));
        return "game";
    }
}