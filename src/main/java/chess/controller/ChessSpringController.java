package chess.controller;

import chess.dto.ResultDto;
import chess.dto.RoomInfoDto;
import chess.dto.StatusDto;
import chess.service.ChessGameService;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ChessSpringController {
    private final ChessGameService chessGameService;

    public ChessSpringController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @GetMapping("/")
    public ModelAndView home() {
        final ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("roomsDto", chessGameService.getRooms());
        modelAndView.setViewName("home");
        return modelAndView;
    }

    @PostMapping("/")
    public ResponseEntity<String> create(@RequestBody RoomInfoDto roomInfoDto) {
        chessGameService.create(roomInfoDto.getTitle(), roomInfoDto.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/board")
    public ModelAndView board(@RequestParam(name = "id") Long id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView
                .addObject("boardDto", chessGameService.getBoard(id));
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @PatchMapping("/board/move")
    public ResponseEntity<Boolean> move(@RequestParam(name = "id") Long id, @RequestBody String request) {
        List<String> command = Arrays.asList(request.split(" "));
        chessGameService.move(id, command.get(0), command.get(1));
        final boolean gameEnd = chessGameService.isGameEnd(id);
        return ResponseEntity.ok().body(gameEnd);
    }

    @GetMapping("/board/chess-status")
    public ModelAndView status(@RequestParam(name = "id") Long id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView
                .addObject("status",
                        StatusDto.of(chessGameService.statusOfWhite(id), chessGameService.statusOfBlack(id)));
        modelAndView.setViewName("status");
        return modelAndView;
    }

    @GetMapping("/board/chess-result")
    public ModelAndView result(@RequestParam(name = "id") Long id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView
                .addObject("result", ResultDto
                        .of(chessGameService.statusOfWhite(id), chessGameService.statusOfBlack(id),
                                chessGameService.findWinner(id)));
        modelAndView.setViewName("result");
        return modelAndView;
    }

    @PostMapping("/board/end")
    public ResponseEntity<String> end(@RequestParam(name = "id") Long id) {
        chessGameService.end(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/board/restart")
    public ResponseEntity<String> restart(@RequestParam(name = "id") Long id) {
        chessGameService.restart(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/board")
    public ResponseEntity<String> delete(@RequestParam(name = "id") Long id, @RequestBody String request) {
        chessGameService.delete(id, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
