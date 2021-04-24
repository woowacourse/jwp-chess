package chess.service;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import chess.dao.ChessRepository;
import chess.domain.board.Score;
import chess.domain.chessgame.ChessGame;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.position.Position;

@Service
public class ChessService {
    private final ChessRepository chessRepository;

    public ChessService(ChessRepository chessRepository) {
        this.chessRepository = chessRepository;
    }

    @Transactional
    public Map<Position, Piece> postPieces(int roomId) {
        Map<Position, Piece> boardInfo = chessRepository.findPiecesByRoomId(roomId);

        if (CollectionUtils.isEmpty(boardInfo)) {
            chessRepository.insertRoom(roomId);
            return makeNewBoard(roomId);
        }

        return boardInfo;
    }

    private Map<Position, Piece> makeNewBoard(int roomId) {
        ChessGame chessGame = new ChessGame();

        for (Entry<Position, Piece> entry : chessGame.pieces().entrySet()) {
            chessRepository.insertPieceByRoomId(roomId, entry.getValue().getName(), entry.getKey().chessCoordinate());
        }

        return chessRepository.findPiecesByRoomId(roomId);
    }

    @Transactional
    public ChessGame putBoard(int roomId, Position source, Position target) {
        ChessGame chessGame = makeChessGame(roomId);
        chessGame.move(source, target);

        chessRepository.updateRoom(roomId, chessGame.getIsBlackTurn(), chessGame.isPlaying());
        chessRepository.updatePiecesByRoomId(roomId, chessGame.pieces());

        return new ChessGame(chessGame.pieces(), chessGame.isPlaying(), makeWinnerColor(roomId, chessGame));
    }

    private Color makeWinnerColor(int roomId, ChessGame chessGame) {
        Color winnerColor = Color.NONE;
        if (!chessGame.isPlaying()) {
            winnerColor = Color.valueOf(chessRepository.findTurnByRoomId(roomId));
            chessRepository.deleteAllPiecesByRoomId(roomId);
            chessRepository.deleteRoomById(roomId);
        }
        return winnerColor;
    }

    @Transactional
    public Score getScore(int roomId, String colorName) {
        ChessGame chessGame = makeChessGame(roomId);
        return chessGame.score(Color.valueOf(colorName));
    }

    @Transactional
    public List<Integer> getRooms() {
        return chessRepository.findAllRoomId();
    }

    private ChessGame makeChessGame(int roomId) {
        Map<Position, Piece> board = chessRepository.findPiecesByRoomId(roomId);
        Color turn = Color.valueOf(chessRepository.findTurnByRoomId(roomId));
        boolean isPlaying = chessRepository.findPlayingFlagByRoomId(roomId);

        return new ChessGame(board, isPlaying, turn);
    }

}

