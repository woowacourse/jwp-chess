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
import chess.dto.GameRoomDto;
import chess.dto.MoveCommandDto;
import chess.dto.PieceDto;
import chess.dto.PiecesDto;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ChessGameService {

    private static final String INVALID_ROOM_PASSWORD_EXCEPTION_MESSAGE = "잘못된 방 비밀번호 입니다. 다시 입력해주세요.";
    private static final String NOT_FINISHED_GAME_STATUS_EXCEPTION_MESSAGE = "아직 게임이 끝나지 않아 삭제할 수 없습니다.";

    private final PieceDao pieceDao;
    private final GameDao gameDao;

    public ChessGameService(PieceDao pieceDao, GameDao gameDao) {
        this.pieceDao = pieceDao;
        this.gameDao = gameDao;
    }

    public List<GameRoomDto> getAllGames() {
        return gameDao.findAllIdAndTitle();
    }

    public long create(String title, String password) {
        long gameId = gameDao.createByTitleAndPassword(title, password);
        Pieces chessmen = ChessmenInitializer.init();
        pieceDao.createAllByGameId(chessmen.getPieces(), gameId);
        return gameId;
    }

    public ChessGame getGameStatus(long gameId) {
        boolean endFlag = gameDao.findEndFlagById(gameId);
        Color turn = gameDao.findTurnById(gameId);
        Pieces chessmen = pieceDao.findAllByGameId(gameId);
        return new ChessGame(endFlag, chessmen, turn);
    }

    public PiecesDto getCurrentGame(long gameId) {
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

    public GameResultDto calculateGameResult(long gameId) {
        GameResult gameResult = GameResult.calculate(getGameStatus(gameId).getChessmen());
        return new GameResultDto(gameResult.getWinner().getName(),
            gameResult.getWhiteScore(),
            gameResult.getBlackScore());
    }

    public void cleanGame(long gameId) {
        pieceDao.deleteAllByGameId(gameId);
        gameDao.deleteById(gameId);
    }

    public void move(long gameId, MoveCommandDto moveCommandDto) {
        String from = moveCommandDto.getSource();
        String to = moveCommandDto.getTarget();
        getGameStatus(gameId).moveChessmen(new MoveCommand(from, to));
        saveMove(gameId, moveCommandDto);
        if (getGameStatus(gameId).isEnd()) {
            gameDao.updateEndFlagById(true, gameId);
        }
    }

    private void saveMove(long gameId, MoveCommandDto moveCommandDto) {
        ChessGame chessGame = getGameStatus(gameId);
        chessGame.moveChessmen(moveCommandDto.toEntity());
        boolean endFlag = chessGame.getEndFlag();
        Color turn = chessGame.getTurn();

        pieceDao.deleteAllByGameId(gameId);
        pieceDao.createAllByGameId(chessGame.getChessmen().getPieces(), gameId);
        gameDao.updateTurnById(turn, gameId);
        gameDao.updateEndFlagById(endFlag, gameId);
    }

    public void cleanGameByIdAndPassword(long id, String password) {
        validatePassword(id, password);
        validateEnd(id);
        cleanGame(id);
    }

    private void validatePassword(long id, String password) {
        String targetPassword = gameDao.findPasswordById(id);
        if (!targetPassword.equals(password)) {
            throw new IllegalArgumentException(INVALID_ROOM_PASSWORD_EXCEPTION_MESSAGE);
        }
    }

    private void validateEnd(long id) {
        boolean endFlag = gameDao.findEndFlagById(id);
        if (!endFlag) {
            throw new IllegalArgumentException(NOT_FINISHED_GAME_STATUS_EXCEPTION_MESSAGE);
        }
    }

}
