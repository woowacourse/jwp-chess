package chess.controller;

import chess.domain.game.BoardInitializer;
import chess.domain.game.BoardEntity;
import chess.domain.member.Member;
import chess.domain.pieces.Color;
import chess.dto.RequestDto;
import chess.service.GameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class ChessController {

    private final GameService gameService;

    public ChessController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/")
    public ModelAndView index(Model model) {
        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("boards", gameService.getRooms());
        return modelAndView;
    }

    @PostMapping("/room")
    public String createRoom(@ModelAttribute RequestDto requestDto) {
        final BoardEntity board = new BoardEntity(requestDto.getTitle(), Color.WHITE,
                List.of(new Member(requestDto.getFirstMemberName()), new Member(requestDto.getSecondMemberName())), requestDto.getPassword());
        int roomId = gameService.saveBoard(board, new BoardInitializer()).getId();
        return "redirect:/room/" + roomId;
    }

    @GetMapping("/room/{roomId}")
    public ModelAndView showRoom(@PathVariable("roomId") int id) {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("roomId", id);
        modelAndView.addObject("board", gameService.getBoard(id));
        return modelAndView;
    }

}
