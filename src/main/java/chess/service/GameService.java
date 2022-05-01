package chess.service;

import chess.dao.BoardDao;
import chess.dao.MemberDao;
import chess.dao.PieceDao;
import chess.dao.PositionDao;
import chess.domain.game.ChessBoard;
import chess.domain.game.Game;
import chess.domain.game.Initializer;
import chess.domain.member.Member;
import chess.domain.pieces.Blank;
import chess.domain.pieces.Color;
import chess.domain.pieces.Piece;
import chess.domain.position.Position;
import chess.dto.BoardDto;
import chess.dto.RoomDto;
import chess.dto.RoomsDto;
import chess.dto.StatusDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GameService {

    private final BoardDao<ChessBoard> boardDao;
    private final PositionDao<Position> positionDao;
    private final PieceDao<Piece> pieceDao;
    private final MemberDao<Member> memberDao;

    public GameService(BoardDao<ChessBoard> boardDao, PositionDao<Position> positionDao, PieceDao<Piece> pieceDao, MemberDao<Member> memberDao) {
        this.boardDao = boardDao;
        this.positionDao = positionDao;
        this.pieceDao = pieceDao;
        this.memberDao = memberDao;
    }

    @Transactional
    public ChessBoard saveBoard(ChessBoard board, Initializer initializer) {
        final ChessBoard savedBoard = boardDao.save(board);
        final Map<Position, Piece> initialize = initializer.initialize();
        positionDao.saveAll(savedBoard.getId());
        pieceDao.saveAll(makePieces(savedBoard, initialize));
        memberDao.saveAll(board.getMembers(), savedBoard.getId());
        return savedBoard;
    }

    private List<Piece> makePieces(ChessBoard savedBoard, Map<Position, Piece> initialize) {
        List<Piece> pieces = new ArrayList<>();
        for (Position position : initialize.keySet()) {
            int lastPositionId = positionDao.findByColumnAndRowAndBoardId(position.getColumn(), position.getRow(), savedBoard.getId()).get().getId();
            Piece piece = initialize.get(position);
            pieces.add(new Piece(piece.getColor(), piece.getType(), lastPositionId));
        }
        return pieces;
    }

    @Transactional
    public void move(int roomId, Position sourceRawPosition, Position targetRawPosition) {
        final Optional<ChessBoard> wrappedBoard = boardDao.findById(roomId);
        if (wrappedBoard.isEmpty()) {
            throw new IllegalArgumentException("보드가 존재하지 않습니다.");
        }
        Game game = new Game(() -> positionDao.findAllPositionsAndPieces(roomId), wrappedBoard.get().getTurn());
        Piece sourcePiece = extractPiece(game.piece(sourceRawPosition));
        Piece targetPiece = extractPiece(game.piece(targetRawPosition));
        game.move(sourceRawPosition, targetRawPosition);

        updateMovement(sourcePiece, targetPiece);
        boardDao.updateTurn(game.getTurn(), roomId);
    }

    private void updateMovement(Piece sourcePiece, Piece targetPiece) {
        pieceDao.updatePiece(targetPiece, sourcePiece);
        pieceDao.updatePiece(sourcePiece, new Piece(Color.NONE, new Blank()));
    }

    private Piece extractPiece(Optional<Piece> wrappedPiece) {
        if (wrappedPiece.isEmpty()) {
            throw new IllegalArgumentException("기물이 존재하지 않습니다.");
        }
        return wrappedPiece.get();
    }

    public BoardDto getBoard(int roomId) {
        final Optional<ChessBoard> wrappedBoard = boardDao.findById(roomId);
        if (wrappedBoard.isEmpty()) {
            throw new IllegalArgumentException("보드가 존재하지 않습니다.");
        }
        final ChessBoard board = wrappedBoard.get();
        final Map<Position, Piece> allPositionsAndPieces = positionDao.findAllPositionsAndPieces(roomId);
        Map<String, Piece> pieces = mapPositionToString(allPositionsAndPieces);
        return BoardDto.of(pieces, board.getRoomTitle(), board.getMembers().get(0), board.getMembers().get(1));
    }

    private Map<String, Piece> mapPositionToString(Map<Position, Piece> allPositionsAndPieces) {
        return allPositionsAndPieces.keySet().stream().collect(Collectors.toMap(position -> position.getRow().value() + position.getColumn().name(), allPositionsAndPieces::get));
    }

    public boolean isEnd(int roomId) {
        Game game = new Game(() -> positionDao.findAllPositionsAndPieces(roomId), Color.NONE);
        return game.isEnd();
    }

    public StatusDto status(int roomId) {
        return new StatusDto(Arrays.stream(Color.values()).collect(Collectors.toMap(Enum::name, color -> calculateScore(roomId, color))));
    }

    public double calculateScore(int roomId, final Color color) {
        Game game = new Game(() -> positionDao.findAllPositionsAndPieces(roomId), Color.NONE);
        return game.calculateScore(color);
    }

    public boolean end(int roomId, String password) {
        return boardDao.deleteByIdAndPassword(roomId, password) == 1;
    }

    public RoomsDto getRooms() {
        List<RoomDto> boardsDto = new ArrayList<>();
        List<ChessBoard> boards = boardDao.findAll();
        for (ChessBoard board : boards) {
            boardsDto.add(new RoomDto(board.getId(), board.getRoomTitle(), board.getMembers().get(0), board.getMembers().get(1)));
        }
        return new RoomsDto(boardsDto);
    }
}
