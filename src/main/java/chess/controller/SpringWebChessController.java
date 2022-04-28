package chess.controller;

import chess.controller.dto.MoveRequestDto;
import chess.controller.dto.ResponseDto;
import chess.domain.game.Status;
import chess.service.ChessService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.sql.SQLException;

@Controller
public class SpringWebChessController {

    private final ChessService chessService;

    public SpringWebChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("games",chessService.findAllGame());
        return "lobby";
    }

    @PostMapping("/create")
    public String create(@RequestParam String title, @RequestParam String password){
        long gameId = chessService.create(title, password);
        return "redirect:/play/" + gameId;
    }

    @GetMapping("/play/{gameId}")
    public String play(Model model, @PathVariable int gameId) {
        ChessBoard chessBoard = chessService.findBoard(gameId);
        if (chessService.checkStatus(chessBoard, Status.END)) {
            return "redirect:result";
        }
        model.addAttribute("play", true);
        model.addAttribute("board", chessService.currentBoardForUI(chessBoard));
        return "game";
    }

    @PostMapping(value = "/move", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto> move(@RequestBody MoveRequestDto moveRequestDto) throws SQLException, IllegalArgumentException {
        chessService.move(moveRequestDto.getSource(), moveRequestDto.getTarget());
        if (chessService.checkStatus(Status.END)) {
            chessService.end();
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/status")
    public String status(Model model) {
        if (chessService.checkStatus(Status.PLAYING)) {
            model.addAttribute("play", true);
            model.addAttribute("status", chessService.status());
            model.addAttribute("board", chessService.currentBoardForUI());
            return "game";
        }
        return "redirect:/end";
    }

    @PostMapping("/save")
    public String save() {
        if (chessService.checkStatus(Status.PLAYING)) {
            chessService.save();
        }
        return "redirect:/play";
    }

    @GetMapping("/end")
    public ResponseEntity<ResponseDto> end() throws SQLException, IllegalArgumentException {
        chessService.end();
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/result")
    public String result(Model model) throws SQLException {
        chessService.end();
        model.addAttribute("play", true);
        model.addAttribute("status", chessService.status());
        model.addAttribute("board", chessService.currentBoardForUI());
        model.addAttribute("winner", chessService.findWinner());
        return "game";
    }
}
