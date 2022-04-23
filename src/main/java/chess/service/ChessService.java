package chess.service;

import chess.model.status.End;
import chess.model.status.Status;
import org.springframework.stereotype.Service;
import chess.repository.*;
import chess.dto.BoardDto;
import chess.dto.RoomDto;
import chess.dto.RoomsDto;
import chess.model.board.Board;
import chess.model.member.Member;
import chess.model.piece.Empty;
import chess.model.piece.Initializer;
import chess.model.piece.Piece;
import chess.model.piece.Team;
import chess.model.room.Room;
import chess.model.square.File;
import chess.model.square.Square;
import chess.model.status.Running;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChessService {

    private static final int PROPER_KING_COUNT = 2;

    private final BoardRepository<Board> chessBoardRepository;
    private final SquareRepository<Square> chessSquareRepository;
    private final PieceRepository<Piece> chessPieceRepository;
    private final RoomRepository<Room> chessRoomRepository;
    private final MemberRepository<Member> chessMemberRepository;

    public ChessService(
            BoardRepository<Board> chessBoardRepository,
            SquareRepository<Square> chessSquareRepository,
            PieceRepository<Piece> chessPieceRepository,
            RoomRepository<Room> chessRoomRepository,
            MemberRepository<Member> chessMemberRepository) {
        this.chessBoardRepository = chessBoardRepository;
        this.chessSquareRepository = chessSquareRepository;
        this.chessPieceRepository = chessPieceRepository;
        this.chessRoomRepository = chessRoomRepository;
        this.chessMemberRepository = chessMemberRepository;
    }

    public Room init(String roomTitle, String member1, String member2) {
        Board board = chessBoardRepository.init(new Board(new Running(), Team.WHITE), Initializer.initialize());
        Room room = chessRoomRepository.save(new Room(roomTitle, board.getId()));
        chessMemberRepository.saveAll(List.of(new Member(member1), new Member(member2)), room.getId());
        return room;
    }

    public RoomsDto getRooms() {
        List<RoomDto> roomsDto = new ArrayList<>();
        List<Room> rooms = chessRoomRepository.findAllByBoardStatus(new Running());
        for (Room room : rooms) {
            List<Member> membersByRoom = chessMemberRepository.getAllByRoomId(room.getId());
            roomsDto.add(
                    new RoomDto(room.getId(), room.getTitle(), membersByRoom));
        }
        return new RoomsDto(roomsDto);
    }

    public BoardDto getBoard(int roomId) {
        final Room room = chessRoomRepository.getById(roomId);
        final Board board = chessBoardRepository.getById(room.getBoardId());
        final Map<Square, Piece> allPositionsAndPieces = chessSquareRepository.findAllSquaresAndPieces(board.getId());
        List<Member> members = chessMemberRepository.getAllByRoomId(roomId);
        return BoardDto.of(
                allPositionsAndPieces,
                room.getTitle(),
                members.get(0),
                members.get(1));
    }

    public void move(String source, String target, int roomId) {
        Room room = chessRoomRepository.getById(roomId);
        Square sourceSquare = chessSquareRepository.getBySquareAndBoardId(Square.fromString(source), room.getBoardId());
        Square targetSquare = chessSquareRepository.getBySquareAndBoardId(Square.fromString(target), room.getBoardId());
        Piece piece = chessPieceRepository.findBySquareId(sourceSquare.getId());
        Board board = chessBoardRepository.getById(room.getBoardId());
        checkTurn(board, piece);

        checkMovable(sourceSquare, targetSquare, piece, room.getBoardId());
        chessPieceRepository.deletePieceBySquareId(targetSquare.getId());
        chessPieceRepository.updatePieceSquareId(sourceSquare.getId(), targetSquare.getId());
        chessPieceRepository.save(new Empty(), sourceSquare.getId());
        
        chessBoardRepository.updateTeamById(room.getBoardId(), board.getTeam().oppositeTeam());
        checkKingDead(room.getBoardId());
    }

    private void checkTurn(Board board, Piece piece) {
        Team team = board.getTeam();
        if (!team.isProperTurn(piece.team())) {
            throw new IllegalArgumentException(String.format("현재 %s팀의 차례가 아닙니다.", piece.team().name()));
        }
    }

    private void checkKingDead(int boardId) {
        long kingCount = chessPieceRepository.getAllPiecesByBoardId(boardId).stream()
                .filter(Piece::isKing)
                .count();
        if (kingCount != PROPER_KING_COUNT) {
            chessBoardRepository.updateStatus(boardId, new End());
        }
    }

    private void checkMovable(Square sourceSquare, Square targetSquare, Piece piece, int boardId) {
        Piece targetPiece = chessPieceRepository.findBySquareId(targetSquare.getId());
        if (!piece.movable(targetPiece, sourceSquare, targetSquare)) {
            throw new IllegalArgumentException("해당 위치로 움직일 수 없습니다.");
        }
        List<Square> route = piece.getRoute(sourceSquare, targetSquare);

        if (piece.isPawn() && !route.isEmpty() && !piece.isNotAlly(targetPiece)) {
            throw new IllegalArgumentException("같은 팀이 있는 곳으로 갈 수 없습니다.");
        }

        checkMoveWithoutObstacle(route, boardId, piece, targetPiece);
    }

    private void checkMoveWithoutObstacle(List<Square> route, int boardId, Piece sourcePiece, Piece targetPiece) {
        List<Square> realSquares = chessSquareRepository.getPaths(route, boardId);
        for (Square square : realSquares) {
            Piece piece = chessPieceRepository.findBySquareId(square.getId());
            if (piece.equals(targetPiece) && sourcePiece.isNotAlly(targetPiece)) {
                return;
            }
            if (piece.isNotEmpty()) {
                throw new IllegalArgumentException("경로 중 기물이 있습니다.");
            }
        }
    }

    public boolean isEnd(int roomId) {
        Room room = chessRoomRepository.getById(roomId);
        Status status = chessBoardRepository.getStatusById(room.getBoardId());
        return status.isEnd();
    }

    public Map<String, Double> status(int roomId) {
         return Team.getPlayerTeams().stream()
                .collect(Collectors.toMap(Enum::name, team -> calculateScore(roomId, team)));
    }

    public double calculateScore(int roomId, final Team team) {
        Room room = chessRoomRepository.getById(roomId);
        return calculateDefaultScore(room.getBoardId(), team) - 0.5 * countPawnsOnSameColumns(room.getBoardId(), team);
    }

    private double calculateDefaultScore(int boardId, Team team) {
        return existPieces(boardId)
                .stream()
                .filter(piece -> piece.isSameTeam(team))
                .mapToDouble(Piece::getPoint)
                .sum();
    }

    public List<Piece> existPieces(int boardId) {
        return chessPieceRepository.getAllPiecesByBoardId(boardId);
    }

    private int countPawnsOnSameColumns(int boardId, final Team team) {
        return Arrays.stream(File.values())
                .mapToInt(file -> chessPieceRepository.countPawnsOnSameColumn(boardId, file, team))
                .filter(count -> count > 1)
                .sum();
    }

    public void end(int roomId) {
        Room room = chessRoomRepository.getById(roomId);
        chessBoardRepository.updateStatus(room.getBoardId(), new End());
    }
}
