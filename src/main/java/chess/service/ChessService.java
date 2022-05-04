package chess.service;

import chess.dao.BoardDao;
import chess.dao.GameDao;
import chess.domain.chessboard.ChessBoard;
import chess.domain.command.GameCommand;
import chess.domain.game.ChessGame;
import chess.domain.piece.Color;
import chess.domain.piece.EmptyPiece;
import chess.domain.piece.generator.NormalPiecesGenerator;
import chess.domain.position.Position;
import chess.domain.state.State;
import chess.domain.state.StateName;
import chess.dto.BoardDto;
import chess.dto.GameDto;
import chess.dto.RoomDto;
import chess.entity.Square;
import chess.dto.StatusDto;
import chess.entity.Game;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChessService {

    private final GameDao gameDao;
    private final BoardDao boardDao;

    public ChessService(GameDao gameDao, BoardDao boardDao) {
        this.gameDao = gameDao;
        this.boardDao = boardDao;
    }

    @Transactional
    public int insertGame(RoomDto roomDto, ChessBoard chessBoard) {
        int id = gameDao.save(new Game(roomDto.getTitle(), roomDto.getPassword(), StateName.WHITE_TURN.getValue()));
        boardDao.save(Square.from(chessBoard, id));
        return id;
    }

    public List<GameDto> findGame() {
        return gameDao.findAll().stream()
                .map(GameDto::of)
                .collect(Collectors.toList());
    }

    public BoardDto selectBoard(int id) {
        return BoardDto.of(boardDao.findById(id));
    }

    public String selectWinner(int gameId) {
        ChessBoard chessBoard = Square.toBoard((boardDao.findById(gameId)));

        if (chessBoard.isEnd()) {
            return chessBoard.getWinner().name();
        }
        return null;
    }

    public String selectState(int gameId) {
        return gameDao.findState(gameId).getValue();
    }

    public StatusDto selectStatus(int gameId) {
        ChessBoard chessBoard = Square.toBoard((boardDao.findById(gameId)));

        ChessGame chessGame = new ChessGame(gameDao.findState(gameId), chessBoard);
        Map<Color, Double> scores = chessGame.calculateScore();

        return new StatusDto(scores);
    }

    @Transactional
    public void movePiece(int gameId, String from, String to) {
        ChessBoard chessBoard = Square.toBoard((boardDao.findById(gameId)));
        ChessGame chessGame = new ChessGame(gameDao.findState(gameId), chessBoard);
        playChessGame(from, to, chessGame);

        gameDao.update(new Game(chessGame.getState().getValue(), gameId));
        boardDao.update(new Square(to, chessBoard.selectPiece(Position.of(to)), gameId));
        boardDao.update(new Square(from, EmptyPiece.getInstance(), gameId));
    }

    private void playChessGame(String from, String to, ChessGame chessGame) {
        chessGame.playGameByCommand(GameCommand.of("move", from, to));
        chessGame.isEndGameByPiece();
    }

    @Transactional
    public void endGame(int gameId) {
        gameDao.update(new Game(StateName.FINISH.getValue(), gameId));
    }

    @Transactional
    public void deleteGame(int gameId, String password) {
        Game game = gameDao.findById(gameId);
        State state = gameDao.findState(gameId);
        validateState(state);
        if (game.isSamePassword(password)) {
            gameDao.delete(gameId);
            return;
        }
        throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
    }

    private void validateState(State state) {
        if (!state.isFinished()) {
            throw new IllegalStateException("진행중인 게임은 삭제할 수 없습니다.");
        }
    }

    @Transactional
    public void restartGame(int gameId) {
        ChessBoard chessBoard = new ChessBoard(new NormalPiecesGenerator());
        int id = gameDao.update(new Game(StateName.WHITE_TURN.getValue(), gameId));
        boardDao.delete(gameId);
        boardDao.save(Square.from(chessBoard, id));
    }
}
