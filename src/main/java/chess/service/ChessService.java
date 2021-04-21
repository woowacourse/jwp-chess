package chess.service;

import chess.dao.ChessDAO;
import chess.dao.ChessDto;
import chess.domain.ChessGame;
import chess.domain.ChessGameImpl;
import chess.domain.CurrentGameRoom;
import chess.domain.PieceForm;
import chess.domain.Pieces;
import chess.domain.Position;
import chess.domain.TeamColor;
import chess.domain.converter.StringPositionConverter;
import chess.domain.piece.Piece;
import chess.exception.InvalidGameException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ChessService {

    private final ChessDAO chessDAO;
    private final CurrentGameRoom currentGameRoom;
    private final StringPositionConverter positionConverter;

    public ChessService(ChessDAO chessDAO) {
        this.chessDAO = chessDAO;
        this.currentGameRoom = new CurrentGameRoom();
        positionConverter = new StringPositionConverter();
    }

    public ChessGame loadChess(Long gameId) {
        return currentGameRoom.loadGame(gameId)
            .orElseGet(() -> {
                ChessGame loadGame = chessGame(gameId);
                currentGameRoom.save(gameId, loadGame);
                return loadGame;
            });
    }

    private ChessGame chessGame(Long gameId) {
        final List<ChessDto> foundChess = chessDAO.findChess(gameId);
        final Optional<TeamColor> foundTurnColor = chessDAO.findCurrentTurn(gameId);
        if (foundChess.isEmpty() || foundTurnColor.isEmpty()) {
            return ChessGameImpl.initialGame();
        }
        final List<Piece> pieces = foundChess.stream()
            .map(chess -> {
                Position position = positionConverter.convert(chess.getPosition());
                TeamColor teamColor = TeamColor.valueOf(chess.getColor());
                return PieceForm.create(chess.getName(), teamColor, position);
            }).collect(Collectors.toList());

        return ChessGameImpl.from(Pieces.from(pieces), foundTurnColor.get());
    }

    public void restart(Long gameId) {
        currentGameRoom.restart(gameId);
    }

    @Transactional
    public void exitGame(Long gameId) {
        chessDAO.deleteAllByGameId(gameId);
        currentGameRoom.remove(gameId);
    }

    @Transactional
    public void saveGame(Long gameId) {
        ChessGame chessGame = currentGameRoom.loadGame(gameId)
            .orElseThrow(InvalidGameException::new);

        chessDAO.deleteAllByGameId(gameId);
        chessDAO.saveCurrentColor(gameId, chessGame.currentColor());
        chessDAO.savePieces(gameId, chessGame.pieces().asList());
    }

    public void move(Long gameId, Position currentPosition, Position targetPosition) {
        final ChessGame chessGame = loadChess(gameId);
        chessGame.movePiece(currentPosition, targetPosition);
    }
}
