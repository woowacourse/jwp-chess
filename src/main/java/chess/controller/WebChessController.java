package chess.controller;

import chess.web.BoardDTO;
import chess.web.ChessForm;
import chess.web.MoveForm;
import chess.web.WebChessGame;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Set;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebChessController {

    private static final String NEW_GAME_URL = "new_game_generate";
    private static final String RUN_EXCEPTION_URL = "game_run_exception";
    private static final String INDEX_URL = "index";
    private static final String FINISHED_URL = "game_end";
    private static final String EXCEPTION = "exception";
    private static final String REDIRECT_GAME_RUN = "redirect:/gameRun";
    private static final String GAME_RUN_URL = "game_run";
    private static final String NEW_GAME_EXCEPTION_URL = "new_game_exception";
    private static final String SCORE_URL = "game_score";
    private static final String REDIRECT_GAME_END = "redirect:/gameEnd";
    private static final String SEARCH_GAME_URL = "search_save";
    private static final String SEARCH_END_URL = "search_end";

    private final BoardDTO boardDTO = BoardDTO.buildModel();
    private final WebChessGame webChessGame;

    @Autowired
    public WebChessController(WebChessGame webChessGame) {
        this.webChessGame = webChessGame;
    }

    @GetMapping("/")
    public String inputGameID() {
        return INDEX_URL;
    }

    @GetMapping(value = "/newGame")
    public String newGame() {
        return NEW_GAME_URL;
    }

    @PostMapping(value = "/")
    public String redirectMain() {
        return INDEX_URL;
    }

    @GetMapping(value = "/searchSave")
    public String searchSave(Model model) {
        List<String> allNames = webChessGame.findAllSavedGame();
        model.addAttribute("names", allNames);
        return SEARCH_GAME_URL;
    }

    @GetMapping(value = "/searchEnd")
    public String searchEnd(Model model) {
        List<String> allNames = webChessGame.findAllEndedGame();
        model.addAttribute("names", allNames);
        return SEARCH_END_URL;
    }

    @PostMapping(value = "/newGame")
    public String generateNewGame(Model model, ChessForm chessForm) {
        try {
            webChessGame.validateDuplicateName(chessForm);
            webChessGame.initializeGame(boardDTO, chessForm);
            return REDIRECT_GAME_RUN + "?roomName=" + chessForm.getRoomName();
        }
        catch (IllegalArgumentException exception) {
            model.addAttribute(EXCEPTION, exception.getMessage());
            return NEW_GAME_EXCEPTION_URL;
        }
    }

    @GetMapping(value = "/gameRun", params = "roomName")
    public String runGame(Model model, @RequestParam("roomName") String roomName) {
        model.addAttribute("roomName", roomName);
        model.addAttribute("color", webChessGame.getColor(roomName));
        updateDTO(model);
        return GAME_RUN_URL;
    }

    @PostMapping(value = "/gameRun")
    public String runGame(ChessForm chessForm, MoveForm moveForm, Model model) {
        model.addAttribute("roomName", chessForm.getRoomName());
        try {
            webChessGame.executeOneTurn(chessForm, moveForm, boardDTO);
            updateDTO(model);
            model.addAttribute("color", webChessGame.getColor(chessForm.getRoomName()));
            if (webChessGame.isKingDead(chessForm)) {
                return REDIRECT_GAME_END + "?roomName=" + chessForm.getRoomName();
            }
            return GAME_RUN_URL;
        } catch (IllegalArgumentException exception) {
            model.addAttribute(EXCEPTION, exception.getMessage());
            updateDTO(model);
            return RUN_EXCEPTION_URL;
        }
    }

    private void updateDTO(Model model) {
        Set<String> keys = boardDTO.getData().keySet();
        for (String key : keys) {
            model.addAttribute(key, boardDTO.getData().get(key));
        }
    }

    @PostMapping(value = "/gameScore")
    public String gameScore(ChessForm chessForm, Model model) {
        model.addAttribute("roomName", chessForm.getRoomName());
        model.addAttribute("whiteScore", webChessGame.calculateScore().getWhiteScore());
        model.addAttribute("blackScore", webChessGame.calculateScore().getBlackScore());
        updateDTO(model);
        return SCORE_URL;
    }

    @PostMapping(value = "/gameEnd")
    public String gameEnd(ChessForm chessForm, Model model) {
        model.addAttribute("roomName", chessForm.getRoomName());
        webChessGame.terminateState(chessForm.getRoomName());
        updateDTO(model);
        return FINISHED_URL;
    }

    @GetMapping(value = "/gameEnd", params = "roomName")
    public String gameEnd(@RequestParam("roomName") String name, Model model) {
        model.addAttribute("roomName", name);
        webChessGame.terminateState(name);
        updateDTO(model);
        return FINISHED_URL;
    }


}
