package chess.service;

import chess.dao.PiecesDao;
import chess.domain.chessgame.ChessGame;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceFactory;
import chess.domain.position.Position;
import chess.dto.PieceDto;
import chess.dto.PiecesDto;
import chess.dto.RoomIdDto;
import chess.dto.request.BoardRequestDto;
import chess.dto.request.PiecesRequestDto;
import chess.dto.response.PiecesResponseDto;
import chess.dto.response.PiecesResponsesDto;
import chess.dto.response.RoomsResponseDto;
import chess.dto.response.ScoreResponseDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChessService {

    private final PiecesDao piecesDao;

    public ChessService(PiecesDao piecesDao) {
        this.piecesDao = piecesDao;
    }

    @Transactional
    public PiecesResponseDto postPieces(PiecesRequestDto piecesRequestDto) {
        PiecesDto piecesDto = new PiecesDto(
            piecesDao.findPiecesByRoomId(new RoomIdDto(piecesRequestDto.getRoomId())));
        PiecesResponsesDto piecesResponsesDto = new PiecesResponsesDto(piecesDto);

        if (piecesDto.getPieceDtos().size() == 0) {
            piecesDao.insertRoom(new RoomIdDto(piecesRequestDto.getRoomId()));
            return new PiecesResponseDto(makeNewBoard(piecesRequestDto.getRoomId()));
        }

        return new PiecesResponseDto(piecesResponsesDto);
    }

    private PiecesResponsesDto makeNewBoard(int roomId) {
        ChessGame chessGame = new ChessGame();

        for (Entry<Position, Piece> entry : chessGame.pieces().entrySet()) {
            piecesDao.insertPieceByRoomId(new PieceDto(roomId, entry));
        }

        return new PiecesResponsesDto(new PiecesDto(piecesDao.findPiecesByRoomId(new RoomIdDto(roomId))));
    }

    @Transactional
    public PiecesResponseDto putBoard(BoardRequestDto boardRequestDto) {
        int roomId = boardRequestDto.getRoomId();
        ChessGame chessGame = makeChessGame(roomId);
        chessGame.move(new Position(boardRequestDto.getSource()),
            new Position(boardRequestDto.getTarget()));

        piecesDao.updateRoom(roomId, chessGame.getIsBlackTurn(), chessGame.isPlaying());
        piecesDao.updatePiecesByRoomId(new PiecesDto(boardRequestDto.getRoomId(), chessGame.pieces()));

        return new PiecesResponseDto(makeWinnerColor(roomId, chessGame), chessGame.isPlaying(),
            new PiecesDto(piecesDao.findPiecesByRoomId(new RoomIdDto(roomId))));
    }

    private Color makeWinnerColor(int roomId, ChessGame chessGame) {
        Color winnerColor = Color.NONE;

        if (!chessGame.isPlaying()) {
            RoomIdDto roomIdDto = new RoomIdDto(roomId);
            winnerColor = Color.valueOf(piecesDao.findTurnByRoomId(roomIdDto));
            piecesDao.deleteAllPiecesByRoomId(roomIdDto);
            piecesDao.deleteRoomById(roomIdDto);
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
        List<RoomIdDto> roomIdDtos = piecesDao.findAllRoomId();
        return new RoomsResponseDto(roomIdDtos.stream()
            .map(RoomIdDto::getId)
            .collect(Collectors.toList())
        );
    }

    private ChessGame makeChessGame(int roomId) {
        PiecesDto piecesDtos = new PiecesDto(piecesDao.findPiecesByRoomId(new RoomIdDto(roomId)));
        Map<Position, Piece> board = new HashMap<>();
        Color turn = Color.valueOf(piecesDao.findTurnByRoomId(new RoomIdDto(roomId)));
        boolean isPlaying = piecesDao.findPlayingFlagByRoomId(new RoomIdDto(roomId));

        for (PieceDto pieceDto : piecesDtos.getPieceDtos()) {
            board.put(new Position(pieceDto.getPosition()),
                PieceFactory.of(pieceDto.getPieceName()));
        }

        return new ChessGame(board, isPlaying, turn);
    }

}

