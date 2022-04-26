package chess.service;

import chess.dao.GameDao;
import chess.dao.PieceDao;
import chess.domain.game.ChessGame;
import chess.domain.game.GameResult;
import chess.domain.piece.ChessmenInitializer;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.piece.Pieces;
import chess.dto.GameResultDto;
import chess.dto.MoveDto;
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
        if (!gameDao.isInId(gameId)) {
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

    public void move(String gameId, MoveDto moveDto) {
        System.out.println(gameId);

        System.out.println("getGameStatus(gameId)" + getGameStatus(gameId));
        getGameStatus(gameId).moveChessmen(moveDto.toEntity());

        saveMove(gameId, moveDto);
    }

    private void saveMove(String gameId, MoveDto moveDto) {
        ChessGame chessGame = getGameStatus(gameId);
        chessGame.moveChessmen(moveDto.toEntity());
        boolean forceEndFlag = chessGame.getForceEndFlag();
        Color turn = chessGame.getTurn();

        pieceDao.deleteAllByGameId(gameId);
        pieceDao.createAllById(chessGame.getChessmen().getPieces(), gameId);
        gameDao.updateTurnById(turn, gameId);
        gameDao.updateForceEndFlagById(forceEndFlag, gameId);
    }

}
