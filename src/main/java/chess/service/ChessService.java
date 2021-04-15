package chess.service;

import chess.dao.PiecesDao;
import chess.domain.chessgame.ChessGame;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceType;
import chess.domain.position.Position;
import chess.dto.PiecesDto;
import chess.dto.RoomIdDto;
import chess.dto.request.BoardRequestDto;
import chess.dto.request.PiecesRequestDto;
import chess.dto.response.PieceResponseDto;
import chess.dto.response.PiecesResponseDto;
import chess.dto.response.RoomsResponseDto;
import chess.dto.response.ScoreResponseDto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.stereotype.Service;

@Service
public class ChessService {

    private final PiecesDao piecesDao;

    public ChessService(PiecesDao piecesDao) {
        this.piecesDao = piecesDao;
    }

    public PiecesResponseDto postPieces(PiecesRequestDto piecesRequestDto) {
        List<PiecesDto> piecesDtos = piecesDao.findPiecesByRoomId(piecesRequestDto.getRoomId());
        List<PieceResponseDto> alivePieces = new ArrayList<>();

        if (piecesDtos.size() == 0) {
            piecesDao.insertRoom(piecesRequestDto.getRoomId());
            alivePieces = makeNewBoard(piecesRequestDto.getRoomId());
            return new PiecesResponseDto(alivePieces);
        }

        for (PiecesDto piecesDto : piecesDtos) {
            alivePieces
                .add(new PieceResponseDto(piecesDto.getPosition(), piecesDto.getPieceName()));
        }

        return new PiecesResponseDto(alivePieces);
    }

    private List<PieceResponseDto> makeNewBoard(int roomId) {
        ChessGame chessGame = new ChessGame();
        List<PieceResponseDto> pieceResponseDtoList = new ArrayList<>();
        for (Entry<Position, Piece> entry : chessGame.pieces().entrySet()) {
            pieceResponseDtoList.add(
                new PieceResponseDto(entry.getKey().chessCoordinate(), entry.getValue().getName()));
            piecesDao.insertPieceByRoomId(new PiecesDto(roomId, entry.getValue().getName(), entry.getKey().chessCoordinate()));
        }
        return pieceResponseDtoList;
    }

    public PiecesResponseDto putBoard(BoardRequestDto boardRequestDto) {
        int roomId = boardRequestDto.getRoomId();
        ChessGame chessGame = makeChessGame(roomId);
        chessGame.move(new Position(boardRequestDto.getSource()), new Position(boardRequestDto.getTarget()));
        piecesDao.updateRoom(roomId, chessGame.getIsBlackTurn(), chessGame.isPlaying());

        List<PiecesDto> piecesDtos = new ArrayList<>();
        for(Entry<Position, Piece> entry : chessGame.pieces().entrySet()){
            piecesDtos.add(new PiecesDto(roomId, entry.getValue().getName(), entry.getKey().chessCoordinate()));
        }

        piecesDao.updatePiecesByRoomId(piecesDtos);
        List<PiecesDto> movedPieces = piecesDao.findPiecesByRoomId(roomId);
        List<PieceResponseDto> pieceResponseDtos = new ArrayList<>();
        for (PiecesDto movedPiece : movedPieces) {
            pieceResponseDtos.add(new PieceResponseDto(movedPiece.getPosition(), movedPiece.getPieceName()));
        }

        Color winnerColor = Color.NONE;
        if(!chessGame.isPlaying()){
            piecesDao.deleteAllPiecesByRoomId(roomId);
            piecesDao.deleteRoomById(roomId);
            if(chessGame.isKingAlive(Color.BLACK)) {
                winnerColor = Color.BLACK;
            }
            if(chessGame.isKingAlive(Color.WHITE)) {
                winnerColor = Color.WHITE;
            }
        }

        return new PiecesResponseDto(winnerColor, chessGame.isPlaying(), pieceResponseDtos);
    }

    public ScoreResponseDto getScore(int roomId, String colorName) {
        ChessGame chessGame = makeChessGame(roomId);

        System.out.println(chessGame.score(Color.valueOf(colorName)).getValue());
        return new ScoreResponseDto(chessGame.score(Color.valueOf(colorName)));
    }

    public RoomsResponseDto getRooms() {
        List<RoomIdDto> roomIdDtos = piecesDao.findAllRoomId();
        List<Integer> roomIds = new ArrayList<>();
        for (RoomIdDto roomIdDto : roomIdDtos) {
            roomIds.add(roomIdDto.getId());
        }
        return new RoomsResponseDto(roomIds);
    }

    private ChessGame makeChessGame(int roomId){
        List<PiecesDto> piecesDtos = piecesDao.findPiecesByRoomId(roomId);
        Map<Position, Piece> board = new HashMap<>();
        Color turn = piecesDao.findTurnByRoomId(roomId);
        boolean isPlaying = piecesDao.findPlayingFlagByRoomId(roomId);

        for (PiecesDto piecesDto : piecesDtos) {
            board.put(new Position(piecesDto.getPosition()),
                PieceType.findPiece(piecesDto.getPieceName()));
        }

        return new ChessGame(board, isPlaying, turn);
    }

}
