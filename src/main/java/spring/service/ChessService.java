package spring.service;

import chess.board.ChessBoardCreater;
import chess.command.MoveCommand;
import chess.game.ChessGame;
import chess.location.Location;
import chess.progress.Progress;
import chess.team.Team;
import org.springframework.stereotype.Service;
import spring.dto.*;
import spring.entity.ChessGameEntity;
import spring.entity.repository.ChessGameRepository;
import spring.vo.ChessGameVo;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class ChessService {
    private final ChessGameRepository chessGameRepository;

    public ChessService(ChessGameRepository chessGameRepository) {
        this.chessGameRepository = chessGameRepository;
    }

    public ChessGameIdDto makeChessBoard() {
        ChessGame chessGame = new ChessGame(ChessBoardCreater.create(), Team.WHITE);

        // TODO : Save 확인 로직 필요!
        ChessGameEntity save = chessGameRepository.save(chessGame.toEntity());

        return new ChessGameIdDto(save.getId());
    }

    public ChessMoveDto move(long gameId, LocationDto nowDto, LocationDto destinationDto) throws SQLException {
        Optional<ChessGameEntity> optionalChessGameEntity = chessGameRepository.findById(gameId);

        ChessGameEntity chessGameEntity = optionalChessGameEntity.orElseThrow(
                () -> new SQLException("game id에 맞는 데이터가 존재하지 않습니다."));

        ChessGame chessGame = chessGameEntity.toChessGame();
        Location now = nowDto.toLocation();
        Location destination = destinationDto.toLocation();
        MoveCommand move = MoveCommand.of(now, destination, chessGame);
        Progress progress = chessGame.doOneCommand(move);

        saveChessGameIfProgressIsNotEnd(chessGameEntity, chessGame, progress);

        ChessGameScoresDto chessGameScoresDto = new ChessGameScoresDto(chessGame.calculateScores());

        return new ChessMoveDto(chessGameScoresDto, progress, chessGame.getTurn());
    }

    private void saveChessGameIfProgressIsNotEnd(ChessGameEntity chessGameEntity, ChessGame chessGame, Progress progress) {
        if (!progress.isError()) {
            chessGame.changeTurn();
            ChessGameEntity targetChessGameEntity = chessGame.toEntity(chessGameEntity.getId());
            chessGameRepository.save(targetChessGameEntity);
        }
    }

    public ChessGameDto resumeGame(Long gameId) throws SQLException {
        Optional<ChessGameEntity> optionalChessGameEntity = chessGameRepository.findById(gameId);

        ChessGameEntity chessGameEntity = optionalChessGameEntity.orElseThrow(
                () -> new SQLException("game id에 맞는 데이터가 존재하지 않습니다."));

        return new ChessGameDto(chessGameEntity.toChessGame());
    }

    public ChessResultDto findWinner(Long gameId) throws SQLException {
        Optional<ChessGameEntity> optionalChessGameEntity = chessGameRepository.findById(gameId);

        ChessGameEntity chessGameEntity = optionalChessGameEntity.orElseThrow(
                () -> new SQLException("game id에 맞는 데이터가 존재하지 않습니다."));

        ChessGame chessGame = chessGameEntity.toChessGame();
        return new ChessResultDto(chessGame.findWinner());
    }

    // deleteById 는 믿을 수 없다. 존재하지 않는 row를 삭제해도 에러를 띄우지 않는다.
    public void endGame(Long gameId) throws SQLException {
        Optional<ChessGameEntity> optionalChessGameEntity = chessGameRepository.findById(gameId);

        ChessGameEntity chessGameEntity = optionalChessGameEntity.orElseThrow(
                () -> new SQLException("game id에 맞는 데이터가 존재하지 않습니다."));

        chessGameRepository.delete(chessGameEntity);
    }

    public List<ChessGameVo> findAllGame() {
        List<ChessGameEntity> chessGameEntities = chessGameRepository.findAll();

        return new ChessGamesDto(chessGameEntities).getGames();
    }
}
