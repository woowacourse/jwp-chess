package chess.service;

import chess.domain.board.Board;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceFactory;
import chess.domain.piece.Position;
import chess.dto.chess.ChessResponseDto;
import chess.dto.game.GameRequestDto;
import chess.dto.game.GameResponseDto;
import chess.dto.chess.MoveRequestDto;
import chess.dto.chess.MoveResponseDto;
import chess.dto.piece.PieceDto;
import chess.dto.user.UserResponseDto;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ChessService {

    private final GameService gameService;
    private final PieceService pieceService;
    private final UserService userService;

    public ChessService(GameService gameService, PieceService pieceService,
        UserService userService) {
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
        String turn = gameService.findById(gameId).getTurn();
        if (!turn.equals(moveRequestDto.getColor().toUpperCase())) {
            return false;
        }
        Board board = generateBoard(gameId);

        return board.isMovable(moveRequestDto.getColor(), moveRequestDto.getSource(),
            moveRequestDto.getTarget());
    }

    public MoveResponseDto move(long gameId, MoveRequestDto moveRequestDto) {
        Board board = generateBoard(gameId);
        board.moveAndCatchPiece(
            Color.from(moveRequestDto.getColor()),
            new Position(moveRequestDto.getSource()),
            new Position(moveRequestDto.getTarget())
        );

        pieceService.catchPiece(gameId, moveRequestDto);
        pieceService.move(gameId, moveRequestDto);
        if (board.isKingCatch()) {
            gameService.endGame(gameId);
            pieceService.removeAll(gameId);
            return new MoveResponseDto(true, true);
        }
        gameService.changeTurn(gameId);
        return new MoveResponseDto(false, true);

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
