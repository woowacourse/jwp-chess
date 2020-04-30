package wooteco.chess.controller;

import chess.domain.GameResult;
import chess.domain.board.ChessBoard;
import chess.dto.CellManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import wooteco.chess.entity.RoomEntity;
import wooteco.chess.service.ChessGameService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Controller
public class ChessController {
    @Autowired
    private ChessGameService chessGameService;

    @GetMapping("/")
    public ModelAndView index() {
        Map<String, Object> model = new HashMap<>();
        List<RoomEntity> roomEntities = chessGameService.loadRooms();
        model.put("rooms", roomEntities);
        return new ModelAndView("index", model);
    }

    @PostMapping("/create-room")
    public String createRoom(@RequestParam(defaultValue = "") String name) {
        RoomEntity roomEntity = chessGameService.createRoom(name);
        return String.format("redirect:/room/%s", roomEntity.getId());
    }

    @GetMapping("/{roomId}")
    public ModelAndView initializeRoom(@PathVariable String roomId) {
        Map<String, Object> model = new HashMap<>();
        model.put("roomId", roomId);
        return new ModelAndView("game", model);
    }

    @GetMapping("/load-game")
    public ModelAndView loadChessGame() {
        Map<String, Object> model = new HashMap<>();
        try {
            // todo: roomId를 수정해야 함
            model = createCurrentGameModel(chessGameService.loadBoard(), 1L);
        } catch (NoSuchElementException e) {
            model.put("error", "새 게임을 눌러주세요!");
        }
        return new ModelAndView("game", model);
    }

    @GetMapping("/{roomId}/new")
    public ModelAndView newChessGame(@PathVariable Long roomId) {
        ChessBoard chessBoard = chessGameService.createNewChessGame(roomId);
        Map<String, Object> model = createCurrentGameModel(chessBoard, roomId);
        return new ModelAndView("game", model);
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
            // todo: roomId를 수정해야 함
            chessBoard = chessGameService.movePiece(source, target);
            model = createCurrentGameModel(chessBoard, 1L);
        } catch (IllegalArgumentException e) {
            // todo: roomId를 수정해야 함
            chessBoard = chessGameService.loadBoard();
            model = createCurrentGameModel(chessBoard, 1L);
            model.put("error", e.getMessage());
        }

        if (chessBoard.isGameOver()) {
            return new ModelAndView("redirect:/winner");
        }
        return new ModelAndView("game", model);
    }

    private Map<String, Object> createCurrentGameModel(ChessBoard chessBoard, Long roomId) {
        Map<String, Object> model = new HashMap<>();
        GameResult gameResult = chessBoard.createGameResult();
        CellManager cellManager = new CellManager();

        model.put("cells", cellManager.createCells(chessBoard));
        model.put("currentTeam", chessBoard.getTeam().getName());
        model.put("blackScore", gameResult.getAliveBlackPieceScoreSum());
        model.put("whiteScore", gameResult.getAliveWhitePieceScoreSum());
        model.put("roomId", roomId);

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
