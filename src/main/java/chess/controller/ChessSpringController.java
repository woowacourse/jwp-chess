package chess.controller;

import chess.domain.Color;
import chess.domain.board.Position;
import chess.dto.ChessBoardDto;
import chess.dto.CreateBoardDto;
import chess.dto.MovePositionDto;
import chess.service.ChessGameService;
import java.net.URI;
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
        if (chessGameService.getTurn(id) == Color.END) {
            throw new IllegalArgumentException("이미 완료된 게임입니다");
        }
        modelAndView.addObject("boardDto", ChessBoardDto.from(chessGameService.getPieces(id)));
        modelAndView.addObject("boardId", id);
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @PutMapping("/board")
    public @ResponseBody
    ResponseEntity<String> move(@RequestBody MovePositionDto request, @RequestParam int id) {
        HttpStatus moveResult = chessGameService.move(Position.from(request.getSource()),
                Position.from(request.getTarget()), id);

        return ResponseEntity.status(moveResult).build();
    }

    @GetMapping("/board/status")
    public ModelAndView status(@RequestParam int id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("status", chessGameService.status(id));
        modelAndView.setViewName("boardStatus");
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
        modelAndView.addObject("result", chessGameService.result(id));
        modelAndView.setViewName("boardResult");
        return modelAndView;
    }

    @DeleteMapping("/board")
    public @ResponseBody
    ResponseEntity<String> delete(@RequestParam int id, @RequestParam String password) {
        chessGameService.deleteBoard(id, password);
        return ResponseEntity.ok().build();
    }
}
