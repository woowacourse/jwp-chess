package chess.controller;

import chess.dto.ResponseDto;
import chess.dto.ResultDto;
import chess.dto.RoomInfoDto;
import chess.dto.StatusDto;
import chess.service.ChessGameService;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
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
        final ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("roomsDto", chessGameService.getRooms());
        modelAndView.setViewName("home");
        return modelAndView;
    }

    @PostMapping("/create")
    @ResponseBody
    ResponseDto create(@RequestBody RoomInfoDto roomInfoDto) {
        return chessGameService.create(roomInfoDto.getTitle(), roomInfoDto.getPassword());
    }

    @GetMapping("/board")
    public ModelAndView board(@RequestParam(name = "id") Long id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView
                .addObject("boardDto", chessGameService.getBoard(id));
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @PostMapping("/board/move")
    @ResponseBody
    public ResponseDto move(@RequestParam(name = "id") Long id, @RequestBody String request) {
        List<String> command = Arrays.asList(request.split(" "));
        return chessGameService.move(id, command.get(0), command.get(1));
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
    @ResponseBody
    public ResponseDto end(@RequestParam(name = "id") Long id) {
        return chessGameService.end(id);
    }

    @PostMapping("/board/restart")
    @ResponseBody
    public ResponseDto restart(@RequestParam(name = "id") Long id) {
        return chessGameService.restart(id);
    }

    @DeleteMapping("/board/delete")
    @ResponseBody
    public ResponseDto delete(@RequestParam(name = "id") Long id, @RequestBody String request) {
        return chessGameService.delete(id, request);
    }
}
