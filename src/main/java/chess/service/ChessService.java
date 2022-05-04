package chess.service;

import chess.dao.BoardDao;
import chess.dao.GameDao;
import chess.model.ChessGame;
import chess.model.Color;
import chess.model.Status;
import chess.model.board.Board;
import chess.model.board.ChessInitializer;
import chess.model.board.Square;
import chess.model.piece.Piece;
import chess.model.piece.PieceType;
import chess.service.dto.BoardDto;
import chess.service.dto.ChessGameDto;
import chess.service.dto.GameResultDto;
import chess.service.dto.GamesDto;
import chess.service.dto.PieceWithSquareDto;
import chess.service.dto.StatusDto;
import chess.web.controller.dto.MoveRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ChessService {

    private final BoardDao boardDao;
    private final GameDao gameDao;
    private final PasswordEncoder passwordEncoder;

    public ChessService(BoardDao boardDao, GameDao gameDao,
        PasswordEncoder passwordEncoder) {
        this.boardDao = boardDao;
        this.gameDao = gameDao;
        this.passwordEncoder = passwordEncoder;
    }

    public void initGame(Long gameId) {
        ChessGame chessGame = new ChessGame(new ChessInitializer(), Status.PLAYING);
        boardDao.remove(gameId);
        boardDao.initBoard(gameId);
        updateGame(chessGame, gameId);
    }

    private ChessGameDto updateGame(ChessGame chessGame, Long id) {
        ChessGameDto chessGameDto = ChessGameDto.of(chessGame, id);
        gameDao.update(chessGameDto);
        return chessGameDto;
    }

    public BoardDto getBoard(Long id) {
        return boardDao.getBoardByGameId(id);
    }

    public ChessGameDto move(Long id, MoveRequest moveRequest) {
        ChessGame chessGame = getGameFromDao(id);
        Square fromSquare = Square.of(moveRequest.getFrom());
        Square toSquare = Square.of(moveRequest.getTo());
        chessGame.move(fromSquare, toSquare);
        boardDao.update(PieceWithSquareDto.of(toSquare, chessGame.findPieceBySquare(toSquare)), id);
        boardDao.update(PieceWithSquareDto.of(fromSquare, chessGame.findPieceBySquare(fromSquare)), id);
        return updateGame(chessGame, id);
    }

    public boolean isRunning(Long id) {
        return getGameFromDao(id).isPlaying();
    }

    private ChessGame getGameFromDao(Long id) {
        ChessGameDto game = gameDao.findById(id);
        BoardDto boardDto = getBoard(id);
        return new ChessGame(new Board(boardDto), Color.valueOf(game.getTurn()),
            Status.valueOf(game.getStatus()));
    }

    public boolean isGameEmpty(Long id) {
        return getGameFromDao(id).isEmpty();
    }

    public void endGame(Long id) {
        gameDao.updateStatus(new StatusDto(Status.EMPTY.name()), id);
        boardDao.remove(id);
    }

    public GamesDto getAllGames() {
        return gameDao.findAll();
    }

    public Long createGame(String name, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        Long gameId = gameDao.createGame(name, encodedPassword);
        ChessGame chessGame = new ChessGame(new ChessInitializer(), Status.PLAYING);
        boardDao.initBoard(gameId);
        updateGame(chessGame, gameId);
        return gameId;
    }

    public GameResultDto getResult(Long id) {
        return getGameFromDao(id).getResult();
    }

    public void deleteGame(Long id, String password) {
        ChessGameDto chessGameDto = gameDao.findById(id);
        validatePassword(password, chessGameDto.getPassword());
        validateRunningGame(id);
        gameDao.deleteGame(id);
    }

    private void validateRunningGame(Long id) {
        ChessGameDto chessGameDto = gameDao.findById(id);
        if (Status.PLAYING.name().equals(chessGameDto.getStatus())) {
            throw new IllegalStateException("진행중인 게임은 삭제할 수 없습니다.");
        }
    }

    private void validatePassword(String inputPassword, String password) {
        if (!passwordEncoder.matches(inputPassword, password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    public boolean existsGameById(Long gameId) {
        return gameDao.existsById(gameId);
    }

    public void removeAllGame() {
        gameDao.removeAll();
    }
}
