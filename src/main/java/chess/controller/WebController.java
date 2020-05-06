package chess.controller;

import chess.model.domain.piece.Team;
import chess.model.repository.ChessGameEntity;
import chess.service.ChessGameService;
import chess.service.ResultService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebController {

    private final ChessGameService chessGameService;
    private final ResultService resultService;

    public WebController(
        ChessGameService chessGameService, ResultService resultService) {
        this.chessGameService = chessGameService;
        this.resultService = resultService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/start")
    public String start(@RequestParam String roomId, Model model) {
        model.addAttribute("roomId", roomId);
        return "start";
    }

    @PostMapping("/game")
    public String game(@RequestParam Map<String, String> req, Model model) {
        Integer roomId = Integer.valueOf(req.get("roomId"));
        chessGameService.findProceedGameId(roomId).ifPresent(chessGameService::closeGame);

        Map<Team, String> userNames = makeUserNames(req);
        Integer gameId = chessGameService.saveNewGameInfo(userNames, roomId);
        chessGameService.saveNewUserNames(userNames);

        model.addAttribute("gameId", gameId);
        return "game";
    }

    @PostMapping("/load")
    public String loadGame(@RequestParam Map<String, String> req, Model model) {
        Integer roomId = Integer.valueOf(req.get("roomId"));
        model.addAttribute("gameId",
            chessGameService.findProceedGameId(roomId)
                .orElseGet(() -> chessGameService
                    .create(roomId, makeUserNames(req))));
        return "game";
    }

    @PostMapping("/game/new")
    public String newGame(@RequestParam Map<String, String> req, Model model) {
        Integer gameId = Integer.valueOf(req.get("gameId"));
        Map<Team, String> userNames = makeUserNames(req);
        if (chessGameService.isGameProceed(gameId)) {
            ChessGameEntity chessGameEntity = chessGameService.closeGame(gameId);
            resultService.setGameResult(chessGameEntity);
        }
        model.addAttribute("gameId", chessGameService.createBy(gameId, userNames));
        return "game";
    }

    @PostMapping("/game/exit")
    public String exitGame() {
        return "index";
    }

    @PostMapping("/result")
    public String result() {
        return "result";
    }

    private Map<Team, String> makeUserNames(Map<String, String> req) {
        Map<Team, String> userNames = new HashMap<>();
        userNames.put(Team.BLACK, req.get("blackName"));
        userNames.put(Team.WHITE, req.get("whiteName"));
        return userNames;
    }
}
