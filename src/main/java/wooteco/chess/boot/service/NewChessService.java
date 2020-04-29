package wooteco.chess.boot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wooteco.chess.boot.entity.PieceEntity;
import wooteco.chess.boot.entity.RoomEntity;
import wooteco.chess.boot.entity.RoomInfoEntity;
import wooteco.chess.boot.repository.PieceRepository;
import wooteco.chess.boot.repository.RoomInfoRepository;
import wooteco.chess.boot.repository.RoomRepository;
import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.board.BoardFactory;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.Team;

@Service
public class NewChessService {

    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private RoomInfoRepository roomInfoRepository;
    @Autowired
    private PieceRepository pieceRepository;

    public Board newGame() {
        Board board = BoardFactory.create();
        Long roomNumber = generateRandomNumber();

        RoomEntity roomEntity = roomRepository.save(new RoomEntity(roomNumber));
        Long roomId = roomEntity.getId();

        writeBoard(roomId, board);
        roomInfoRepository.save(new RoomInfoEntity(roomId, Team.WHITE.toString(), false));

        return board;
    }

    private Long generateRandomNumber() {
        Long currentTime = System.currentTimeMillis();
        return (long) currentTime.hashCode();
    }

    private void writeBoard(final Long roomId, final Board board) {
        for (Position position : Position.positions) {
            Piece piece = board.findPieceOn(position);
            pieceRepository.save(new PieceEntity(roomId, position.toString(), piece.toString()));
        }
    }

    //임시
    public void deleteRoom(long roomId) {
        roomRepository.deleteById(roomId);
    }
}
