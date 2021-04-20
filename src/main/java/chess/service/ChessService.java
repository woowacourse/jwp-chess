package chess.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import chess.dao.ChessDao;
import chess.domain.chessgame.ChessGame;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceFactory;
import chess.domain.position.Position;
import chess.dto.PieceDto;
import chess.dto.PiecesDto;
import chess.dto.RoomIdDto;
import chess.dto.request.MoveRequestDto;
import chess.dto.request.PiecesRequestDto;
import chess.dto.response.PiecesResponseDto;
import chess.dto.response.PiecesResponsesDto;
import chess.dto.response.RoomsResponseDto;
import chess.dto.response.ScoreResponseDto;

@Service
public class ChessService {

    private final ChessDao chessDao;

    public ChessService(ChessDao chessDao) {
        this.chessDao = chessDao;
    }

    @Transactional
    public PiecesResponseDto postPieces(PiecesRequestDto piecesRequestDto) {
        PiecesDto piecesDto = new PiecesDto(chessDao.findPiecesByRoomId(new RoomIdDto(piecesRequestDto.getRoomId())));
        PiecesResponsesDto piecesResponsesDto = new PiecesResponsesDto(piecesDto);

        if (CollectionUtils.isEmpty(piecesDto.getPieceDtos())) {
            chessDao.insertRoom(new RoomIdDto(piecesRequestDto.getRoomId()));
            return new PiecesResponseDto(makeNewBoard(piecesRequestDto.getRoomId()));
        }

        return new PiecesResponseDto(piecesResponsesDto);
    }

    private PiecesResponsesDto makeNewBoard(int roomId) {
        ChessGame chessGame = new ChessGame();

        for (Entry<Position, Piece> entry : chessGame.pieces().entrySet()) {
            chessDao.insertPieceByRoomId(new PieceDto(roomId, entry));
        }

        return new PiecesResponsesDto(new PiecesDto(chessDao.findPiecesByRoomId(new RoomIdDto(roomId))));
    }

    @Transactional
    public PiecesResponseDto putBoard(MoveRequestDto moveRequestDto) {
        int roomId = moveRequestDto.getRoomId();
        ChessGame chessGame = makeChessGame(roomId);
        chessGame.move(new Position(moveRequestDto.getSource()), new Position(moveRequestDto.getTarget()));

        chessDao.updateRoom(roomId, chessGame.getIsBlackTurn(), chessGame.isPlaying());
        chessDao.updatePiecesByRoomId(new PiecesDto(moveRequestDto.getRoomId(), chessGame.pieces()));

        return new PiecesResponseDto(makeWinnerColor(roomId, chessGame), chessGame.isPlaying(),
            new PiecesDto(chessDao.findPiecesByRoomId(new RoomIdDto(roomId))));
    }

    private Color makeWinnerColor(int roomId, ChessGame chessGame) {
        Color winnerColor = Color.NONE;

        if (!chessGame.isPlaying()) {
            RoomIdDto roomIdDto = new RoomIdDto(roomId);
            winnerColor = Color.valueOf(chessDao.findTurnByRoomId(roomIdDto));
            chessDao.deleteAllPiecesByRoomId(roomIdDto);
            chessDao.deleteRoomById(roomIdDto);
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
        List<RoomIdDto> roomIdDtos = chessDao.findAllRoomId();
        return new RoomsResponseDto(roomIdDtos.stream()
            .map(RoomIdDto::getId)
            .collect(Collectors.toList())
        );
    }

    private ChessGame makeChessGame(int roomId) {
        PiecesDto piecesDtos = new PiecesDto(chessDao.findPiecesByRoomId(new RoomIdDto(roomId)));
        Map<Position, Piece> board = new HashMap<>();
        Color turn = Color.valueOf(chessDao.findTurnByRoomId(new RoomIdDto(roomId)));
        boolean isPlaying = chessDao.findPlayingFlagByRoomId(new RoomIdDto(roomId));

        for (PieceDto pieceDto : piecesDtos.getPieceDtos()) {
            board.put(new Position(pieceDto.getPosition()),
                PieceFactory.of(pieceDto.getPieceName()));
        }

        return new ChessGame(board, isPlaying, turn);
    }

}

