package chess.service;

import static java.util.stream.Collectors.toMap;
import chess.controller.Movement;
import chess.dao.ChessGameDao;
import chess.dao.PieceDao;
import chess.domain.ChessBoard;
import chess.domain.Score;
import chess.domain.piece.Bishop;
import chess.domain.piece.Color;
import chess.domain.piece.King;
import chess.domain.piece.Knight;
import chess.domain.piece.Pawn;
import chess.domain.piece.Piece;
import chess.domain.piece.Queen;
import chess.domain.piece.Rook;
import chess.domain.position.File;
import chess.domain.position.Position;
import chess.domain.position.Rank;
import chess.domain.vo.Room;
import chess.dto.ChessGameDto;
import chess.dto.ChessGameRequest;
import chess.dto.GameStatus;
import chess.dto.MoveRequest;
import chess.dto.PieceDto;
import chess.exception.ChessGameException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;

@Service
public class ChessGameService {

    private final PieceDao pieceDao;
    private final ChessGameDao chessGameDao;

    public ChessGameService(PieceDao pieceDao, ChessGameDao chessGameDao) {
        this.pieceDao = pieceDao;
        this.chessGameDao = chessGameDao;
    }

    public ChessGameDto findChessGame(int chessGameId) {
        return chessGameDao.findById(chessGameId);
    }

    public List<PieceDto> findPieces(int chessGameId) {
        return pieceDao.findPieces(chessGameId);
    }

    public boolean isFinished(int chessGameId) {
        ChessGameDto chessGameDto = chessGameDao.findById(chessGameId);
        return chessGameDto.getStatus().isFinished();
    }

    public String findWinner(int chessGameId) {
        ChessGameDto chessGameDto = chessGameDao.findById(chessGameId);
        return chessGameDto.getWinner();
    }

    public int move(MoveRequest moveRequest) {
        int chessGameId = moveRequest.getId();
        Movement movement = new Movement(moveRequest.getFrom(), moveRequest.getTo());
        List<PieceDto> pieces = pieceDao.findPieces(chessGameId);
        ChessGameDto chessGameDto = chessGameDao.findById(chessGameId);
        if (chessGameDto.getStatus().isFinished()) {
            throw new ChessGameException(chessGameDto.getId(), "게임이 종료되었습니다.");
        }
        ChessBoard chessBoard = createChessBoard(pieces, chessGameDto.getCurrentColor());
        movePiece(chessGameId, movement, chessBoard);
        return updateChessBoard(chessBoard, movement, chessGameDto);
    }

    public void create(ChessGameRequest chessGameRequest) {
        Room room = new Room(chessGameRequest.getName(), chessGameRequest.getPassword());
        int chessGameId = chessGameDao.saveChessGame(room, GameStatus.READY, Color.WHITE, new Score(), new Score());
        pieceDao.deleteById(chessGameId);
        pieceDao.savePieces(chessGameId, createPieces());
    }

    public List<ChessGameDto> findAll() {
        return chessGameDao.findAll();
    }

    public void deleteRoom(int chessGameId, String password) {
        chessGameDao.deleteByIdAndPassword(chessGameId, password);
    }

    private ChessBoard createChessBoard(List<PieceDto> pieces, Color color) {
        return new ChessBoard(createBoard(pieces), color);
    }

    private Map<Position, Piece> createBoard(List<PieceDto> pieces) {
        return pieces.stream()
                .collect(toMap(PieceDto::getPosition, PieceDto::createPiece));
    }

    private void movePiece(int chessGameId, Movement movement, ChessBoard chessBoard) {
        try {
            chessBoard.move(movement.getFrom(), movement.getTo());
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new ChessGameException(chessGameId, e.getMessage());
        }
    }

    private int updateChessBoard(ChessBoard chessBoard, Movement movement, ChessGameDto chessGameDto) {
        updatePieces(chessGameDto, chessBoard, movement);
        return updateChessGame(chessBoard, chessGameDto);
    }

    private void updatePieces(ChessGameDto chessGameDto, ChessBoard chessBoard, Movement movement) {
        Map<Position, Piece> board = chessBoard.getBoard();
        pieceDao.deletePieceByPosition(chessGameDto.getId(), movement.getFrom());
        pieceDao.deletePieceByPosition(chessGameDto.getId(), movement.getTo());
        pieceDao.savePiece(chessGameDto.getId(), new PieceDto(movement.getTo(), board.get(movement.getTo())));
    }

