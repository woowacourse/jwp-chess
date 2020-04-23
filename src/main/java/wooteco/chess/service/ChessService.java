package wooteco.chess.service;

import org.springframework.stereotype.Service;
import wooteco.chess.dao.ChessGameDao;
import wooteco.chess.domain.game.Board;
import wooteco.chess.domain.game.ChessGame;
import wooteco.chess.domain.game.Turn;
import wooteco.chess.domain.game.exception.InvalidTurnException;
import wooteco.chess.domain.game.state.Playing;
import wooteco.chess.domain.piece.Position;
import wooteco.chess.domain.piece.exception.NotMovableException;
import wooteco.chess.dto.*;

import java.sql.SQLException;

@Service
public class ChessService {
    private static final ChessGameDao chessGameDao = new ChessGameDao();

    public ResponseDto createChessRoom() throws SQLException {
        ChessGame chessGame = chessGameDao.save();
        if (chessGame == null) {
            return new ResponseDto(ResponseDto.FAIL, "새로운 방을 만드는데 실패했습니다.");
        }
        chessGame.start();
        chessGameDao.update(chessGame);
        return new ResponseDto(ResponseDto.SUCCESS, chessGame.getId());
    }

    public ResponseDto restartGame(int chessRoomId) throws SQLException {
        ChessGame chessGame = chessGameDao.findById(chessRoomId);
        if (chessGame == null) {
            return null;
        }
        ChessGame newChessGame = new ChessGame(chessGame.getId(), new Playing(Board.create(), Turn.WHITE));
        chessGameDao.update(newChessGame);
        return new ResponseDto(ResponseDto.SUCCESS, chessGame.getId());
    }

    public ResponseDto movePiece(int chessGameId, Position sourcePosition, Position targetPosition) throws SQLException {
        ChessGame chessGame = chessGameDao.findById(chessGameId);
        try {
            chessGame.move(sourcePosition, targetPosition);
            chessGameDao.update(chessGame);
        } catch (NotMovableException | IllegalArgumentException e) {
            return new ResponseDto(ResponseDto.FAIL, "이동할 수 없는 위치입니다.");
        } catch (InvalidTurnException e) {
            return new ResponseDto(ResponseDto.FAIL, chessGame.turn().getColor() + "의 턴입니다.");
        }
        return responseChessGame(chessGame);
    }

    public ResponseDto getChessGameById(int chessGameId) throws SQLException {
        ChessGame chessGame = chessGameDao.findById(chessGameId);
        return responseChessGame(chessGame);
    }

    public ResponseDto getGameList() throws SQLException {
        return new ResponseDto(ResponseDto.SUCCESS, chessGameDao.selectAll());
    }

    private static ResponseDto responseChessGame(ChessGame chessGame) {
        return new ResponseDto(ResponseDto.SUCCESS,
                new ChessGameDto(new BoardDto(chessGame.board()), new TurnDto(chessGame.turn()),
                        new StatusDto(chessGame.status().getWhiteScore(), chessGame.status().getBlackScore(),
                                chessGame.status().getWinner()), chessGame.isFinished()));
    }
}
