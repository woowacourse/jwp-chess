package chess.controller.gamecontroller;

import static chess.view.webview.Converter.convertToWebViewPiece;

import chess.dto.MoveRequestDto;
import chess.service.ChessGameService;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/game/{roomId}")
@Controller
public class GameWebController {

    private final ChessGameService chessGameService;

    public GameWebController(final ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @GetMapping
    public ModelAndView index(@PathVariable int roomId) {
        return render(roomId);
    }

    @PostMapping
    public ModelAndView start(@PathVariable int roomId) {
        chessGameService.start(roomId);
        return render(roomId);
    }

    @PatchMapping
    public ModelAndView move(@PathVariable int roomId, @RequestBody MoveRequestDto moveRequest) {
        final String sourcePosition = moveRequest.getSource();
        final String targetPosition = moveRequest.getTarget();
        chessGameService.move(roomId, sourcePosition, targetPosition);
        return render(roomId);
    }

    @DeleteMapping
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
