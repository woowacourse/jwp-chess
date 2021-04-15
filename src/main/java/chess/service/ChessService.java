package chess.service;

import chess.dao.PiecesDao;
import chess.domain.chessgame.ChessGame;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
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
import java.util.List;
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

        if(piecesDtos.size() == 0) {
            piecesDao.insertRoom(piecesRequestDto.getRoomId()); //나중에 없어도 될 듯??
            alivePieces = makeNewBoard();
            return new PiecesResponseDto(alivePieces);
        }

        for(PiecesDto piecesDto : piecesDtos){
            alivePieces.add(new PieceResponseDto(piecesDto.getPosition(), piecesDto.getPiece_name()));
        }

        return new PiecesResponseDto(alivePieces);
    }

    private List<PieceResponseDto> makeNewBoard() {
        ChessGame chessGame = new ChessGame();
        List<PieceResponseDto> pieceResponseDtoList = new ArrayList<>();
        for(Entry<Position, Piece> entry: chessGame.pieces().entrySet()) {
            if(entry.getValue().isEmpty()) {
                continue;
            }
            pieceResponseDtoList.add(new PieceResponseDto(entry.getKey().chessCoordinate(), entry.getValue().getName()));
        }
        return pieceResponseDtoList;
    }

    public void putBoard(BoardRequestDto boardRequestDto, ChessGame chessGame) {
        chessGame.move(new Position(boardRequestDto.getSource()), new Position(boardRequestDto.getTarget()));
     //   return getPieces(piecesRequestDto);
    }

    public ScoreResponseDto getScore(String colorName, ChessGame chessGame) {
        return new ScoreResponseDto(chessGame.score(Color.valueOf(colorName)));
    }

    public RoomsResponseDto getRooms() {
        List<RoomIdDto> roomIdDtos = piecesDao.findAllRoomId();
        List<Integer> roomIds = new ArrayList<>();
        for (RoomIdDto roomIdDto : roomIdDtos){
            roomIds.add(roomIdDto.getId());
        }
        return new RoomsResponseDto(roomIds);
    }
}
