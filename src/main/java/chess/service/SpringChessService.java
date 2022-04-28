package chess.service;

import chess.dao.ChessDao;
import chess.domain.board.Board;
import chess.domain.board.Position;
import chess.domain.piece.Color;
import chess.domain.piece.MoveResult;
import chess.domain.piece.Piece;
import chess.dto.GameCreateRequest;
import chess.dto.GameCreateResponse;
import chess.dto.GameDeleteRequest;
import chess.dto.GameDeleteResponse;
import chess.dto.GameDto;
import chess.dto.MoveRequest;
import chess.dto.MoveResponse;
import chess.dto.PositionDto;
import chess.exception.DeleteFailOnPlayingException;
import chess.exception.PasswordNotMatchedException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SpringChessService implements ChessService {

    private final ChessDao chessDao;
    private final PasswordEncoder passwordEncoder;

    public SpringChessService(ChessDao chessDao, PasswordEncoder passwordEncoder) {
        this.chessDao = chessDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public GameCreateResponse create(GameCreateRequest gameCreateRequest) {
        return new GameCreateResponse(chessDao.create(gameCreateRequest));
    }

    @Override
    public List<GameDto> findAll() {
        return chessDao.findAll();
    }

    @Override
    public GameDto findById(int id) {
        return chessDao.findById(id);
    }

    @Override
    public Map<Color, Double> findScoreById(int gameId) {
        final Board board = chessDao.findBoardByGameId(gameId);
        return board.getScore();
    }

    @Override
    public List<PositionDto> findPositionsById(int gameId) {
        final Board boardByGameId = chessDao.findBoardByGameId(gameId);
        final Map<Position, Piece> board = boardByGameId.getBoard();

        return board.entrySet()
                .stream()
                .map(entry -> PositionDto.of(entry.getKey().getName(), entry.getValue().getName()))
                .collect(Collectors.toList());
    }

    @Override
    public MoveResponse updateBoard(MoveRequest moveRequest) {
        final Board board = chessDao.findBoardByGameId(moveRequest.getGameId());
        MoveResult moveResult = move(board, moveRequest);
        if (moveResult != MoveResult.FAIL) {
            chessDao.updateBoardByMove(moveRequest);
            chessDao.changeTurnByGameId(moveRequest.getGameId());
        }
        if (moveResult == MoveResult.END) {
            chessDao.finishBoardById(moveRequest.getGameId());
        }
        return new MoveResponse(moveRequest.getPiece(), moveRequest.getFrom(), moveRequest.getTo(),
                moveResult);
    }

    private MoveResult move(Board board, MoveRequest moveRequest) {
        return board.move(Position.from(moveRequest.getFrom()), Position.from(moveRequest.getTo()));
    }

    @Override
    public GameDeleteResponse deleteById(GameDeleteRequest gameDeleteRequest) {
        validateFinished(gameDeleteRequest.getId());
        validatePassword(gameDeleteRequest.getId(), gameDeleteRequest.getPassword());
        return chessDao.deleteById(gameDeleteRequest.getId());
    }

    private void validateFinished(int id) {
        final Board boardByGameId = chessDao.findBoardByGameId(id);
        if (boardByGameId.isFinished()) {
            return;
        }

        throw new DeleteFailOnPlayingException();
    }

    private void validatePassword(int id, String password) {
        String hash = chessDao.findPasswordById(id);
        if (passwordEncoder.matches(password, hash)) {
            return;
        }

        throw new PasswordNotMatchedException();
    }
}
