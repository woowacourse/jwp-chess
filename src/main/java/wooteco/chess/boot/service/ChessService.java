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
import wooteco.chess.domain.judge.Judge;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChessService {

    //임시로 방 하나만 쓰기 위한 코드(4단계에서 수정)
    private static final long STATIC_ROOM_ID = 6;
    private static final long STATIC_ROOM_NUMBER = 1;
    private static final long STATIC_ROOM_INFO_ID = 7;

    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private RoomInfoRepository roomInfoRepository;
    @Autowired
    private PieceRepository pieceRepository;

    public Board newGame() {
        Board board = BoardFactory.create();

        RoomEntity roomEntity = roomRepository.save(new RoomEntity(STATIC_ROOM_ID, STATIC_ROOM_NUMBER));
        Long roomId = roomEntity.getId();

        writeBoard(roomId, board);

        //임시로 방 하나만 쓰기 위한 코드(4단계에서 수정)
        roomInfoRepository.save(new RoomInfoEntity(STATIC_ROOM_INFO_ID, roomId, Team.WHITE.toString(), false));

        return board;
    }

    //4단계에서 사용할 roomNumber generator
    private Long generateRandomNumber() {
        Long currentTime = System.currentTimeMillis();
        return (long) currentTime.hashCode();
    }

    private void writeBoard(final Long roomId, final Board board) {
        //임시로 방 하나만 쓰기 위한 코드(4단계에서 수정)
        pieceRepository.deleteAll();

        for (Position position : Position.positions) {
            Piece piece = board.findPieceOn(position);
            pieceRepository.save(new PieceEntity(roomId, position.toString(), piece.toString()));
        }
    }

    public Board readBoard() {
        List<PieceEntity> pieces = pieceRepository.findAll();
        //임시로 방 하나만 쓰기 위한 코드(4단계에서 수정)
        RoomInfoEntity roomInfoEntity = roomInfoRepository.findByRoomId(STATIC_ROOM_ID);

        Team currentTurn = Team.of(roomInfoEntity.getTurn());
        return new Board(parsePieceEntities(pieces), currentTurn);
    }

    private Map<Position, Piece> parsePieceEntities(final List<PieceEntity> pieceEntities) {
        return pieceEntities.stream()
                .collect(Collectors.toMap(entity -> Position.of(entity.getPlace()), entity -> Piece.of(entity.getPiece())));
    }

    public List<Position> findMovablePlaces(final Position start) {
        Board board = readBoard();

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

    public void move(final Position start, final Position end) {
        Board board = readBoard();

        try {
            checkGameOver(board);
            board.move(start, end);

            writeBoard(STATIC_ROOM_ID, board);
            roomInfoRepository.save(new RoomInfoEntity(STATIC_ROOM_INFO_ID, STATIC_ROOM_ID, board.getCurrentTurn().toString(), false));
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    public double calculateScore(final Team team) {
        Judge judge = new Judge();
        return judge.getScoreByTeam(readBoard(), team);
    }
}
