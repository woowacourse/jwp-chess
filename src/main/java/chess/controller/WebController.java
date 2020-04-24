package chess.controller;

import chess.model.domain.piece.Team;
import chess.service.ChessGameService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebController {

    @Autowired
    private ChessGameService chessGameService;

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
    public String game(@RequestParam String roomId,
        @RequestParam(defaultValue = "WHITE") String whiteName,
        @RequestParam(defaultValue = "BLACK") String blackName,
        Model model) {
        Map<Team, String> userNames = makeUserNames(whiteName, blackName);

        Integer gameId = chessGameService.saveRoom(userNames, Integer.valueOf(roomId));
        chessGameService.saveUser(userNames);

        model.addAttribute("gameId", gameId);
        return "game";
    }

    @PostMapping("/game/newGame")
    public String newGame(@RequestParam String gameId,
        @RequestParam(defaultValue = "WHITE") String whiteName,
        @RequestParam(defaultValue = "BLACK") String blackName,
        Model model) {
        Map<Team, String> userNames = makeUserNames(whiteName, blackName);
        chessGameService.closeGame(Integer.valueOf(gameId));
        model.addAttribute("gameId",
            chessGameService.createBy(Integer.valueOf(gameId), userNames));
        return "game";
    }

    @PostMapping("/continueGame")
    public String continueGame(@RequestParam String roomId,
        @RequestParam(defaultValue = "WHITE") String whiteName,
        @RequestParam(defaultValue = "BLACK") String blackName,
        Model model) {
        Map<Team, String> userNames = makeUserNames(whiteName, blackName);

        model.addAttribute("gameId",
            chessGameService.getIdBefore(Integer.valueOf(roomId), userNames));
        return "game";
    }

    @PostMapping("/game/choiceGame")
    public String choiceGame(@RequestParam String gameId, Model model) {
        model.addAttribute("roomId", chessGameService.getRoomId(Integer.valueOf(gameId)));
        return "start";
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
