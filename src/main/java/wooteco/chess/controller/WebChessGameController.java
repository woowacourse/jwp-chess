package wooteco.chess.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import wooteco.chess.dao.ChessGameDao;
import wooteco.chess.domain.piece.Position;
import wooteco.chess.dto.MoveOperationDto;
import wooteco.chess.service.ChessService;

@Controller
public class WebChessGameController {
    private static final Gson GSON = new GsonBuilder().create();
    private static final ChessGameDao chessGameDao = new ChessGameDao();
    private ChessService chessService;

    @Autowired
    public WebChessGameController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/game/{id}")
    public String renderGamePage(Model model, @PathVariable String id) {
        if (chessGameDao.selectAll().contains(Integer.parseInt(id))) {
            model.addAttribute("id", id);
            return "game";
        }
        return "index";
    }

    @GetMapping("/")
    public String renderIndexPage(Model model) {
        return "index";
    }

    @PostMapping("/create")
    @ResponseBody
    public String createChessRoom(Model model) {
        return GSON.toJson(chessService.createChessRoom());
    }

    @PostMapping("/restart/{id}")
    @ResponseBody
    public String restartGame(Model model, @PathVariable String id) {
        return GSON.toJson(chessService.restartGame(Integer.parseInt(id)));
    }

    @PostMapping("/move/{id}")
    @ResponseBody
    public String movePiece(Model model, @PathVariable String id, @RequestBody MoveOperationDto moveOperationDto) {
        int pieceId = Integer.parseInt(id);
        Position source = Position.of(moveOperationDto.getStartX(), moveOperationDto.getStartY());
        Position target = Position.of(moveOperationDto.getTargetX(), moveOperationDto.getTargetY());
        return GSON.toJson(chessService.movePiece(pieceId, source, target));
    }

    @GetMapping("/board/{id}")
    @ResponseBody
    public String getChessGameById(Model model, @PathVariable String id) {
        return GSON.toJson(chessService.getChessGameById(Integer.parseInt(id)));
    }

    @GetMapping("/games")
    @ResponseBody
    public String getGameList(Model model) {
        return GSON.toJson(chessService.getGameList());
    }
}
