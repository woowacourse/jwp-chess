package chess.web.service;

import chess.domain.game.ChessGame;
import chess.domain.game.state.ChessBoard;
import chess.domain.game.state.Player;
import chess.domain.game.state.RunningGame;
import chess.domain.piece.Piece;
import chess.domain.piece.position.Position;
import chess.domain.piece.property.Color;
import chess.web.dao.ChessBoardDao;
import chess.web.dao.PlayerDao;
import chess.web.dto.MoveDto;
import chess.web.dto.MoveResultDto;
import chess.web.dto.PlayResultDto;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ChessGameService {

    private final ChessBoardDao chessBoardDao;
    private final PlayerDao playerDao;

    public ChessGameService(ChessBoardDao chessBoardDao, PlayerDao playerDao) {
        this.chessBoardDao = chessBoardDao;
        this.playerDao = playerDao;
    }

    public ChessGame start() {
        ChessGame chessGame = new ChessGame();
        chessGame.start();

        removeAll();
        saveAll(chessGame);

        return chessGame;
    }

    public MoveResultDto move(MoveDto moveDto) {
        ChessGame chessGame = getChessGame();
        String turn = chessGame.getTurn();
        MoveResultDto moveResultDto = new MoveResultDto();

        try {
            chessGame.move(Position.of(moveDto.getSource()), Position.of(moveDto.getTarget()));
            removeAll();
            if (isChessGameEnd(chessGame)) {
                moveResultDto.setIsGameOver(true);
                moveResultDto.setWinner(turn);
                return moveResultDto;
            }
            saveAll(chessGame);
        } catch (IllegalArgumentException e) {
            moveResultDto.setIsMovable(false);
        }

        return moveResultDto;
    }

    public PlayResultDto play() {
        Map<Position, Piece> board = findAllBoard();
        if (board.isEmpty()) {
            start();
        }

        Map<String, Piece> boardDto = new HashMap<>();
        for (Position position : board.keySet()) {
            Piece piece = board.get(position);
            boardDto.put(position.toString(), piece);
        }

        return new PlayResultDto(boardDto, findTurn().name());
    }

    private void removeAll() {
        chessBoardDao.deleteAll();
        playerDao.deleteAll();
    }

    private void saveAll(ChessGame chessGame) {
        Map<Position, Piece> chessBoard = chessGame.getBoard();
        for (Position position : chessBoard.keySet()) {
            chessBoardDao.save(position, chessBoard.get(position));
        }
        playerDao.save(Color.of(chessGame.getTurn()));
    }

    public ChessGame getChessGame() {
        return ChessGame.of(new RunningGame(ChessBoard.of(findAllBoard()), findTurn()));
    }

    private Map<Position, Piece> findAllBoard() {
        return chessBoardDao.findAll();
    }

    private Player findTurn() {
        return playerDao.getPlayer();
    }

    private boolean isChessGameEnd(ChessGame chessGame) {
        return chessGame.isFinished();
    }
}
