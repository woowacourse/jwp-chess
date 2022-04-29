package chess.controller;

import chess.domain.board.Board;
import chess.domain.board.Position;
import chess.dto.ChessBoardDto;
import chess.dto.CreateBoardDto;
import chess.dto.ResultDto;
import chess.dto.StatusDto;
import chess.service.ChessGameService;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ChessSpringController {
    private final ChessGameService chessGameService;

    public ChessSpringController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @GetMapping("/")
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("boards", chessGameService.getBoards());
        modelAndView.setViewName("home");
        return modelAndView;
    }


    @PostMapping("/board")
    public @ResponseBody
    ResponseEntity<Integer> makeBoard(@RequestBody CreateBoardDto createBoardDto) {
        int id = chessGameService.start(createBoardDto);
        return ResponseEntity.created(URI.create("/board/" + id)).body(id);
    }

    @GetMapping("/board")
    public ModelAndView chess(@RequestParam int id) {
        ModelAndView modelAndView = new ModelAndView();
        if (chessGameService.isGameEnd(id)) {
            throw new IllegalArgumentException("이미 완료된 게임입니다");
        }
        modelAndView.addObject("boardDto", ChessBoardDto.from(chessGameService.getBoard(id)));
        modelAndView.addObject("boardId", id);
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @PutMapping("/board")
    public @ResponseBody
    ResponseEntity<String> move(@RequestBody String request, @RequestParam int id) {
        List<String> command = Arrays.asList(request.split(" "));
        Board board = getBoard(id);
        board.move(Position.from(command.get(0)), Position.from(command.get(1)));
        chessGameService.updatePosition(Position.from(command.get(0)), Position.from(command.get(1)),
                board.getTurn(), id);

        if (board.hasKingCaptured()) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }

        return ResponseEntity.ok().build();
    }

    private Board getBoard(int id) {
        return new Board(chessGameService.getBoard(id), chessGameService.getTurn(id));
    }

    @GetMapping("/board/status")
    public ModelAndView status(@RequestParam int id) {
        ModelAndView modelAndView = new ModelAndView();
        Board board = getBoard(id);
        modelAndView.addObject("status", StatusDto.of(board.scoreOfWhite(), board.scoreOfBlack()));
        modelAndView.setViewName("status");
        return modelAndView;
    }

    @GetMapping("/board/end")
    public @ResponseBody
    ResponseEntity<String> end(@RequestParam int id) {
        chessGameService.end(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/board/result")
    public ModelAndView result(@RequestParam int id) {
        ModelAndView modelAndView = new ModelAndView();
        Board board = getBoard(id);
        modelAndView.addObject("result", ResultDto.of(board.scoreOfWhite(), board.scoreOfBlack(), board.findWinner()));
        modelAndView.setViewName("result");
        return modelAndView;
    }

    @DeleteMapping("/board")
    public @ResponseBody
    ResponseEntity<String> delete(@RequestParam int id, @RequestParam String password){
        chessGameService.deleteBoard(id, password);
        return ResponseEntity.ok().build();
    }
}