    private int updateChessGame(ChessBoard chessBoard, ChessGameDto chessGameDto) {
        ChessGameDto newChessGameDto = createNewChessGameDto(chessBoard, chessGameDto);
        chessGameDao.updateChessGame(newChessGameDto);
        return newChessGameDto.getId();
    }

    private ChessGameDto createNewChessGameDto(ChessBoard chessBoard, ChessGameDto chessGameDto) {
        GameStatus status = chessGameDto.getStatus();
        String winner = chessGameDto.getWinner();

        if (chessBoard.isFinished()) {
            status = GameStatus.FINISHED;
            winner = chessBoard.getWinner().name();
        }

        return new ChessGameDto(chessGameDto.getId(), chessGameDto.getName(), status, chessBoard.getScore(Color.BLACK),
                chessBoard.getScore(Color.WHITE), chessBoard.getCurrentColor(), winner);
    }

    private List<PieceDto> createPieces() {
        return Stream.concat(createWhitePieces().stream(), createBlackPieces().stream())
                .collect(Collectors.toList());
    }

    private static List<PieceDto> createWhitePieces() {
        return List.of(
                new PieceDto(new Position(File.A, Rank.ONE), new Rook(Color.WHITE)),
                new PieceDto(new Position(File.B, Rank.ONE), new Knight(Color.WHITE)),
                new PieceDto(new Position(File.C, Rank.ONE), new Bishop(Color.WHITE)),
                new PieceDto(new Position(File.D, Rank.ONE), new Queen(Color.WHITE)),
                new PieceDto(new Position(File.E, Rank.ONE), new King(Color.WHITE)),
                new PieceDto(new Position(File.F, Rank.ONE), new Bishop(Color.WHITE)),
                new PieceDto(new Position(File.G, Rank.ONE), new Knight(Color.WHITE)),
                new PieceDto(new Position(File.H, Rank.ONE), new Rook(Color.WHITE)),
                new PieceDto(new Position(File.A, Rank.TWO), new Pawn(Color.WHITE)),
                new PieceDto(new Position(File.B, Rank.TWO), new Pawn(Color.WHITE)),
                new PieceDto(new Position(File.C, Rank.TWO), new Pawn(Color.WHITE)),
                new PieceDto(new Position(File.D, Rank.TWO), new Pawn(Color.WHITE)),
                new PieceDto(new Position(File.E, Rank.TWO), new Pawn(Color.WHITE)),
                new PieceDto(new Position(File.F, Rank.TWO), new Pawn(Color.WHITE)),
                new PieceDto(new Position(File.G, Rank.TWO), new Pawn(Color.WHITE)),
                new PieceDto(new Position(File.H, Rank.TWO), new Pawn(Color.WHITE)));
    }

    private static List<PieceDto> createBlackPieces() {
        return List.of(
                new PieceDto(new Position(File.A, Rank.EIGHT), new Rook(Color.BLACK)),
                new PieceDto(new Position(File.B, Rank.EIGHT), new Knight(Color.BLACK)),
                new PieceDto(new Position(File.C, Rank.EIGHT), new Bishop(Color.BLACK)),
                new PieceDto(new Position(File.D, Rank.EIGHT), new Queen(Color.BLACK)),
                new PieceDto(new Position(File.E, Rank.EIGHT), new King(Color.BLACK)),
                new PieceDto(new Position(File.F, Rank.EIGHT), new Bishop(Color.BLACK)),
                new PieceDto(new Position(File.G, Rank.EIGHT), new Knight(Color.BLACK)),
                new PieceDto(new Position(File.H, Rank.EIGHT), new Rook(Color.BLACK)),
                new PieceDto(new Position(File.A, Rank.SEVEN), new Pawn(Color.BLACK)),
                new PieceDto(new Position(File.B, Rank.SEVEN), new Pawn(Color.BLACK)),
                new PieceDto(new Position(File.C, Rank.SEVEN), new Pawn(Color.BLACK)),
                new PieceDto(new Position(File.D, Rank.SEVEN), new Pawn(Color.BLACK)),
                new PieceDto(new Position(File.E, Rank.SEVEN), new Pawn(Color.BLACK)),
                new PieceDto(new Position(File.F, Rank.SEVEN), new Pawn(Color.BLACK)),
                new PieceDto(new Position(File.G, Rank.SEVEN), new Pawn(Color.BLACK)),
                new PieceDto(new Position(File.H, Rank.SEVEN), new Pawn(Color.BLACK)));
    }
}
