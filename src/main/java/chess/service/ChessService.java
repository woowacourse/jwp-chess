package chess.service;

import chess.domain.command.Command;
import chess.domain.command.Move;
import chess.domain.command.dao.MoveCommandDao;
import chess.domain.game.ChessGame;
import chess.domain.game.Score;
import chess.domain.game.Side;
import chess.domain.game.dao.ChessGameDao;
import chess.exception.ChessException;
import chess.domain.command.dto.MoveCommandDto;
import chess.web.view.RenderView;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        ChessGame chessGame = ChessGame.initChessGame();
        chessGame.setName(gameName);

        return chessGameDao.addGame(chessGame);
    }

    public ChessGame replayedChessGame(String gameId) {
        List<Command> commands = moveCommandDao.findCommandsByGameId(gameId);

        ChessGame chessGame = ChessGame.initChessGame();
        for (Command command : commands) {
            chessGame.execute(command);
        }
        chessGame.setId(gameId);
        return chessGame;
    }

    public Map<String, Object> movePiece(String gameId, MoveCommandDto moveCommandDto) {
        String source = moveCommandDto.getSource();
        String target = moveCommandDto.getTarget();
        Side turn = moveCommandDto.getTurn();

        ChessGame chessGame = replayedChessGame(gameId);
        validateCurrentTurn(chessGame, turn);

        move(chessGame, new Move(source, target), turn);

        // todo Spark 의존 없애기
        Map<String, Object> model = RenderView.renderBoard(chessGame);

        if (chessGame.isGameSet()) {
            chessGameDao.updateGameEnd(chessGame.getId());
            model.put("isGameSet", Boolean.TRUE);
            model.put("gameResult", result(chessGame));
        }
        return model;
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

    private Map<String, Object> result(ChessGame chessGame) {
        Map<String, Object> model = new HashMap<>();

        model.put("winner", chessGame.winner().toString());

        Score score = chessGame.score();
        model.put("blackScore", score.blackScore());
        model.put("whiteScore", score.whiteScore());

        return model;
    }

    private void validateCurrentTurn(ChessGame chessGame, Side playerSide) {
        if (!chessGame.currentTurn().equals(playerSide)) {
            throw new ChessException("플레이어의 턴이 아닙니다");
        }
    }
}
