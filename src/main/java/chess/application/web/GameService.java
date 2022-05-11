package chess.application.web;

import static chess.view.Expressions.EXPRESSIONS_COLUMN;
import static chess.view.Expressions.EXPRESSIONS_ROW;

import chess.dao.BoardDao;
import chess.dao.GameDao;
import chess.domain.Camp;
import chess.domain.ChessGame;
import chess.domain.board.BoardInitializer;
import chess.domain.board.Position;
import chess.domain.gamestate.Score;
import chess.domain.piece.Piece;
import chess.domain.piece.Type;
import chess.dto.BoardResponse;
import chess.dto.GameRequest;
import chess.dto.GameResponse;
import chess.dto.MoveRequest;
import chess.dto.ResultResponse;
import chess.dto.StatusResponse;
import chess.entity.GameEntity;
import chess.entity.PieceEntity;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    private static final String ERROR_PASSWORD = "비밀번호가 일치하지 않습니다.";
    private static final String ERROR_IS_RUNNING = "실행 중인 게임은 삭제할 수 없습니다.";

    private static final int INDEX_COLUMN = 0;
    private static final int INDEX_ROW = 1;

    private final GameDao gameDao;
    private final BoardDao boardDao;

    public GameService(GameDao gameDao, BoardDao boardDao) {
        this.gameDao = gameDao;
        this.boardDao = boardDao;
    }

    public Long save(GameRequest gameRequest) {
        GameEntity gameEntity = gameRequest.toGameEntity();
        Long gameId = gameDao.save(gameEntity);
        boardDao.saveAll(gameId, BoardInitializer.get().getSquares());
        return gameId;
    }

    public StatusResponse findStatus(Long id) {
        ChessGame chessGame = findChessGameById(id);
        Map<Camp, Score> scores = chessGame.getScores();
        return new StatusResponse(scores.get(Camp.BLACK).getValue(), scores.get(Camp.WHITE).getValue());
    }

    private ChessGame findChessGameById(Long id) {
        List<PieceEntity> rawBoard = boardDao.findAllByGameId(id);
        Map<Position, Piece> board = rawBoard.stream()
                .collect(Collectors.toMap(pieceEntity -> parsePosition(pieceEntity.getPosition()),
                        this::parsePiece));
        return gameDao.findById(id).toChessGame(board);
    }

    public BoardResponse update(Long id, MoveRequest moveRequest) {
        ChessGame chessGame = findChessGameById(id);
        move(chessGame, moveRequest);
        gameDao.updateTurnById(id);
        boardDao.deleteAllByGameId(id);
        boardDao.saveAll(id, chessGame.getBoardSquares());

        return BoardResponse.of(chessGame.getBoardSquares());
    }

    private void move(ChessGame chessGame, MoveRequest moveRequest) {
        chessGame.move(parsePosition(moveRequest.getSource()), parsePosition(moveRequest.getTarget()));
    }

    private Position parsePosition(String rawPosition) {
        return Position.of(EXPRESSIONS_COLUMN.get(rawPosition.charAt(INDEX_COLUMN)),
                EXPRESSIONS_ROW.get(rawPosition.charAt(INDEX_ROW)));
    }

    public void delete(Long id, String password) {
        GameEntity gameEntity = gameDao.findById(id);
        if (gameEntity.incorrectPassword(password)) {
            throw new IllegalArgumentException(ERROR_PASSWORD);
        }
        if (!gameEntity.isFinished()) {
            throw new IllegalStateException(ERROR_IS_RUNNING);
        }
        boardDao.deleteAllByGameId(id);
        gameDao.deleteById(id);
    }

    public List<GameResponse> findAllGames() {
        return gameDao.findAll()
                .stream()
                .map(GameResponse::of)
                .collect(Collectors.toList());
    }

    public BoardResponse findBoardByGameId(Long id) {
        ChessGame chessGame = findChessGameById(id);
        return BoardResponse.of(chessGame.getBoardSquares());
    }

    private Piece parsePiece(PieceEntity piece) {
        String rawType = piece.getType();
        if (rawType.isBlank()) {
            rawType = "none";
        }
        Type type = Type.valueOf(rawType.toUpperCase());
        Camp camp = Camp.valueOf(piece.getCamp());
        return type.generatePiece(camp);
    }

    public ResultResponse end(Long id) {
        ChessGame chessGame = findChessGameById(id);
        chessGame.end();
        gameDao.updateStateById(id);
        Camp winner = chessGame.getWinner();
        if (winner == Camp.NONE) {
            return new ResultResponse(winner.toString(), true);
        }
        return new ResultResponse(winner.toString(), false);
    }
}
