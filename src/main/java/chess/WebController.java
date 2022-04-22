package chess;

import chess.dao.ChessGame;
import chess.domain.piece.property.Team;
import chess.dto.BoardDTO;
import chess.dto.ChessGameRoomInfoDTO;
import chess.service.ChessService;
import java.sql.SQLException;
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
        ChessGameRoomInfoDTO chessGameRoomInfoDTO = CHESS_SERVICE.findGameById(String.valueOf(id));
        model.addAttribute("chessGameRoom", chessGameRoomInfoDTO);
        return "game";
    }

    @GetMapping("/chess/game/{id}/board")
    @ResponseBody
    public ResponseEntity<BoardDTO> createBoard(@PathVariable int id) throws SQLException {
        ChessGame chessGame = CHESS_SERVICE.getChessGamePlayed(String.valueOf(id));
        return ResponseEntity.ok(new BoardDTO(chessGame));
    }

    @PostMapping("/chess/game/{id}/move")
    @ResponseBody
    public ResponseEntity<BoardDTO> movePiece(HttpServletRequest request, @PathVariable int id) throws SQLException {
        String source = request.getParameter("source");
        String target = request.getParameter("target");
        Team team = Team.valueOf(request.getParameter("team"));
        ChessGame chessGame = CHESS_SERVICE.movePiece(String.valueOf(id), source, target, team);
        return ResponseEntity.ok(new BoardDTO(chessGame));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handle(Exception exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
