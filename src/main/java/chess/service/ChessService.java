package chess.service;

import chess.dao.GameDao;
import chess.domain.ChessGameManager;
import chess.domain.position.Position;
import chess.dto.CommonDto;
import chess.dto.GameListDto;
import chess.dto.NewGameDto;
import chess.dto.RunningGameDto;
import org.springframework.stereotype.Service;

@Service
public class ChessService {
    private final GameDao gameDAO;

    public ChessService(GameDao gameDAO) {
        this.gameDAO = gameDAO;
    }

    public CommonDto<NewGameDto> saveNewGame() {
        ChessGameManager chessGameManager = new ChessGameManager();
        chessGameManager.start();
        int gameId = gameDAO.saveGame(chessGameManager);
        return new CommonDto<>("새로운 게임이 생성되었습니다.", NewGameDto.from(chessGameManager, gameId));
    }

    public CommonDto<RunningGameDto> move(int gameId, String startPosition, String endPosition) {
        ChessGameManager chessGameManager = gameDAO.loadGame(gameId);
        chessGameManager.move(Position.of(startPosition), Position.of(endPosition));

        gameDAO.updateTurnByGameId(chessGameManager, gameId);
        gameDAO.updatePiecesByGameId(chessGameManager, gameId);

        return new CommonDto<>("기물을 이동했습니다.", RunningGameDto.from(chessGameManager));
    }

    public CommonDto<GameListDto> loadGameList() {
        return new CommonDto<>("게임 목록을 불러왔습니다.", new GameListDto(gameDAO.loadGameList()));
    }

    public CommonDto<RunningGameDto> loadGame(int gameId) {
        ChessGameManager chessGameManager = gameDAO.loadGame(gameId);
        return new CommonDto<>("게임을 불러왔습니다", RunningGameDto.from(chessGameManager));
    }
}
