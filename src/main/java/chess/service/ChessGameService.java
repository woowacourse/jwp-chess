package chess.service;

import static java.util.stream.Collectors.toMap;
import chess.controller.Movement;
import chess.dao.ChessGameDao;
import chess.dao.PieceDao;
import chess.domain.ChessBoard;
import chess.domain.ChessBoardInitializer;
import chess.domain.ChessGame;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceStorage;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ChessGameService {

    private final PieceDao pieceDao;
    private final ChessGameDao chessGameDao;

    public ChessGameService(PieceDao pieceDao, ChessGameDao chessGameDao) {
        this.pieceDao = pieceDao;
        this.chessGameDao = chessGameDao;
    }

    public ChessGameDto findChessGame(int chessGameId) {
        final ChessGameDto chessGameDto = chessGameDao.findById(chessGameId);
        if (chessGameDto == null) {
            throw new ChessGameException(chessGameId, "해당하는 체스 게임이 존재하지 않습니다.");
        }
        return chessGameDto;
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

    @Transactional
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

    @Transactional
    public void create(ChessGameRequest chessGameRequest) {
        Room room = new Room(chessGameRequest.getName(), chessGameRequest.getPassword());
        int chessGameId = chessGameDao.saveChessGame(ChessGame.start(room));
        Map<Position, Piece> initBoard = ChessBoardInitializer.getInitBoard();
        initBoard.forEach((key, value) -> pieceDao.savePiece(chessGameId, key, value));
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
            .collect(toMap(
                pieceDto -> createPosition(pieceDto.getPosition()),
                pieceDto -> PieceStorage.valueOf(pieceDto.getType(), pieceDto.getColor()))
            );
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
        pieceDao.savePiece(chessGameDto.getId(), movement.getTo(), board.get(movement.getTo()));
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


    private Position createPosition(String position) {
        File file = File.valueOf(position.substring(0, 1));
        Rank rank = Rank.find(position.substring(1, 2));
        return new Position(file, rank);
    }
}
