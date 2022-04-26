package chess.service;

import chess.dao.GameDao;
import chess.dao.PieceDao;
import chess.domain.command.MoveCommand;
import chess.domain.game.ChessGame;
import chess.domain.game.GameResult;
import chess.domain.piece.ChessmenInitializer;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.piece.Pieces;
import chess.dto.GameResultDto;
import chess.dto.MoveCommandDto;
import chess.dto.PieceDto;
import chess.dto.PiecesDto;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ChessGameService {

    private final ChessmenInitializer chessmenInitializer = new ChessmenInitializer();

    private final PieceDao pieceDao;
    private final GameDao gameDao;


    public ChessGameService(PieceDao pieceDao, GameDao gameDao) {
        this.pieceDao = pieceDao;
        this.gameDao = gameDao;
    }

    public void createOrGet(String gameId) {
        if (!gameDao.exists(gameId)) {
            initGame(gameId);
        }
    }

    private void initGame(String gameId) {
        gameDao.createById(gameId);
        Pieces chessmen = chessmenInitializer.init();
        pieceDao.createAllById(chessmen.getPieces(), gameId);
    }

    public ChessGame getGameStatus(String gameId) {
        boolean forceEndFlag = gameDao.findForceEndFlagById(gameId);
        Color turn = gameDao.findTurnById(gameId);
        Pieces chessmen = pieceDao.findAllByGameId(gameId);
        return new ChessGame(forceEndFlag, chessmen, turn);
    }

    public PiecesDto getCurrentGame(String gameId) {
        return new PiecesDto(toDto(getGameStatus(gameId).getChessmen()));
    }

    private List<PieceDto> toDto(Pieces chessmen) {
        List<PieceDto> pieces = new ArrayList<>();
        for (Piece piece : chessmen.getPieces()) {
            pieces.add(new PieceDto(piece.getPosition().getPosition(),
                piece.getColor().getName(),
                piece.getName()));
        }
        return pieces;
    }

    public GameResultDto calculateGameResult(String gameId) {
        GameResult gameResult = GameResult.calculate(getGameStatus(gameId).getChessmen());
        return new GameResultDto(gameResult.getWinner().getName(),
            gameResult.getWhiteScore(),
            gameResult.getBlackScore());
    }

    public void cleanGame(String gameId) {
        pieceDao.deleteAllByGameId(gameId);
        gameDao.deleteById(gameId);
    }

    public void move(String gameId, MoveCommandDto moveCommandDto) {
        String from = moveCommandDto.getSource();
        String to = moveCommandDto.getTarget();
        getGameStatus(gameId).moveChessmen(new MoveCommand(from, to));
        saveMove(gameId, moveCommandDto);
    }

    private void saveMove(String gameId, MoveCommandDto moveCommandDto) {
        ChessGame chessGame = getGameStatus(gameId);
        chessGame.moveChessmen(moveCommandDto.toEntity());
        boolean forceEndFlag = chessGame.getForceEndFlag();
        Color turn = chessGame.getTurn();

        pieceDao.deleteAllByGameId(gameId);
        pieceDao.createAllById(chessGame.getChessmen().getPieces(), gameId);
        gameDao.updateTurnById(turn, gameId);
        gameDao.updateForceEndFlagById(forceEndFlag, gameId);
    }

}
