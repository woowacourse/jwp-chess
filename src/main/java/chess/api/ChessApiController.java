package chess.api;

import chess.domain.piece.Position;
import chess.dto.ChessGameDto;
import chess.dto.ChessRoomDto;
import chess.dto.ScoreDto;
import chess.service.ChessGameService;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
public class ChessApiController {

    private final ChessGameService chessGameService;

    public ChessApiController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @GetMapping("/pieces")
    public ResponseEntity<ChessGameDto> findPieces(@RequestParam String source,
                                                   @RequestParam String target,
                                                   @RequestParam long roomId) {
        Position sourcePosition = Position.parseChessPosition(source);
        Position targetPosition = Position.parseChessPosition(target);
        ChessGameDto chessGameDto = chessGameService.moveChessPiece(sourcePosition, targetPosition, roomId);
        return ResponseEntity.ok(chessGameDto);
    }

    @GetMapping("/chessgames")
    public ResponseEntity<ChessGameDto> findChessGame(@RequestParam long roomId) {
        ChessGameDto chessGame = chessGameService.findChessGame(roomId);
        return ResponseEntity.ok(chessGame);
    }

    @GetMapping("/newRoomId")
    public ResponseEntity<ChessRoomDto> newRoom(Model model) {
        ChessRoomDto chessRoomDto = chessGameService.createNewChessRoom();
        return ResponseEntity.ok(chessRoomDto);
    }

    @DeleteMapping("/chessgames")
    public ResponseEntity<ChessGameDto> endChessGame(@RequestParam long roomId) {
        ChessGameDto chessGameDto = chessGameService.endGame(roomId);
        return ResponseEntity.ok(chessGameDto);
    }

    @GetMapping("/scores")
    public ResponseEntity<ScoreDto> calculateScores(@RequestParam long roomId) {
        ScoreDto scoreDto = chessGameService.calculateScores(roomId);
        return ResponseEntity.ok(scoreDto);
    }

}
