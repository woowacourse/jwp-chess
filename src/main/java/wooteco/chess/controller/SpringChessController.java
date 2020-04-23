package wooteco.chess.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import wooteco.chess.domain.game.NormalStatus;
import wooteco.chess.dto.ChessGameDto;
import wooteco.chess.dto.MovablePositionsDto;
import wooteco.chess.service.SpringChessService;
import wooteco.chess.web.JsonTransformer;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class SpringChessController {
    @Autowired
    private SpringChessService springChessService;

    @GetMapping("/")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        modelAndView.addObject("normalStatus", NormalStatus.YES.isNormalStatus());
        return modelAndView;
    }

    @GetMapping("/new")
    public ModelAndView startNewGame() throws SQLException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("chess");
        modelAndView.addObject("normalStatus", NormalStatus.YES.isNormalStatus());
        springChessService.clearHistory();
        return modelAndView;
    }

    @GetMapping("/board")
    @ResponseBody
    public String setBoard() throws SQLException {
        ChessGameDto chessGameDto = springChessService.setBoard();
        String board = JsonTransformer.toJson(chessGameDto);
        return board;
    }

    @GetMapping("/source")
    @ResponseBody
    public String getMovablePositions(@RequestParam String source) throws SQLException {
        Map<String, Object> map = new HashMap<>();
        try {
            MovablePositionsDto movablePositionsDto = springChessService.findMovablePositions(source);
            map.put("movable", movablePositionsDto.getMovablePositionNames());
            map.put("position", movablePositionsDto.getPosition());
            map.put("normalStatus", NormalStatus.YES.isNormalStatus());
            return JsonTransformer.toJson(map);
        } catch (IllegalArgumentException | UnsupportedOperationException | NullPointerException e) {
            map.put("normalStatus", NormalStatus.NO.isNormalStatus());
            map.put("exception", e.getMessage());
            return JsonTransformer.toJson(map);
        }
    }
}
