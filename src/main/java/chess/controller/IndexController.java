package chess.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import chess.controller.dto.ControllerDtoAssembler;
import chess.controller.dto.response.GameStatusResponse;
import chess.service.ChessService;

@Controller
public class IndexController {

    private final ChessService chessService;

    public IndexController(final ChessService chessService) {
        this.chessService = chessService;
    }

    @RequestMapping("/")
    public String index(final Model model) {
        final List<GameStatusResponse> games = chessService.listGames()
                .stream()
                .map(ControllerDtoAssembler::gameStatusResponse)
                .collect(Collectors.toUnmodifiableList());
        model.addAttribute("games", games);
        return "index";
    }
}