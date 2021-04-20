package chess.service;

import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import chess.dao.ChessRepository;
import chess.domain.chessgame.ChessGame;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.dto.request.MoveRequestDto;
import chess.dto.request.PiecesRequestDto;
import chess.dto.response.PiecesResponseDto;
import chess.dto.response.PiecesResponsesDto;
import chess.dto.response.RoomsResponseDto;
import chess.dto.response.ScoreResponseDto;

@Service
public class ChessService {

    private final ChessRepository chessRepository;

    public ChessService(ChessRepository chessRepository) {
        this.chessRepository = chessRepository;
    }

    @Transactional
    public PiecesResponseDto postPieces(PiecesRequestDto piecesRequestDto) {
        Map<Position, Piece> boardInfo = chessRepository.findPiecesByRoomId(piecesRequestDto.getRoomId());
        PiecesResponsesDto piecesResponsesDto = new PiecesResponsesDto(boardInfo);

        if (CollectionUtils.isEmpty(boardInfo)) {
            chessRepository.insertRoom(piecesRequestDto.getRoomId());
            return new PiecesResponseDto(makeNewBoard(piecesRequestDto.getRoomId()));
        }

        return new PiecesResponseDto(piecesResponsesDto);
    }

    private PiecesResponsesDto makeNewBoard(int roomId) {
        ChessGame chessGame = new ChessGame();

        for (Entry<Position, Piece> entry : chessGame.pieces().entrySet()) {
            chessRepository.insertPieceByRoomId(roomId, entry.getValue().getName(), entry.getKey().chessCoordinate());
        }

        return new PiecesResponsesDto(chessRepository.findPiecesByRoomId(roomId));
    }

    @Transactional
    public PiecesResponseDto putBoard(MoveRequestDto moveRequestDto) {
        int roomId = moveRequestDto.getRoomId();
        ChessGame chessGame = makeChessGame(roomId);
        chessGame.move(new Position(moveRequestDto.getSource()), new Position(moveRequestDto.getTarget()));

        chessRepository.updateRoom(roomId, chessGame.getIsBlackTurn(), chessGame.isPlaying());
        chessRepository.updatePiecesByRoomId(moveRequestDto.getRoomId(), chessGame.pieces());

        return new PiecesResponseDto(makeWinnerColor(roomId, chessGame), chessGame.isPlaying(),
            chessRepository.findPiecesByRoomId(roomId));
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
    public ScoreResponseDto getScore(int roomId, String colorName) {
        ChessGame chessGame = makeChessGame(roomId);
        return new ScoreResponseDto(chessGame.score(Color.valueOf(colorName)));
    }

    @Transactional
    public RoomsResponseDto getRooms() {
        return new RoomsResponseDto(chessRepository.findAllRoomId());
    }

    private ChessGame makeChessGame(int roomId) {
        Map<Position, Piece> board = chessRepository.findPiecesByRoomId(roomId);
        Color turn = Color.valueOf(chessRepository.findTurnByRoomId(roomId));
        boolean isPlaying = chessRepository.findPlayingFlagByRoomId(roomId);

        return new ChessGame(board, isPlaying, turn);
    }

}

