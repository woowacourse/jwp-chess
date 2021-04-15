package chess.service;

import chess.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class ChessService {

    private final GameService gameService;
    private final PieceService pieceService;
    private final UserService userService;

    public ChessService(GameService gameService, PieceService pieceService, UserService userService) {
        this.gameService = gameService;
        this.pieceService = pieceService;
        this.userService = userService;
    }

    public long initializeChess(final GameRequestDto gameRequestDto) {
        final long id = gameService.add(gameRequestDto);
        pieceService.createInitialPieces(id);
        return id;
    }

    public ChessResponseDto bringGameData(long gameId) {
        List<PieceDto> pieceDtos = pieceService.findPiecesByGameId(gameId);
        GameResponseDto gameResponseDto = gameService.findById(gameId);
        UserResponseDto host = userService.findUserById(gameResponseDto.getHostId());
        UserResponseDto guest = userService.findUserById(gameResponseDto.getGuestId());
        return new ChessResponseDto(pieceDtos, host, guest, gameResponseDto);
    }
}
