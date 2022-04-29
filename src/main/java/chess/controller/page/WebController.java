package chess.controller.page;

import chess.entity.Square;
import chess.service.ChessService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WebController {

    private ChessService chessServiceV2;

    public WebController(ChessService chessServiceV2) {
        this.chessServiceV2 = chessServiceV2;
    }

    @GetMapping("/")
    public ModelAndView renderRoom() {
        return new ModelAndView("room", new HashMap<>());
    }

    @GetMapping("/room/{roomId}")
    public ModelAndView findOneRoom(@PathVariable Long roomId) {
        final List<Square> squares = chessServiceV2.findSquareAllById(roomId);
        final BoardRes boardRes = BoardRes.createBoardResToListSquare(squares);

        final Map<String, Object> model = new HashMap<>();
        model.put("board", boardRes);
        model.put("roomId", roomId);
        return new ModelAndView("board", model);
    }
}
