package wooteco.chess.boot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.chess.boot.entity.PieceEntity;
import wooteco.chess.boot.entity.RoomEntity;
import wooteco.chess.boot.entity.RoomInfoEntity;
import wooteco.chess.boot.repository.PieceRepository;
import wooteco.chess.boot.repository.RoomInfoRepository;
import wooteco.chess.boot.repository.RoomRepository;
import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.board.BoardFactory;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.judge.Judge;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChessService {

    private static final int HASH_CONSTANT = 1000000;

    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private RoomInfoRepository roomInfoRepository;
    @Autowired
    private PieceRepository pieceRepository;

    @Transactional
    public Long newGame() {
        Long roomNumber = generateCurrentTimeHash();

        Long roomId = writeRoom(roomNumber);
        writeRoomInfo(roomId, Team.WHITE, false);
        writeBoard(roomId, BoardFactory.create());

        return roomNumber;
    }

    private Long generateCurrentTimeHash() {
        Long currentTime = System.currentTimeMillis();
        return (long) -currentTime.hashCode() % HASH_CONSTANT;
    }

    private Long writeRoom(final Long roomNumber) {
        RoomEntity roomEntity = roomRepository.save(new RoomEntity(roomNumber));
        return roomEntity.getId();
    }

    private void writeRoomInfo(final Long roomId, final Team turn, final boolean isOver) {
        roomInfoRepository.save(new RoomInfoEntity(roomId, turn.toString(), isOver));
    }

    private void writeBoard(final Long roomId, final Board board) {
        for (Position position : Position.positions) {
            Piece piece = board.findPieceOn(position);
            pieceRepository.save(new PieceEntity(roomId, position.toString(), piece.toString()));
        }
    }

    @Transactional
    public Board readBoard(Long roomNumber) {
        Long roomId = roomRepository.findIdByNumber(roomNumber);

        List<PieceEntity> pieces = pieceRepository.findAllByRoomId(roomId);
        Team currentTurn = Team.of(roomInfoRepository.findTurnByRoomId(roomId));

        return new Board(parsePieceEntities(pieces), currentTurn);
    }

    private Map<Position, Piece> parsePieceEntities(final List<PieceEntity> pieceEntities) {
        return pieceEntities.stream()
                .collect(Collectors.toMap(entity -> Position.of(entity.getPlace()), entity -> Piece.of(entity.getPiece())));
    }

    @Transactional
    public List<Position> findMovablePlaces(final Long roomNumber, final Position start) {
        Board board = readBoard(roomNumber);

        try {
            checkGameOver(board);
            return board.findMovablePositions(start);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    private void checkGameOver(Board board) {
        Judge judge = new Judge();
        if (judge.findWinner(board).isPresent()) {
            throw new IllegalArgumentException("게임이 종료되었습니다.");
        }
    }

    @Transactional
    public void move(final Long roomNumber, final Position start, final Position end) {
        Long roomId = roomRepository.findIdByNumber(roomNumber);
        Board board = readBoard(roomNumber);

        try {
            checkGameOver(board);
            board.move(start, end);

            pieceRepository.deleteAllByRoomId(roomId);
            writeBoard(roomId, board);

            Long roomInfoId = roomInfoRepository.findIdByRoomId(roomId);
            roomInfoRepository.save(new RoomInfoEntity(roomInfoId, roomId, board.getCurrentTurn().toString(), false));
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    @Transactional
    public double calculateScore(final Long roomNumber, final Team team) {
        Judge judge = new Judge();
        return judge.getScoreByTeam(readBoard(roomNumber), team);
    }
}
