package chess.service;

import chess.dto.BoardDto;
import chess.dto.GameDeleteResponseDto;
import chess.dto.RoomDto;
import chess.dto.RoomsDto;
import chess.model.board.Board;
import chess.model.member.Member;
import chess.model.piece.*;
import chess.model.room.Room;
import chess.model.square.File;
import chess.model.square.Square;
import chess.model.status.End;
import chess.model.status.Running;
import chess.model.status.Status;
import chess.repository.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChessService {

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

    public Room init(String roomTitle, String password, String member1, String member2) {
        Board board = chessBoardRepository.save(new Board(new Running(), Team.WHITE));
        Map<Square, Piece> startingPieces = Initializer.initialize();
        chessSquareRepository.saveAllSquares(board.getId(), startingPieces.keySet());
        chessPieceRepository.saveAllPieces(mapToPieces(board.getId(), startingPieces));
        Room room = chessRoomRepository.save(new Room(roomTitle, password, board.getId()));
        chessMemberRepository.saveAll(List.of(new Member(member1), new Member(member2)), room.getId());
        return room;
    }

    private List<Piece> mapToPieces(int boardId, Map<Square, Piece> startingPieces) {
        return startingPieces.keySet().stream()
                .map(square -> startingPieces.get(square).setSquareId(getSquare(square, boardId).getId()))
                .collect(Collectors.toList());
    }

    private Square getSquare(Square square, int board) {
        return chessSquareRepository.getBySquareAndBoardId(square, board);
    }

    public RoomsDto getRooms() {
        List<RoomDto> roomsDto = new ArrayList<>();
        List<Room> rooms = chessRoomRepository.findAll();
        for (Room room : rooms) {
            List<Member> membersByRoom = chessMemberRepository.findMembersByRoomId(room.getId());
            boolean isEnd = chessBoardRepository.getStatusById(room.getBoardId()).isEnd();
            roomsDto.add(
                    new RoomDto(room.getId(), room.getTitle(), room.getPassword(), membersByRoom, isEnd));
        }
        return new RoomsDto(roomsDto);
    }

    public BoardDto getBoard(int roomId) {
        final Room room = chessRoomRepository.getById(roomId);
        final Map<Square, Piece> allPositionsAndPieces = chessSquareRepository.findAllSquaresAndPieces(
                room.getBoardId());
        List<Member> members = chessMemberRepository.findMembersByRoomId(roomId);
        return BoardDto.of(
                allPositionsAndPieces,
                room.getTitle(),
                members.get(0),
                members.get(1));
    }

    public void move(String source, String target, int roomId) {
        Room room = chessRoomRepository.getById(roomId);
        int boardId = room.getBoardId();
        Board board = new Board(chessSquareRepository.findAllSquaresAndPieces(boardId));
        Team team = chessBoardRepository.getTeamById(boardId);
        board.checkTurn(team, source);
        board.move(source, target);

        updatePieces(source, target, boardId);
        chessBoardRepository.updateTeamById(boardId, team.oppositeTeam());
        checkKingDead(boardId, board);
    }

    private void checkKingDead(int boardId, Board board) {
        if (board.isKingDead()) {
            chessBoardRepository.updateStatus(boardId, new End());
        }
    }

    private void updatePieces(String source, String target, int boardId) {
        Square sourceSquare = getSquare(Square.fromString(source), boardId);
        Square targetSquare = getSquare(Square.fromString(target), boardId);
        chessPieceRepository.deletePieceBySquareId(targetSquare.getId());
        chessPieceRepository.updatePieceSquareId(sourceSquare.getId(), targetSquare.getId());
        chessPieceRepository.save(new Empty(), sourceSquare.getId());
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
                .mapToInt(file -> chessPieceRepository.countPawnsOnSameFile(boardId, file, team))
                .filter(count -> count > 1)
                .sum();
    }

    public void end(int roomId) {
        Room room = chessRoomRepository.getById(roomId);
        chessBoardRepository.updateStatus(room.getBoardId(), new End());
    }

    public GameDeleteResponseDto deleteRoom(int id, String password) {
        Room room = chessRoomRepository.getById(id);
        if (room.getPassword().equals(password)) {
            chessRoomRepository.deleteById(id);
            return new GameDeleteResponseDto(true, "삭제되었습니다.");
        }
        return new GameDeleteResponseDto(false, "삭제할 수 없습니다.");
    }
}
