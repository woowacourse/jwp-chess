package chess.controller;

import chess.domain.position.Position;
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
    private static final String PASSWORD_SEND_URL = "password_send";
    private static final String REDIRECT_MATCH = "redirect:/match";
    private static final String REDIRECT_INDEX = "redirect:/";
    private static final String NOT_MATCH = "not_match";
    private static final String MATCH_URL = "match_password";
    private static final String REDIRECT_PASSWORD = "redirect:/sendPassword";
    private static final String REDIRECT_INDEX_OR_RUN = "redirect:/indexOrRun";
    private static final String SAVE_DONE_URL = "save_done";

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
        model.addAttribute("names", allNames.toArray());
        return SEARCH_END_URL;
    }

    @PostMapping(value = "/newGame")
    public String generateNewGame(Model model, ChessForm chessForm) {
        try {
            webChessGame.validateDuplicateName(chessForm);
            webChessGame.initializeGame(boardDTO, chessForm);
            webChessGame.saveBoard(chessForm.getRoomName());
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
            if (webChessGame.isKingDead()) {
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

    @PostMapping(value = "/gameSave")
    public String gameSave(ChessForm chessForm, Model model) {
        webChessGame.saveBoard(chessForm.getRoomName());
        model.addAttribute("roomName", chessForm.getRoomName());
        updateDTO(model);
        return SAVE_DONE_URL;
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

    @PostMapping(value = "/sendPassword")
    public String checkPassword(ChessForm chessForm, Model model) {
        if (webChessGame.checkPassword(chessForm.getRoomName(), chessForm.getPassword())) {
            return REDIRECT_MATCH + "?roomName=" + chessForm.getRoomName();
        }
        model.addAttribute("roomName", chessForm.getRoomName());
        model.addAttribute("message", "비밀번호가 틀렸습니다.");
        return NOT_MATCH;
    }

    @PostMapping("/redirectPassword")
    public String redirectPassword(ChessForm chessForm) {
        return REDIRECT_PASSWORD + "?roomName=" + chessForm.getRoomName();
    }

    @GetMapping(value = "/sendPassword", params = "roomName")
    public String checkPassword(@RequestParam("roomName") String name, Model model) {
        model.addAttribute("roomName", name);
        if (webChessGame.isEndedGame(name)) {
            webChessGame.getEndGameBoard(name, boardDTO);
            updateDTO(model);
            model.addAttribute("passwordtype", "을 삭제");
            return PASSWORD_SEND_URL;
        }
        model.addAttribute("passwordtype", "에 참여");
        return PASSWORD_SEND_URL;
    }

    @GetMapping(value = "/match", params = "roomName")
    public String displayMatch(@RequestParam("roomName") String name, Model model) {
        model.addAttribute("result", webChessGame.getMessageByPassWord(name));
        model.addAttribute("roomName", name);
        return MATCH_URL;
    }

    @PostMapping(value = "redirectIndexOrRun")
    public String redirectIndexOrRun(ChessForm chessForm) {
        return REDIRECT_INDEX_OR_RUN + "?roomName=" + chessForm.getRoomName();
    }

    @GetMapping(value = "/indexOrRun", params = "roomName")
    public String redirectByCondition(@RequestParam("roomName") String name) {
        if (webChessGame.isEndedGame(name)) {
            webChessGame.deleteRoom(name);
            return REDIRECT_INDEX;
        }
        webChessGame.loadBoard(name, boardDTO);
        return REDIRECT_GAME_RUN + "?roomName=" + name;
    }
}
