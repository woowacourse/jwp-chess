package chess.service;

import chess.dao.ChessDao;
import chess.dao.MovementDao;
import chess.dao.UserDao;
import chess.domain.board.Board;
import chess.domain.game.ChessGame;
import chess.domain.piece.Color;
import chess.domain.position.Position;
import chess.entity.Chess;
import chess.entity.Movement;
import chess.entity.User;
import chess.exception.*;
import chess.service.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class SpringChessService {

    private final ChessDao chessDao;
    private final MovementDao movementDao;
    private final UserDao userDao;

    public SpringChessService(final ChessDao chessDao, final MovementDao movementDao, final UserDao userDao) {
        this.chessDao = chessDao;
        this.movementDao = movementDao;
        this.userDao = userDao;
    }

    @Transactional
    public MoveResponseDto movePiece(final String gameName, final MoveRequestDto requestDto, User user) {
        Chess chess = findChessByName(gameName);
        ChessGame chessGame = ChessGame.newGame();
        moveChess(chessGame, chess.getName());
        validateTurn(chess, user.getName(), chessGame.turn());

        chessGame.moveByTurn(new Position(requestDto.getSource()), new Position(requestDto.getTarget()));
        movementDao.save(new Movement(chess.getId(), requestDto.getSource(), requestDto.getTarget()));

        if (chessGame.isGameOver()) {
            chess.changeRunning(!chessGame.isGameOver());
            chess.changeWinnerColor(chessGame.findWinner());
            chessDao.update(chess);
        }

        return new MoveResponseDto(requestDto.getSource(), requestDto.getTarget(),
                chessGame.calculateScore(), !chess.isRunning());
    }

    private void validateTurn(final Chess chess, final String playerName, final Color turn) {
        User user = userDao.findByName(playerName).orElseThrow(NotExistUserException::new);

        if (Objects.isNull(chess.getBlackPlayerId())) {
            throw new NotFoundUserException();
        }

        if (chess.getWhitePlayerId().equals(user.getId())) {
            if (turn.isBlack()) {
                throw new InvalidTurnException();
            }
        }

        if (chess.getBlackPlayerId().equals(user.getId())) {
            if (!turn.isBlack()) {
                throw new InvalidTurnException();
            }
        }
    }

    @Transactional
    public void saveChess(final ChessSaveRequestDto requestDto, User user) {
        User whiteUser = userDao.findByName(user.getName()).orElseThrow(NotExistUserException::new);

        if (chessDao.findByName(requestDto.getName()).isPresent()) {
            throw new DuplicateRoomException();
        }
        Chess chess = new Chess(requestDto.getName(), whiteUser.getId());
        chessDao.save(chess);
    }

    @Transactional
    public void changeGameStatus(final GameStatusRequestDto requestDto) {
        Chess chess = findChessByName(requestDto.getChessName());
        ChessGame chessGame = ChessGame.newGame();
        moveChess(chessGame, requestDto.getChessName());

        chess.changeRunning(!requestDto.isGameOver());
        chess.changeWinnerColor(chessGame.findWinner());
        chessDao.update(chess);
    }

    @Transactional
    public GameStatusDto loadChess(final String chessName, final User user) {
        Chess chess = findChessByName(chessName);
        validateChess(chess, user.getName());
        ChessGame chessGame = ChessGame.newGame();
        moveChess(chessGame, chessName);

        return new GameStatusDto(chessGame.pieces(),
                chessGame.calculateScore(), !chess.isRunning(), chess.getWinnerColor());
    }

    private void validateChess(final Chess chess, String playerName) {
        User user = userDao.findByName(playerName).orElseThrow(NotExistUserException::new);

        if (chess.getWhitePlayerId().equals(user.getId())) {
            return;
        }

        if (Objects.isNull(chess.getBlackPlayerId())) {
            chess.changeBlackPlayerID(user.getId());
            chessDao.update(chess);
            return;
        }

        if (chess.getBlackPlayerId().equals(user.getId())) {
            return;
        }

        throw new WrongAccessException();
    }

    public TilesDto emptyBoard() {
        return new TilesDto(Board.emptyBoard());
    }

    private void moveChess(final ChessGame chessGame, final String chessName) {
        List<Movement> movements = movementDao.findByChessName(chessName);

        for (Movement movement : movements) {
            chessGame.moveByTurn(new Position(movement.getSourcePosition()), new Position(movement.getTargetPosition()));
        }
    }

    private Chess findChessByName(final String chessName) {
        return chessDao.findByName(chessName).orElseThrow(NotExistRoomException::new);
    }

    @Transactional(readOnly = true)
    public ChessInfosDto findAllGame() {
        List<Chess> findChessGame = chessDao.findAll();
        return new ChessInfosDto(findChessGame);
    }
}
