package chess.service;

import java.util.List;
import java.util.Map.Entry;

import org.springframework.stereotype.Service;

import chess.dao.BoardDao;
import chess.dao.GameDao;
import chess.domain.board.Board;
import chess.domain.game.ChessGame;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.dto.request.CreatePieceDto;
import chess.dto.request.DeletePieceDto;
import chess.dto.request.UpdatePiecePositionDto;
import chess.dto.response.BoardDto;
import chess.dto.response.ChessGameDto;
import chess.dto.response.PieceColorDto;
import chess.dto.response.RoomDto;
import chess.dto.response.ScoreResultDto;

@Service
public class ChessService {
    private final GameDao gameDao;
    private final BoardDao boardDao;

    public ChessService(GameDao gameDao, BoardDao boardDao) {
        this.gameDao = gameDao;
        this.boardDao = boardDao;
    }

    public int createGame(String gameName, String gamePassword) {
        int id = gameDao.createGame(gameName, gamePassword);

        Board initializedBoard = Board.createInitializedBoard();
        for (Entry<Position, Piece> entry : initializedBoard.getValue().entrySet()) {
            Position position = entry.getKey();
            Piece piece = entry.getValue();

            CreatePieceDto createPieceDto = CreatePieceDto.of(id, position, piece);
            boardDao.createPiece(createPieceDto);
        }
        return id;
    }

    public void movePiece(UpdatePiecePositionDto updatePiecePositionDto) {
        int gameId = updatePiecePositionDto.getGameId();

        ChessGame chessGame = generateChessGame(gameId);
        chessGame.movePiece(updatePiecePositionDto.getFrom(), updatePiecePositionDto.getTo());

        updateGameTurn(gameId, chessGame);
        updatePiecePosition(updatePiecePositionDto, gameId);
    }

    private void updatePiecePosition(UpdatePiecePositionDto updatePiecePositionDto, int gameId) {
        boardDao.deletePiece(DeletePieceDto.of(gameId, updatePiecePositionDto.getTo()));
        boardDao.updatePiecePosition(updatePiecePositionDto);
    }

    private void updateGameTurn(int gameId, ChessGame chessGame) {
        if (chessGame.isWhiteTurn()) {
            gameDao.updateTurnToWhite(gameId);
            return;
        }

        gameDao.updateTurnToBlack(gameId);
    }

    public PieceColorDto getCurrentTurn(int gameId) {
        return PieceColorDto.from(generateChessGame(gameId));
    }

    public ScoreResultDto getScore(int gameId) {
        return ScoreResultDto.from(generateChessGame(gameId));
    }

    public PieceColorDto getWinColor(int gameId) {
        ChessGame chessGame = generateChessGame(gameId);
        return PieceColorDto.from(chessGame.getWinColor());
    }

    private ChessGame generateChessGame(int gameId) {
        BoardDto boardDto = boardDao.getBoard(gameId);
        ChessGameDto chessGameDto = gameDao.getGame(gameId);

        return ChessGame.of(boardDto.toBoard(), chessGameDto.getCurrentTurnAsPieceColor());
    }

    public BoardDto getBoard(int gameId) {
        return boardDao.getBoard(gameId);
    }

    public List<RoomDto> getRooms() {
        return gameDao.inquireAllRooms();
    }

    public void deleteRoom(int gameId, String inputPassword) {
        gameDao.checkCanDelete(gameId, inputPassword);
        ChessGame chessGame = generateChessGame(gameId);
        if (!chessGame.isEnd()) {
            throw new IllegalArgumentException("게임이 아직 안끝났습니다!");
        }

        boardDao.deletePieces(gameId);
        gameDao.deleteGame(gameId);
    }

    @Override
    public String toString() {
        return "ChessService{" +
            "gameDao=" + gameDao +
            ", boardDao=" + boardDao +
            '}';
    }

}
