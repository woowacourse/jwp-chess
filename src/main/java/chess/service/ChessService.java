package chess.service;

import chess.domain.board.dto.BoardDto;
import chess.domain.command.Command;
import chess.domain.command.Move;
import chess.domain.command.dao.MoveCommandDao;
import chess.domain.command.dto.MoveCommandDto;
import chess.domain.game.ChessGame;
import chess.domain.game.Side;
import chess.domain.game.dao.ChessGameDao;
import chess.domain.game.dto.ChessGameDto;
import chess.exception.ChessException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ChessService {

    private final ChessGameDao chessGameDao;
    private final MoveCommandDao moveCommandDao;

    public ChessService(ChessGameDao chessGameDao, MoveCommandDao moveCommandDao) {
        this.chessGameDao = chessGameDao;
        this.moveCommandDao = moveCommandDao;
    }

    public Long addChessGame(String gameName) {
        ChessGame chessGame = ChessGame.initChessGame("game");
        chessGame.setName(gameName);

        return chessGameDao.addGame(chessGame);
    }

    public ChessGame replayedChessGame(String gameId) {
        List<Command> commands = moveCommandDao.findCommandsByGameId(gameId);
        ChessGame chessGame = ChessGame.initChessGame("game");

        for (Command command : commands) {
            chessGame.execute(command);
        }
        chessGame.setId(gameId);

        return chessGame;
    }

    public BoardDto movePiece(String gameId, MoveCommandDto moveCommandDto) {
        String source = moveCommandDto.getSource();
        String target = moveCommandDto.getTarget();
        Side turn = moveCommandDto.getTurn();

        ChessGame chessGame = replayedChessGame(gameId);
        validateCurrentTurn(chessGame, turn);
        move(chessGame, new Move(source, target), turn);

        return new BoardDto(chessGame);
    }

    private void validateCurrentTurn(ChessGame chessGame, Side playerSide) {
        if (!chessGame.currentTurn().equals(playerSide)) {
            throw new ChessException("플레이어의 턴이 아닙니다");
        }
    }

    private void move(ChessGame chessGame, Move command, Side side) {
        chessGame.execute(command);

        command.setGameId(chessGame.getId());
        command.setSide(side);
        int insertedRowCount = moveCommandDao.addMoveCommand(command);

        if (insertedRowCount == 0) {
            throw new ChessException("플레이어의 턴이 아닙니다");
        }
    }

    public ChessGameDto findGameById(String gameId) {
        return chessGameDao.findGameById(gameId);
    }
}
