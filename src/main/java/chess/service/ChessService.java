package chess.service;

import chess.domain.board.Board;
import chess.domain.location.Location;
import chess.domain.piece.Piece;
import chess.domain.team.Team;
import chess.dto.chess.ChessResponseDto;
import chess.dto.chess.MoveRequestDto;
import chess.dto.chess.MoveResponseDto;
import chess.dto.game.GameRequestDto;
import chess.dto.game.GameResponseDto;
import chess.dto.piece.PieceDto;
import chess.dto.user.UserResponseDto;
import chess.utils.PieceConverter;
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

    public ChessService(final GameService gameService, final PieceService pieceService,
        final UserService userService) {

        this.gameService = gameService;
        this.pieceService = pieceService;
        this.userService = userService;
    }

    public long initializeChess(final GameRequestDto gameRequestDto) {
        final long id = gameService.add(gameRequestDto);
        pieceService.createInitialPieces(id);
        return id;
    }

    public ChessResponseDto bringGameData(final long gameId) {
        final List<PieceDto> pieceDtos = pieceService.findPiecesByGameId(gameId);
        final GameResponseDto gameResponseDto = gameService.findById(gameId);
        final UserResponseDto host = userService.findUserById(gameResponseDto.getHostId());
        final UserResponseDto guest = userService.findUserById(gameResponseDto.getGuestId());
        return new ChessResponseDto(pieceDtos, host, guest, gameResponseDto);
    }

    public boolean checkMovement(final long gameId, final MoveRequestDto moveRequestDto) {
        final Team turn = gameService.findById(gameId).getTurn();
        if (!turn.equals(moveRequestDto.getColor())) {
            return false;
        }
        final Board board = generateBoard(gameId);

        return board.isMovable(
            Location.convert(moveRequestDto.getSource()),
            Location.convert(moveRequestDto.getTarget()),
            moveRequestDto.getColor()
        );
    }

    public MoveResponseDto move(final long gameId, final MoveRequestDto moveRequestDto) {
        final Board board = generateBoard(gameId);
        board.move(
            Location.convert(moveRequestDto.getSource()),
            Location.convert(moveRequestDto.getTarget()),
            moveRequestDto.getColor()
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

    private Board generateBoard(final long gameId) {
        final List<Piece> pieces = pieceService.findPiecesByGameId(gameId)
            .stream()
            .map(PieceConverter::run)
            .collect(Collectors.toList());

        return Board.of(pieces);
    }

}
