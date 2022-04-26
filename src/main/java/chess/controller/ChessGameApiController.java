package chess.controller;

import chess.domain.game.dto.BoardDTO;
import chess.domain.game.dto.MoveDTO;
import chess.domain.gameRoom.ChessGame;
import chess.domain.piece.property.Team;
import chess.service.ChessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ChessGameApiController {

    private final ChessService chessService;

    public ChessGameApiController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/chess/game/{id}/board")
    public ResponseEntity<BoardDTO> showBoard(@PathVariable String id) {
        ChessGame chessGame = chessService.getChessGamePlayed(id);
        return ResponseEntity.ok(new BoardDTO(chessGame));
    }

    @PostMapping("/chess/game/{id}/move")
    public ResponseEntity<BoardDTO> movePiece(MoveDTO moveDTO, @PathVariable String id) {
        ChessGame chessGame = chessService.movePiece(id, moveDTO.getSource(), moveDTO.getTarget(), Team.valueOf(moveDTO.getTeam()));
        return ResponseEntity.ok(new BoardDTO(chessGame));
    }
}
