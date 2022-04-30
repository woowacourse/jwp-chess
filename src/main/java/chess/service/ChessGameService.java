package chess.service;

import chess.dao.GameDao;
import chess.dao.PieceDao;
import chess.domain.game.ChessGame;
import chess.domain.game.GameResult;
import chess.domain.game.Room;
import chess.domain.piece.ChessmenInitializer;
import chess.domain.piece.Pieces;
import chess.dto.GameResultDto;
import chess.dto.LogInDto;
import chess.dto.MoveDto;
import java.util.List;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

@Service
public class ChessGameService {
    private static final String PLAYING_CHESS_ERROR_MESSAGE = "진행중인 체스방은 삭제할 수 없습니다.";
    private static final String ALREADY_ROOM_ID_EXIST_ERROR_MESSAGE = "이미 해당 이름의 방이 있습니다.";
    private static final String LOGIN_FORMAT_ERROR_MESSAGE = "방 제목, 패스워드는 한글,영어,숫자 이외엔 들어올 수 없습니다.";
    private static final Pattern logInPattern = Pattern.compile("^[a-zA-Z가-힣ㄱ-ㅎ0-9]*$");

    private final ChessmenInitializer chessmenInitializer = new ChessmenInitializer();

    private final PieceDao pieceDao;
    private final GameDao gameDao;

    public ChessGameService(PieceDao pieceDao, GameDao gameDao) {
        this.pieceDao = pieceDao;
        this.gameDao = gameDao;
    }

    public void createGame(LogInDto logInDto) {
        validateUniqueId(logInDto);
        validateLogInFormat(logInDto.getId());
        validateLogInFormat(logInDto.getPassword());
        gameDao.create(logInDto);
        pieceDao.createAll(chessmenInitializer.init(), logInDto.getId());
    }

    private void validateLogInFormat(String id) {
        if(!logInPattern.matcher(id).matches()) {
            throw new IllegalArgumentException(LOGIN_FORMAT_ERROR_MESSAGE);
        }
    }

    public void validateLogIn(LogInDto logInDto) {
        gameDao.findRoom(logInDto.getId()).validateLogInPassword(logInDto);
    }

    private void validateUniqueId(LogInDto logInDto) {
        try {
            gameDao.findNoPasswordRoom(logInDto.getId());
        } catch (IllegalArgumentException e) {
            return;
        }
        throw new IllegalArgumentException(ALREADY_ROOM_ID_EXIST_ERROR_MESSAGE);
    }

    public ChessGame getGameStatus(String id) {
        Room room = gameDao.findNoPasswordRoom(id);
        return new ChessGame(room.isEnd(), pieceDao.findAll(id),
                room.getTurn());
    }

    public Pieces getPieces(String id) {
        return getGameStatus(id).getChessmen();
    }

    public GameResultDto calculateGameResult(String id) {
        return new GameResultDto(GameResult.calculate(getGameStatus(id).getChessmen()));
    }

    public void cleanGame(LogInDto logInDto) {
        validateLogIn(logInDto);
        pieceDao.deleteAll(logInDto.getId());
        gameDao.delete(logInDto.getId());
    }

    public void move(String id, MoveDto moveDto) {
        ChessGame chessGame = getGameStatus(id);
        chessGame.moveChessmen(moveDto.toEntity());
        pieceDao.deleteAll(id);
        pieceDao.createAll(chessGame.getChessmen(), id);
        gameDao.updateTurn(chessGame.getTurn(), id);
        gameDao.updateForceEndFlag(chessGame.getEnd(), id);
    }

    public List<Room> getRooms() {
        return gameDao.findAllRoom();
    }

    public void changeToEnd(String id) {
        gameDao.updateForceEndFlag(true, id);
    }

    public void validateEnd(String id) {
        if (!gameDao.findNoPasswordRoom(id).isEnd()) {
            throw new IllegalArgumentException(PLAYING_CHESS_ERROR_MESSAGE);
        }
    }
}
