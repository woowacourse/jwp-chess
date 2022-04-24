package chess.service;

import chess.dao.BoardDao;
import chess.dao.GameDao;
import chess.domain.board.Board;
import chess.domain.game.ChessGame;
import chess.domain.game.GameId;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.dto.request.CreatePieceDto;
import chess.dto.request.DeletePieceDto;
import chess.dto.response.BoardDto;
import chess.dto.response.ChessGameDto;
import chess.dto.response.PieceColorDto;
import chess.dto.response.ScoreResultDto;
import java.util.Map.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChessService {
    private final GameDao gameDao;
    private final BoardDao boardDao;

    @Autowired
    public ChessService(GameDao gameDao, BoardDao boardDao) {
        this.gameDao = gameDao;
        this.boardDao = boardDao;
    }

    public void initializeGame(GameId gameId) {
        gameDao.deleteGame(gameId);
        createGame(gameId);
    }

    public void createGame(GameId gameId) {
        gameDao.createGame(gameId);

        Board initializedBoard = Board.createInitializedBoard();
        for (Entry<Position, Piece> entry : initializedBoard.getValue().entrySet()) {
            Position position = entry.getKey();
            Piece piece = entry.getValue();

            CreatePieceDto createPieceDto = CreatePieceDto.of(gameId, position, piece);
            boardDao.createPiece(createPieceDto);
        }
    }

    public void movePiece(GameId gameId, Position from, Position to) {
        ChessGame chessGame = generateChessGame(gameId);
        chessGame.movePiece(from, to);

        updateGameTurn(gameId, chessGame);
        updatePiecePosition(gameId, from, to);
    }

    private void updatePiecePosition(GameId gameId, Position from, Position to) {
        boardDao.deletePiece(DeletePieceDto.of(gameId, to));
        boardDao.updatePiecePosition(gameId, from, to);
    }

    private void updateGameTurn(GameId gameId, ChessGame chessGame) {
        if (chessGame.isWhiteTurn()) {
            gameDao.updateTurnToWhite(gameId);
            return;
        }

        gameDao.updateTurnToBlack(gameId);
    }

    public PieceColorDto getCurrentTurn(GameId gameId) {
        return PieceColorDto.from(generateChessGame(gameId));
    }

    public ScoreResultDto getScore(GameId gameId) {
        return ScoreResultDto.from(generateChessGame(gameId));
    }

    public PieceColorDto getWinColor(GameId gameId) {
        ChessGame chessGame = generateChessGame(gameId);
        return PieceColorDto.from(chessGame.getWinColor());
    }

    private ChessGame generateChessGame(GameId gameId) {
        BoardDto boardDto = boardDao.getBoard(gameId);
        ChessGameDto chessGameDto = gameDao.getGame(gameId);

        return ChessGame.of(boardDto.toBoard(), chessGameDto.getCurrentTurnAsPieceColor());
    }

    public BoardDto getBoard(GameId gameId) {
        return boardDao.getBoard(gameId);
    }

    @Override
    public String toString() {
        return "ChessService{" +
                "gameDao=" + gameDao +
                ", boardDao=" + boardDao +
                '}';
    }
}
