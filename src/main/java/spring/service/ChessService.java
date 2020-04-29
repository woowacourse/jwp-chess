package spring.service;

import chess.board.ChessBoardCreater;
import chess.game.ChessGame;
import chess.team.Team;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;
import spring.dto.BoardDto;
import spring.dto.ChessGameDto;
import spring.entity.ChessGameEntity;
import spring.entity.repository.ChessGameRepository;
import spring.entity.repository.PieceRepository;

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
}
