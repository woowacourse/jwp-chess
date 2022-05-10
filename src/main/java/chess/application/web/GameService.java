package chess.application.web;

import static chess.view.Expressions.EXPRESSIONS_COLUMN;
import static chess.view.Expressions.EXPRESSIONS_ROW;

import chess.dao.BoardDao;
import chess.dao.GameDao;
import chess.domain.Camp;
import chess.domain.ChessGame;
import chess.domain.board.Position;
import chess.domain.gamestate.Score;
import chess.domain.piece.Piece;
import chess.domain.piece.Type;
import chess.dto.BoardResponse;
import chess.dto.GameDto;
import chess.dto.GameRequest;
import chess.dto.MoveRequest;
import chess.dto.PieceDto;
import chess.dto.ResultResponse;
import chess.dto.StatusResponse;
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

    private final ChessGame chessGame;
    private final GameDao gameDao;
    private final BoardDao boardDao;

    public GameService(GameDao gameDao, BoardDao boardDao) {
        this.chessGame = new ChessGame();
        this.gameDao = gameDao;
        this.boardDao = boardDao;
    }

    public Long save(GameRequest gameRequest) {
        ChessGame newChessGame = gameRequest.toChessGame();
        newChessGame.start();
        Long gameId = gameDao.save(newChessGame);
        boardDao.saveAll(gameId, newChessGame.getBoardSquares());
        return gameId;
    }

    public StatusResponse findStatus() {
        Map<Camp, Score> scores = chessGame.getScores();
        return new StatusResponse(scores.get(Camp.BLACK).getValue(), scores.get(Camp.WHITE).getValue());
    }

    public BoardResponse update(Long id, MoveRequest moveRequest) {
        move(moveRequest);
        gameDao.updateTurnById(id);
        boardDao.deleteAllByGameId(id);
        boardDao.saveAll(id, chessGame.getBoardSquares());
        return BoardResponse.of(chessGame.getBoardSquares());
    }

    private void move(MoveRequest moveRequest) {
        chessGame.move(parsePosition(moveRequest.getSource()), parsePosition(moveRequest.getTarget()));
    }

    private Position parsePosition(String rawPosition) {
        return Position.of(EXPRESSIONS_COLUMN.get(rawPosition.charAt(INDEX_COLUMN)),
                EXPRESSIONS_ROW.get(rawPosition.charAt(INDEX_ROW)));
    }

    public void delete(Long id, String password) {
        ChessGame savedChessGame = gameDao.findById(id);
        if (savedChessGame.incorrectPassword(password)) {
            throw new IllegalArgumentException(ERROR_PASSWORD);
        }
        if (gameDao.findRunningById(id)) {
            throw new IllegalStateException(ERROR_IS_RUNNING);
        }
        boardDao.deleteAllByGameId(id);
        gameDao.deleteById(id);
    }

    public List<GameDto> findAllGames() {
        return gameDao.findAll();
    }

    public BoardResponse findBoardByGameId(Long id) {
        List<PieceDto> rawBoard = boardDao.findAllByGameId(id);
        Map<Position, Piece> board = rawBoard.stream()
                .collect(Collectors.toMap(
                        pieceDto -> parsePosition(pieceDto.getPosition()),
                        this::parsePiece
                ));
        chessGame.load(board, gameDao.isWhiteTurn(id));
        return BoardResponse.of(chessGame.getBoardSquares());
    }

    private Piece parsePiece(PieceDto piece) {
        String rawType = piece.getType();
        if (rawType.isBlank()) {
            rawType = "none";
        }
        Type type = Type.valueOf(rawType.toUpperCase());
        Camp camp = Camp.valueOf(piece.getCamp().toUpperCase());
        return type.generatePiece(camp);
    }

    public ResultResponse end(Long id) {
        chessGame.end();
        gameDao.updateStateById(id);
        Camp winner = chessGame.getWinner();
        if (winner == Camp.NONE) {
            return new ResultResponse(winner.toString(), true);
        }
        return new ResultResponse(winner.toString(), false);
    }
}
