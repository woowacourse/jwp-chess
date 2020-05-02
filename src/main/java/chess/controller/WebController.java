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
    public String game(@RequestParam Integer roomId,
        @RequestParam(defaultValue = "WHITE") String whiteName,
        @RequestParam(defaultValue = "BLACK") String blackName,
        Model model) {
        chessGameService.findProceedGameId(roomId).ifPresent(chessGameService::closeGame);

        Map<Team, String> userNames = makeUserNames(whiteName, blackName);
        Integer gameId = chessGameService.saveNewGameInfo(userNames, roomId);
        chessGameService.saveNewUserNames(userNames);

        model.addAttribute("gameId", gameId);
        return "game";
    }

    @PostMapping("/load")
    public String continueGame(@RequestParam Integer roomId,
        @RequestParam(defaultValue = "WHITE") String whiteName,
        @RequestParam(defaultValue = "BLACK") String blackName,
        Model model) {
        model.addAttribute("gameId",
            chessGameService.findProceedGameId(roomId)
                .orElseGet(() -> chessGameService
                    .create(roomId, makeUserNames(whiteName, blackName))));
        return "game";
    }

    @PostMapping("/game/new")
    public String newGame(@RequestParam Integer gameId,
        @RequestParam(defaultValue = "WHITE") String whiteName,
        @RequestParam(defaultValue = "BLACK") String blackName,
        Model model) {
        Map<Team, String> userNames = makeUserNames(whiteName, blackName);
        if (chessGameService.isGameProceed(gameId)) {
            ChessGameEntity chessGameEntity = chessGameService.closeGame(gameId);
            resultService.setGameResult(chessGameEntity);
        }
        model.addAttribute("gameId", chessGameService.createBy(gameId, userNames));
        return "game";
    }

    @PostMapping("/game/exit")
    public String choiceGame() {
        return "index";
    }

    @PostMapping("/result")
    public String result() {
        return "result";
    }

    private Map<Team, String> makeUserNames(String whiteName, String blackName) {
        Map<Team, String> userNames = new HashMap<>();
        userNames.put(Team.BLACK, blackName);
        userNames.put(Team.WHITE, whiteName);
        return userNames;
    }
}
