package wooteco.chess.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import wooteco.chess.domain.game.NormalStatus;
import wooteco.chess.domain.piece.Color;
import wooteco.chess.domain.position.MovingPosition;
import wooteco.chess.dto.ChessGameDto;
import wooteco.chess.dto.DestinationPositionDto;
import wooteco.chess.dto.MovablePositionsDto;
import wooteco.chess.dto.MoveStatusDto;
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
    public ModelAndView routeMainPage() {
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
        Map<String, Object> model = new HashMap<>();
        try {
            MovablePositionsDto movablePositionsDto = springChessService.findMovablePositions(source);
            model.put("movable", movablePositionsDto.getMovablePositionNames());
            model.put("position", movablePositionsDto.getPosition());
            model.put("normalStatus", NormalStatus.YES.isNormalStatus());
            return JsonTransformer.toJson(model);
        } catch (IllegalArgumentException | UnsupportedOperationException | NullPointerException e) {
            model.put("normalStatus", NormalStatus.NO.isNormalStatus());
            model.put("exception", e.getMessage());
            return JsonTransformer.toJson(model);
        }
    }

    @GetMapping("/destination")
    @ResponseBody
    public String getDestination(@RequestParam String destination) {
        Map<String, Object> model = new HashMap<>();

        DestinationPositionDto destinationPositionDto = springChessService.getDestinationPosition(destination);

        model.put("normalStatus", destinationPositionDto.getNormalStatus().isNormalStatus());
        model.put("position", destinationPositionDto.getPosition());

        return JsonTransformer.toJson(model);
    }

    @PostMapping("/board")
    public ModelAndView saveHistory(@RequestBody String param) throws SQLException {
        // TODO: 20. 4. 23. @RequestBody 의 값을 Map 으로 받아서 처리하는 방법 찾기
        String[] params = param.split("&");
        String source = params[0].substring(7);
        String destination = params[1].substring(12);

        ModelAndView modelAndView = new ModelAndView();

        try {
            MoveStatusDto moveStatusDto = springChessService.move(new MovingPosition(source, destination));

            modelAndView.addObject("normalStatus", moveStatusDto.getNormalStatus());
            modelAndView.addObject("winner", moveStatusDto.getWinner());

            if (moveStatusDto.getWinner().isNone()) {
                modelAndView.setViewName("chess");
                return modelAndView;
            }
            modelAndView.setViewName("result");
            return modelAndView;
        } catch (IllegalArgumentException | UnsupportedOperationException | NullPointerException | SQLException e) {
            modelAndView.addObject("normalStatus", NormalStatus.NO.isNormalStatus());
            modelAndView.addObject("exception", e.getMessage());
            modelAndView.addObject("winner", Color.NONE);
            modelAndView.setViewName("chess");
            return modelAndView;
        }
    }

    @GetMapping("/loading")
    public ModelAndView loadGame() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("chess");
        modelAndView.addObject("normalStatus", NormalStatus.YES.isNormalStatus());

        return modelAndView;
    }
}
