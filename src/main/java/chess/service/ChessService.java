package chess.service;

import chess.dao.ChessDao;
import chess.dao.MovementDao;
import chess.domain.board.Board;
import chess.domain.game.ChessGame;
import chess.domain.position.Position;
import chess.entity.Chess;
import chess.entity.Movement;
import chess.service.dto.*;

import java.util.List;

public class ChessService {
    private final ChessDao chessDao;
    private final MovementDao movementDao;

    public ChessService(final ChessDao chessDao, final MovementDao movementDao) {
        this.chessDao = chessDao;
        this.movementDao = movementDao;
    }

    public TilesDto emptyBoard() {
        return new TilesDto(Board.emptyBoard());
    }

    public MoveResponseDto movePiece(final MoveRequestDto requestDto) {
        ChessGame chessGame = ChessGame.newGame();
        List<Movement> movements = movementDao.findByChessName(requestDto.getChessName());

        for (Movement movement : movements) {
            chessGame.moveByTurn(new Position(movement.getSourcePosition()), new Position(movement.getTargetPosition()));
        }

        chessGame.moveByTurn(new Position(requestDto.getSource()), new Position(requestDto.getTarget()));
        Chess chess = findChessByName(requestDto.getChessName());
        movementDao.save(new Movement(chess.getId(), requestDto.getSource(), requestDto.getTarget()));

        if (chessGame.isGameOver()) {
            chess.changeRunning(!chessGame.isGameOver());
            chess.changeWinnerColor(chessGame.findWinner());
            chessDao.update(chess);
        }
        return new MoveResponseDto(requestDto.getSource(), requestDto.getTarget(), chessGame.calculateScore(), !chess.isRunning());
    }

    public void changeGameStatus(final GameStatusRequestDto requestDto) {
        ChessGame chessGame = ChessGame.newGame();
        Chess chess = findChessByName(requestDto.getChessName());
        List<Movement> movements = movementDao.findByChessName(chess.getName());

        for (Movement movement : movements) {
            chessGame.moveByTurn(new Position(movement.getSourcePosition()), new Position(movement.getTargetPosition()));
        }

        chess.changeRunning(!requestDto.isGameOver());
        chess.changeWinnerColor(chessGame.findWinner());
        chessDao.update(chess);
    }

    public CommonResponseDto<GameStatusDto> startChess(final ChessSaveRequestDto request) {
        try {
            ChessGame chessGame = ChessGame.newGame();
            Chess chess = new Chess(request.getName());
            chessDao.save(chess);
            return new CommonResponseDto<>(new GameStatusDto(chessGame.pieces(), chessGame.calculateScore(), chessGame.isGameOver(), chess.getWinnerColor()), ResponseCode.OK.code(), ResponseCode.OK.message());
        } catch (RuntimeException exception){
            return new CommonResponseDto<>(ResponseCode.BAD_REQUEST.code(), exception.getMessage());
        }
    }

    public CommonResponseDto<GameStatusDto> loadChess(final String chessName) {
        try {
            ChessGame chessGame = ChessGame.newGame();
            Chess chess = findChessByName(chessName);
            List<Movement> movements = movementDao.findByChessName(chess.getName());

            for (Movement movement : movements) {
                chessGame.moveByTurn(new Position(movement.getSourcePosition()), new Position(movement.getTargetPosition()));
            }
            return new CommonResponseDto<>(new GameStatusDto(chessGame.pieces(),
                chessGame.calculateScore(), chessGame.isGameOver(), chess.getWinnerColor()),
                ResponseCode.OK.code(), ResponseCode.OK.message());
        } catch (RuntimeException exception) {
            return new CommonResponseDto<>(ResponseCode.BAD_REQUEST.code(), exception.getMessage());
        }
    }

    private Chess findChessByName(final String chessName) {
        return chessDao.findByName(chessName).orElseThrow(() -> new IllegalArgumentException("없는 게임 이름입니다"));
    }
}
