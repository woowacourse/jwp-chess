package wooteco.chess.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import wooteco.chess.boot.service.ChessService;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.util.ModelParser;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Controller
public class ChessController {

    @Autowired
    private ChessService chessService;

    @GetMapping("/")
    public ModelAndView blankBoard() {
        ModelAndView modelAndView = new ModelAndView("boot_index");
        modelAndView.addAllObjects(ModelParser.parseBlankBoard());
        return modelAndView;
    }

    @PostMapping("/new_game")
    public ModelAndView newGame() throws SQLException {
        ModelAndView modelAndView = new ModelAndView("boot_index");
        Map<String, Object> objects = ModelParser.parseBoard(chessService.newGame());
        modelAndView.addAllObjects(objects);
        return modelAndView;
    }

    @GetMapping("/load_game")
    public ModelAndView loadGame() throws SQLException {
        ModelAndView modelAndView = new ModelAndView("boot_index");
        Map<String, Object> objects = ModelParser.parseBoard(chessService.readBoard());
        modelAndView.addAllObjects(objects);
        return modelAndView;
    }

    @GetMapping("/show_movable")
    public ModelAndView showMovable(@RequestParam(defaultValue = "") String start) throws SQLException {
        ModelAndView modelAndView = new ModelAndView("boot_index");
        modelAndView.addAllObjects(parseMovablePositionsModel(start));
        return modelAndView;
    }

    private Map<String, Object> parseMovablePositionsModel(String start) throws SQLException {
        List<Position> movablePositions = chessService.findMovablePlaces(Position.of(start));
        Map<String, Object> objects = ModelParser.parseBoard(chessService.readBoard(), movablePositions);

        if (movablePositions.size() != 0) {
            objects.put("start", start);
        }
        return objects;
    }

    @PostMapping("/move")
    public ModelAndView move(@RequestParam(defaultValue = "") String start, @RequestParam(defaultValue = "") String end) throws SQLException {
        ModelAndView modelAndView = new ModelAndView("boot_index");

        chessService.move(Position.of(start), Position.of(end));
        modelAndView.addAllObjects(ModelParser.parseBoard(chessService.readBoard()));
        return modelAndView;
    }

    @GetMapping("/score")
    public ModelAndView score() throws SQLException {
        ModelAndView modelAndView = new ModelAndView("boot_index");
        modelAndView.addAllObjects(ModelParser.parseBoard(chessService.readBoard()));
        modelAndView.addObject("player1_info", "WHITE: " + chessService.calculateScore(Team.WHITE));
        modelAndView.addObject("player2_info", "BLACK: " + chessService.calculateScore(Team.BLACK));
        return modelAndView;
    }
}
