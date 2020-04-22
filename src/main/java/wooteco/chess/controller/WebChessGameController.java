package wooteco.chess.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import wooteco.chess.dao.ChessGameDao;
import wooteco.chess.domain.piece.Position;
import wooteco.chess.service.ChessService;

import java.sql.SQLException;
import java.util.Map;

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
    public String renderGamePage(Model model, @PathVariable String id) throws SQLException {
        if (chessGameDao.selectAll().contains(Integer.parseInt(id))) {
            model.addAttribute("id", id);
            return "game";
        }
        return "<script>location.replace('/')</script>";
    }

    @GetMapping("/")
    public String renderIndexPage(Model model) {
        return "index";
    }

    @PostMapping("/create")
    @ResponseBody
    public String createChessRoom(Model model) throws SQLException {
        return GSON.toJson(chessService.createChessRoom());
    }

    @PostMapping("/restart/{id}")
    @ResponseBody
    public String restartGame(Model model, @PathVariable String id) throws SQLException {
        return GSON.toJson(chessService.restartGame(Integer.parseInt(id)));
    }

    @PostMapping("/move/{id}")
    @ResponseBody
    public String movePiece(Model model, @PathVariable String id, @RequestBody Map<String, Double> req) throws SQLException {
        int pieceId = Integer.parseInt(id);
        // TODO: 2020/04/22 Map을 별도의 DTO 클래스로 대체할 수 있을지 고려!!
        Position source = Position.of(req.get("sx").intValue(), req.get("sy").intValue());
        Position target = Position.of(req.get("tx").intValue(), req.get("ty").intValue());
        return GSON.toJson(chessService.movePiece(pieceId, source, target));
    }

    @GetMapping("/board/{id}")
    @ResponseBody
    public String getChessGameById(Model model, @PathVariable String id) throws SQLException {
        return GSON.toJson(chessService.getChessGameById(Integer.parseInt(id)));
    }

    @GetMapping("/games")
    @ResponseBody
    public String getGameList(Model model) throws SQLException {
        return GSON.toJson(chessService.getGameList());
    }
}
