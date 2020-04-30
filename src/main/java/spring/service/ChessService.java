package spring.service;

import chess.board.ChessBoardCreater;
import chess.command.MoveCommand;
import chess.game.ChessGame;
import chess.location.Location;
import chess.progress.Progress;
import chess.team.Team;
import org.springframework.stereotype.Service;
import spark.dto.LocationDto;
import spring.dto.ChessGameDto;
import spring.dto.ChessGameScoresDto;
import spring.dto.ChessMoveDto;
import spring.entity.ChessGameEntity;
import spring.entity.repository.ChessGameRepository;
import spring.entity.repository.PieceRepository;

import java.sql.SQLException;
import java.util.Optional;

@Service
public class ChessService {

    private PieceRepository pieceRepository;
    private ChessGameRepository chessGameRepository;

    public ChessService(PieceRepository pieceRepository, ChessGameRepository chessGameRepository) {
        this.pieceRepository = pieceRepository;
        this.chessGameRepository = chessGameRepository;
    }

    public ChessGameDto makeChessBoard() {
        ChessGame chessGame = new ChessGame(ChessBoardCreater.create(), Team.WHITE);

        ChessGameEntity save = chessGameRepository.save(chessGame.toEntity());

        ChessGame savedChessGame = save.toChessGame();

        return new ChessGameDto(savedChessGame);
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
            ChessGameEntity targetChessGameEntity = chessGame.toEntity();
            targetChessGameEntity.setId(chessGameEntity.getId());
            chessGameRepository.save(targetChessGameEntity);
        }
    }

    public ChessGameDto resumeGame() throws SQLException {
        Optional<ChessGameEntity> optionalChessGameEntity = chessGameRepository.findById(1L);

        ChessGameEntity chessGameEntity = optionalChessGameEntity.orElseThrow(
                () -> new SQLException("game id에 맞는 데이터가 존재하지 않습니다."));

        return new ChessGameDto(chessGameEntity.toChessGame());
    }

    public void endGame(Long gameId) throws SQLException {
        // deleteById 는 믿을 수 없다. 존재하지 않는 row를 삭제해도 에러를 띄우지 않는다.
        Optional<ChessGameEntity> optionalChessGameEntity = chessGameRepository.findById(gameId);

        ChessGameEntity chessGameEntity = optionalChessGameEntity.orElseThrow(
                () -> new SQLException("game id에 맞는 데이터가 존재하지 않습니다."));

        chessGameRepository.delete(chessGameEntity);
    }
}
