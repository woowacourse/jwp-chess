package wooteco.chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wooteco.chess.domain.Scores;
import wooteco.chess.service.BoardService;
import wooteco.chess.view.OutputView;

@Controller
public class SpringChessBoardController {
    private final BoardService boardService;

    public SpringChessBoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/rooms/{roomId}")
    public String loadBoard(@PathVariable Long roomId, Model model) {
        OutputView.constructModel(roomId, boardService.loadBoard(roomId), model);
        return "board";
    }

    @PostMapping("/rooms/{roomId}")
    public String move(@PathVariable Long roomId, @RequestParam String source, @RequestParam String target, Model model) {
        try {
            boardService.play(roomId, source, target);
        } catch (RuntimeException e) {
            model.addAttribute("error-message", e.getMessage());
            OutputView.constructModel(roomId, boardService.loadBoard(roomId), model);
        }
        if (boardService.isFinished(roomId)) {
            model.addAttribute("id", roomId);
            model.addAttribute("winner", boardService.isTurnWhite(roomId) ? "흑팀" : "백팀");
            return "result";
        }
        OutputView.constructModel(roomId, boardService.loadBoard(roomId), model);
        return "board";
    }

    @PostMapping("/newgame/{roomId}")
    public String newGame(@PathVariable Long roomId) {
        boardService.init(roomId);
        return "redirect:/rooms/" + roomId;
    }

    @GetMapping("/scores/{roomId}")
    public String scores(@PathVariable Long roomId, Model model) {
        model.addAttribute("id", roomId);
        model.addAttribute("scores", Scores.calculateScores(boardService.loadBoard(roomId)));
        return "scores";
    }

}
