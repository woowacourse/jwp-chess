package chess.service;

import chess.dao.ChessDao;
import chess.domain.board.Board;
import chess.domain.board.BoardFactory;
import chess.domain.board.Position;
import chess.domain.piece.Color;
import chess.domain.piece.MoveResult;
import chess.dto.GameRoomDto;
import chess.dto.MoveRequestDto;
import chess.dto.MoveResultDto;
import chess.dto.NewGameRequest;
import chess.dto.NewGameResponse;
import chess.dto.PositionDto;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class SpringChessService implements ChessService {

    private final ChessDao chessDao;

    public SpringChessService(ChessDao chessDao) {
        this.chessDao = chessDao;
    }

    @Override
    public List<PositionDto> getBoardByGameId(String gameId) {
        return chessDao.getBoardByGameId(gameId)
                .entrySet()
                .stream()
                .map(entry -> PositionDto.of(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public MoveResultDto move(MoveRequestDto moveRequestDto) {
        final Board board = findBoardByGameId(moveRequestDto.getGameId());
        MoveResult moveResult = move(board, moveRequestDto);
        if (moveResult != MoveResult.FAIL) {
            chessDao.move(moveRequestDto);
        }
        if (moveResult == MoveResult.END) {
            chessDao.setFinishedById(moveRequestDto.getGameId());
        }
        return new MoveResultDto(moveRequestDto.getPiece(), moveRequestDto.getFrom(), moveRequestDto.getTo(),
                moveResult);
    }

    private Board findBoardByGameId(String gameId) {
        final Map<String, String> boardByGameId = chessDao.getBoardByGameId(gameId);
        final Color color = Color.from(chessDao.getTurnByGameId(gameId));
        return BoardFactory.newInstance(boardByGameId, color);
    }

    private MoveResult move(Board board, MoveRequestDto moveRequestDto) {
        return board.move(Position.from(moveRequestDto.getFrom()), Position.from(moveRequestDto.getTo()));
    }

    @Override
    public boolean isFinished(String gameId) {
        final Map<String, String> boardByGameId = chessDao.getBoardByGameId(gameId);
        final Board board = BoardFactory.newInstance(boardByGameId, Color.WHITE);
        return board.isFinished();
    }

    @Override
    public Map<Color, Double> getScore(String gameId) {
        return findBoardByGameId(gameId).getScore();
    }

    @Override
    public NewGameResponse createNewGame(NewGameRequest newGameRequest) {
        return new NewGameResponse(chessDao.createNewGame(newGameRequest));
    }

    @Override
    public List<GameRoomDto> findGamesOnPlay() {
        return chessDao.findGamesOnPlay();
    }

    @Override
    public GameRoomDto findGameById(int id) {
        return chessDao.findGameById(id);
    }

    @Override
    public int deleteGameById(int id) {
        return chessDao.deleteGameById(id);
    }

    @Override
    public List<GameRoomDto> findGames() {
        final List<GameRoomDto> games = chessDao.findGames();
        return games;
    }
}
