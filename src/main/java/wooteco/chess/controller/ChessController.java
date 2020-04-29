package wooteco.chess.controller;

import chess.domain.GameResult;
import chess.domain.board.ChessBoard;
import chess.dto.CellManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import wooteco.chess.service.ChessGameService;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Controller
public class ChessController {
    @Autowired
    private ChessGameService chessGameService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/load-game")
    public ModelAndView loadChessGame() {
        Map<String, Object> model = new HashMap<>();
        try {
            model = createCurrentGameModel(chessGameService.loadBoard());
        } catch (NoSuchElementException e) {
            model.put("error", "새 게임을 눌러주세요!");
        }
        return new ModelAndView("index", model);
    }

    @GetMapping("/new-chess-game")
    public ModelAndView newChessGame() {
        ChessBoard chessBoard = chessGameService.createNewChessGame();
        Map<String, Object> model = createCurrentGameModel(chessBoard);
        return new ModelAndView("index", model);
    }

    @GetMapping("/winner")
    public ModelAndView winner() {
        if (chessGameService.isNotGameOver()) {
            return new ModelAndView("redirect:/");
        }

        GameResult gameResult = chessGameService.findWinner();
        Map<String, Object> model = createWinnerModel(gameResult);
        return new ModelAndView("winner", model);
    }

    @PostMapping("/move")
    public ModelAndView winner(@RequestParam(defaultValue = "") String source,
                               @RequestParam(defaultValue = "") String target) {
        ChessBoard chessBoard;
        Map<String, Object> model;
        try {
            chessBoard = chessGameService.movePiece(source, target);
            model = createCurrentGameModel(chessBoard);
        } catch (IllegalArgumentException e) {
            chessBoard = chessGameService.loadBoard();
            model = createCurrentGameModel(chessBoard);
            model.put("error", e.getMessage());
        }

        if (chessBoard.isGameOver()) {
            return new ModelAndView("redirect:/winner");
        }
        return new ModelAndView("index", model);
    }

    private Map<String, Object> createCurrentGameModel(ChessBoard chessBoard) {
        Map<String, Object> model = new HashMap<>();
        GameResult gameResult = chessBoard.createGameResult();
        CellManager cellManager = new CellManager();

        model.put("cells", cellManager.createCells(chessBoard));
        model.put("currentTeam", chessBoard.getTeam().getName());
        model.put("blackScore", gameResult.getAliveBlackPieceScoreSum());
        model.put("whiteScore", gameResult.getAliveWhitePieceScoreSum());

        return model;
    }

    private Map<String, Object> createWinnerModel(GameResult gameResult) {
        Map<String, Object> model = new HashMap<>();
        model.put("winner", gameResult.getWinner());
        model.put("loser", gameResult.getLoser());
        model.put("blackScore", gameResult.getAliveBlackPieceScoreSum());
        model.put("whiteScore", gameResult.getAliveWhitePieceScoreSum());

        return model;
    }
}
