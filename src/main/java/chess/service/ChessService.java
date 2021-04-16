package chess.service;

import chess.domain.board.Board;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceFactory;
import chess.domain.piece.Position;
import chess.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    public boolean checkMovement(long gameId, MoveRequestDto moveRequestDto) {
        Board board = generateBoard(gameId);

        return board.isMovable(moveRequestDto.getColor(),moveRequestDto.getSource(), moveRequestDto.getTarget());
    }

    public void move(long gameId, MoveRequestDto moveRequestDto) {
        pieceService.catchPiece(gameId, moveRequestDto);
        pieceService.move(gameId, moveRequestDto);
    }

    private Board generateBoard(long gameId) {
        List<Piece> pieces = pieceService.findPiecesByGameId(gameId)
                .stream()
                .map(piece -> PieceFactory.createPiece(
                        piece.getColor(),
                        piece.getShape(),
                        new Position(piece.getY(), piece.getX())))
                .collect(Collectors.toList());
        return new Board(pieces);
    }
}
