package chess.controller;

import chess.dto.CreateBoardDto;
import chess.dto.ResultDto;
import chess.dto.StatusDto;
import chess.service.ChessGameService;
import chess.service.ResponseCode;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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


    @PostMapping("/make-board")
    public String makeBoard(String name, String password) {
        int id = chessGameService.makeBoard(new CreateBoardDto(name, password));
        chessGameService.start(id);

        return "redirect:chess/?id=" + id;
    }

    @GetMapping("/chess")
    public ModelAndView chess(@RequestParam int id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("boardDto", chessGameService.getBoard());
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @PostMapping("/move")
    public @ResponseBody
    ResponseEntity<String> move(@RequestBody String request) {
        List<String> command = Arrays.asList(request.split(" "));
        ResponseCode code = chessGameService.move(command.get(0), command.get(1), 1);

        return ResponseEntity.status(code.getHttpStatus()).body("");
    }

    @GetMapping("/chess-status")
    public ModelAndView status() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView
                .addObject("status", StatusDto.of(chessGameService.statusOfWhite(), chessGameService.statusOfBlack()));
        modelAndView.setViewName("status");
        return modelAndView;
    }

    @GetMapping("/end")
    public @ResponseBody
    ResponseEntity<String> end(@RequestParam int id) {
        chessGameService.end(id);
        return ResponseEntity.ok().body("");
    }

    @GetMapping("/chess-result")
    public ModelAndView result() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("result", ResultDto
                .of(chessGameService.statusOfWhite(), chessGameService.statusOfBlack(), chessGameService.findWinner()));
        modelAndView.setViewName("result");
        return modelAndView;
    }
}
