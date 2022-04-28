package chess.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import chess.domain.Command;
import chess.domain.piece.Team;
import chess.dto.ChessGameDto;
import chess.dto.PieceDto;
import chess.service.ChessService;

@Controller
public class SpringController {
    private final ChessService chessService;

    public SpringController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String home(Model model, @RequestParam(required = false) String error) {
        List<ChessGameDto> chessGameDtos = chessService.findAllChessGames();
        model.addAttribute("rooms", chessGameDtos);
        model.addAttribute("error", error);
        return "index";
    }

    @PostMapping("/save")
    public String save(@RequestParam String gameName, @RequestParam String password) {
        chessService.save(gameName, password);
        return "redirect:/";
    }

    @PostMapping("/delete/{chessGameId}")
    public String delete(
        @RequestParam String password,
        @PathVariable int chessGameId,
        RedirectAttributes redirectAttributes) {
        try {
            chessService.delete(password, chessGameId);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addAttribute("error", e.getMessage());
        }
        return "redirect:/";
    }


    @GetMapping("/game/{chessGameId}")
    public String game(
        @PathVariable int chessGameId,
        @RequestParam(value = "error", required = false) String error,
        Model model) {
        List<String> chessBoard = chessService.findChessBoardById(chessGameId);
        model.addAttribute("chessboard", chessBoard);
        List<PieceDto> pieceDtos = chessService.getPieces(chessGameId);
        model.addAttribute("pieces", pieceDtos);
        model.addAttribute("chessGameId", chessGameId);
        model.addAttribute("error", error);

        return "chess";
    }

    @PostMapping("/game/{chessGameId}/move")
    public String move(
        @PathVariable int chessGameId,
        @RequestParam("from") String from,
        @RequestParam("to") String to,
        Model model,
        RedirectAttributes redirectAttributes) throws UnsupportedEncodingException {
        try {
            chessService.move(from, to, chessGameId);
            if (chessService.isEnd(chessGameId)) {
                return "redirect:/game/" + chessGameId + "/end";
            }
        } catch (IllegalArgumentException e) {
            redirectAttributes.addAttribute("error", e.getMessage());
        }

        model.addAttribute("chessGameId", chessGameId);
        return "redirect:/game/" + chessGameId;
    }

    @GetMapping("/game/{chessGameId}/end")
    public String end(@PathVariable int chessGameId, Model model) {
        String winTeamName = chessService.finish(Command.from("end"), chessGameId);
        List<PieceDto> pieceDtos = chessService.getPieces(chessGameId);

        model.addAttribute("winTeam", winTeamName);
        model.addAttribute("chessGameId", chessGameId);
        model.addAttribute("pieces", pieceDtos);

        return "chess";
    }

    @GetMapping("/game/{chessGameId}/status")
    public String status(@PathVariable int chessGameId, Model model) {
        Map<Team, Double> score = chessService.getScore(chessGameId);
        List<PieceDto> pieceDtos = chessService.getPieces(chessGameId);

        model.addAttribute("blackScore", score.get(Team.BLACK));
        model.addAttribute("whiteScore", score.get(Team.WHITE));
        model.addAttribute("chessGameId", chessGameId);
        model.addAttribute("pieces", pieceDtos);

        return "chess";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }
}
