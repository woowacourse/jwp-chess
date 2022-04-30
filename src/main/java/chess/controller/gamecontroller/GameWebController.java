package chess.controller.gamecontroller;

import static chess.view.webview.Converter.convertToWebViewPiece;

import chess.dto.MoveRequestDto;
import chess.service.ChessGameService;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class GameWebController {

    private final ChessGameService chessGameService;

    public GameWebController(final ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @GetMapping(path = "/{roomId}")
    public ModelAndView index(@PathVariable("roomId") int roomId) {
        return render(roomId);
    }

    @PostMapping(path = "/{roomId}")
    public ModelAndView start(@PathVariable("roomId") int roomId) {
        chessGameService.start(roomId);
        return render(roomId);
    }

    @PutMapping(path = "/{roomId}")
    public ModelAndView move(@PathVariable int roomId, @RequestBody MoveRequestDto moveRequest) {
        final String sourcePosition = moveRequest.getSource();
        final String targetPosition = moveRequest.getTarget();
        chessGameService.move(roomId, sourcePosition, targetPosition);
        return render(roomId);
    }

    @DeleteMapping(path = "/{roomId}")
    public ModelAndView end(@PathVariable int roomId) {
        chessGameService.end(roomId);
        return render(roomId);
    }

    private ModelAndView render(final int roomId) {
        final ModelAndView model = new ModelAndView("game");
        final Map<String, Object> pieces = convertToWebViewPiece(chessGameService.getPieces(roomId));
        model.addAllObjects(pieces);
        model.addObject("roomId", roomId);
        return model;
    }
}
