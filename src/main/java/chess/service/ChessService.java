package chess.service;

import chess.dto.GameRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ChessService {

    private final GameService gameService;
    private final PieceService pieceService;

    public ChessService(GameService gameService, PieceService pieceService) {
        this.gameService = gameService;
        this.pieceService = pieceService;
    }

    public long initializeChess(final GameRequestDto gameRequestDto) {
        final long id = gameService.add(gameRequestDto);
        pieceService.createInitialPieces(id);
        return id;
    }
}
