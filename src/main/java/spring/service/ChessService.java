package spring.service;

import chess.board.ChessBoardCreater;
import chess.command.MoveCommand;
import chess.game.ChessGame;
import chess.location.Location;
import chess.piece.type.Piece;
import chess.progress.Progress;
import chess.team.Team;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;
import spark.dto.LocationDto;
import spring.dto.BoardDto;
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
    private static final Gson GSON = new Gson();

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

        BoardDto boardDto = new BoardDto(savedChessGame.getChessBoard());
        boolean isTurnBlack = savedChessGame.getTurn().isBlack();
        double whiteScore = savedChessGame.calculateScores().getWhiteScore().getValue();
        double blackScore = savedChessGame.calculateScores().getBlackScore().getValue();

        return new ChessGameDto(boardDto, isTurnBlack, whiteScore, blackScore);
    }

    public ChessMoveDto move(long gameId, LocationDto nowDto, LocationDto destinationDto) throws SQLException {
        Optional<ChessGameEntity> chessGameEntity = chessGameRepository.findById(gameId);

        if (!chessGameEntity.isPresent()) {
            throw new SQLException("game id에 맞는 데이터가 존재하지 않습니다.");
        }

        ChessGameEntity chessGameEntity2 = chessGameEntity.get();
        ChessGame chessGame = chessGameEntity2.toChessGame();
        Location now = nowDto.toLocation();
        Location destination = destinationDto.toLocation();

        MoveCommand move = MoveCommand.of(now, destination, chessGame);

        Piece nowPiece = chessGame.getPiece(now);

        Progress progress = chessGame.doOneCommand(move);

        if (!progress.isError()) {
            ChessGameEntity chessGameEntity1 = chessGame.toEntity();
            chessGameEntity1.setId(chessGameEntity2.getId());
            chessGameRepository.save(chessGameEntity1);
            chessGame.changeTurn();
        }

        return new ChessMoveDto(new ChessGameScoresDto(chessGame.calculateScores()),
                progress,
                chessGame.getTurn());
    }
}
