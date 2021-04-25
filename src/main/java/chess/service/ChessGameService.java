package chess.service;

import chess.domain.ChessGameManager;
import chess.domain.board.ChessBoard;
import chess.domain.piece.Color;
import chess.domain.position.Position;
import chess.dto.GameListDto;
import chess.dto.NewGameDto;
import chess.dto.RunningGameDto;
import chess.repository.GameRepository;
import chess.repository.PieceRepository;
import org.springframework.stereotype.Service;

@Service
public class ChessGameService {
    private final GameRepository gameRepository;
    private final PieceRepository pieceRepository;

    public ChessGameService(GameRepository gameRepository, PieceRepository pieceRepository) {
        this.gameRepository = gameRepository;
        this.pieceRepository = pieceRepository;
    }

    public NewGameDto createNewGame() {
        ChessGameManager chessGameManager = new ChessGameManager();
        chessGameManager.start();
        long gameId = gameRepository.save(chessGameManager);
        pieceRepository.savePieces(chessGameManager, gameId);
        return NewGameDto.from(chessGameManager, gameId);
    }

    public RunningGameDto move(long gameId, String from, String to) {
        ChessGameManager chessGameManager = loadChessGameByGameId(gameId);

        Position fromPosition = Position.of(from);
        Position toPosition = Position.of(to);

        chessGameManager.move(fromPosition, toPosition);

        pieceRepository.deletePieceByPosition(toPosition, gameId);
        pieceRepository.updatePiecePosition(fromPosition, toPosition, gameId);

        gameRepository.updateTurnByGameId(chessGameManager, gameId);
        if (chessGameManager.isEnd()) {
            gameRepository.delete(gameId);
        }

        return RunningGameDto.from(chessGameManager);
    }

    public ChessGameManager loadChessGameByGameId(long gameId) {
        ChessBoard chessBoard = pieceRepository.findChessBoardByGameId(gameId);
        Color currentTurn = gameRepository.findCurrentTurnByGameId(gameId);

        ChessGameManager chessGameManager = new ChessGameManager();
        chessGameManager.load(chessBoard, currentTurn);
        return chessGameManager;
    }

    public GameListDto loadAllGames() {
        return GameListDto.from(gameRepository.findAllGamesId());
    }
}
