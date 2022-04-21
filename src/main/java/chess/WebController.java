package chess;

import static chess.utils.Render.renderHtml;

import chess.dao.ChessGame;
import chess.domain.piece.property.Team;
import chess.dto.BoardDTO;
import chess.dto.ChessGameRoomInfoDTO;
import chess.dto.GameStatus;
import chess.service.ChessService;
import chess.utils.JsonConvertor;
import chess.utils.Render;
import java.sql.SQLException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WebController {

    private static final ChessService CHESS_SERVICE = new ChessService();

    @GetMapping("/")
    public String init(Model model) throws SQLException {
        model.addAttribute("games", CHESS_SERVICE.getGames());
        return "lobby";
    }

    @PostMapping("/chess/new")
    public String createGame(HttpServletRequest request) throws SQLException {
        String gameId = CHESS_SERVICE.addChessGame(request.getParameter("gameName"));
        return "redirect:/chess/game/"+gameId;
    }

    @GetMapping("/chess/game/{id}")
    public String temp(@PathVariable int id, Model model){
        ChessGameRoomInfoDTO ChessGameRoomInfoDTO = CHESS_SERVICE.findGameById(String.valueOf(id));
        model.addAttribute("gameId", ChessGameRoomInfoDTO.getId());
        model.addAttribute("gameName", ChessGameRoomInfoDTO.getName());
        return "game";
    }


    @GetMapping("/chess/game/{id}/board")
    @ResponseBody
    public String createBoard(@PathVariable int id, Model model) throws SQLException {
        ChessGame chessGame = CHESS_SERVICE.getChessGamePlayed(String.valueOf(id));
        Map<String, Object> boardDto = new BoardDTO(chessGame.getChessBoard()).getResult();
        String boardHtml = renderHtml(boardDto, "/board.hbs");
        model.addAttribute("board", boardHtml);
        model.addAttribute("currentTurn", chessGame.getChessBoard().getCurrentTurn());
        inputTeamScoresToModel(chessGame, model);
        return JsonConvertor.toJson(model);
    }

    @PostMapping("/chess/game/{id}/move")
    @ResponseBody
    public String movePiece(HttpServletRequest request, @PathVariable int id) throws SQLException {
        String source = request.getParameter("source");
        String target = request.getParameter("target");
        Team team = Team.valueOf(request.getParameter("team"));
        ChessGame chessGame = CHESS_SERVICE.movePiece(String.valueOf(id), source, target, team);
        final Map<String, Object> model = Render.renderBoard(chessGame);

        if (chessGame.isGameSet()) {
            Map<String, Object> result = CHESS_SERVICE.getResult(chessGame);
            model.putAll(result);
        }
        return JsonConvertor.toJson(model);
    }

    private static void inputTeamScoresToModel(ChessGame chessGame, Model model) {
        double blackScore = GameStatus.calculateTeamScore(chessGame.getChessBoard().getBoard(), Team.BLACK);
        double whiteScore = GameStatus.calculateTeamScore(chessGame.getChessBoard().getBoard(), Team.WHITE);
        model.addAttribute("currentBlackScore", blackScore);
        model.addAttribute("currentWhiteScore", whiteScore);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handle(Exception exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}