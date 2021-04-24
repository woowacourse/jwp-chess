package chess.service;

import chess.dao.ChessDao;
import chess.dao.MovementDao;
import chess.domain.board.Board;
import chess.domain.game.ChessGame;
import chess.domain.position.Position;
import chess.entity.Chess;
import chess.entity.Movement;
import chess.exception.DuplicateRoomException;
import chess.exception.NotExistRoomException;
import chess.service.dto.*;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SpringChessService {

    private final ChessDao chessDao;
    private final MovementDao movementDao;

    public SpringChessService(final ChessDao chessDao, final MovementDao movementDao) {
        this.chessDao = chessDao;
        this.movementDao = movementDao;
    }

    @Transactional
    public MoveResponseDto movePiece(final String gameName, final MoveRequestDto requestDto) {
        ChessGame chessGame = ChessGame.newGame();
        Chess chess = movedChess(chessGame, gameName);
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

    @Transactional
    public void saveChess(final ChessSaveRequestDto requestDto) {
        Chess chess = new Chess(requestDto.getName());

        if (chessDao.findByName(requestDto.getName()).isPresent()) {
            throw new DuplicateRoomException();
        }

        chessDao.save(chess);
    }

    @Transactional
    public void changeGameStatus(final GameStatusRequestDto requestDto) {
        ChessGame chessGame = ChessGame.newGame();
        Chess chess = movedChess(chessGame, requestDto.getChessName());

        chess.changeRunning(!requestDto.isGameOver());
        chess.changeWinnerColor(chessGame.findWinner());
        chessDao.update(chess);
    }

    @Transactional(readOnly = true)
    public GameStatusDto loadChess(final String name) {
        ChessGame chessGame = ChessGame.newGame();
        Chess chess = movedChess(chessGame, name);

        return new GameStatusDto(chessGame.pieces(),
            chessGame.calculateScore(), !chess.isRunning(), chess.getWinnerColor());
    }

    public TilesDto emptyBoard() {
        return new TilesDto(Board.emptyBoard());
    }

    private Chess movedChess(final ChessGame chessGame, final String name) {
        Chess chess = findChessByName(name);
        List<Movement> movements = movementDao.findByChessName(chess.getName());

        for (Movement movement : movements) {
            chessGame.moveByTurn(new Position(movement.getSourcePosition()), new Position(movement.getTargetPosition()));
        }
        return chess;
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
