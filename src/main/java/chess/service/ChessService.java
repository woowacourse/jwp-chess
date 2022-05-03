package chess.service;

import chess.entity.GameEntity;
import chess.entity.PieceEntity;
import chess.model.ChessGame;
import chess.model.GameResult;
import chess.model.Turn;
import chess.model.board.Board;
import chess.model.board.BoardFactory;
import chess.model.dao.GameDao;
import chess.model.dao.PieceDao;
import chess.model.dto.*;
import chess.model.piece.Piece;
import chess.model.piece.PieceFactory;
import chess.model.position.Position;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class ChessService {

    public static final String PIECE_NONE = "none-.";
    private final PieceDao pieceDao;
    private final GameDao gameDao;

    public ChessService(PieceDao pieceDao, GameDao gameDao) {
        this.pieceDao = pieceDao;
        this.gameDao = gameDao;
    }

    public Long start(RoomDto roomDto) {
        long gameId = initGame(roomDto);
        initBoard(gameId);

        return gameId;
    }

    public GameInfosDto getAllGames() {
        List<GameEntity> games = gameDao.findAll();
        return new GameInfosDto(games.stream()
                .map(GameInfoDto::from)
                .collect(Collectors.toList()));
    }

    public WebBoardDto move(MoveDto moveDto, Long id) {
        ChessGame chessGame = findChessGameById(id);
        Turn turn = Turn.from(gameDao.findTurnById(id));
        chessGame.move(Position.from(moveDto.getSource()), Position.from(moveDto.getTarget()), turn);

        updateGame(moveDto, id, turn);

        if (chessGame.isKingDead()) {
            gameDao.update(turn.finish(), id);
        }

        return WebBoardDto.from(chessGame.getBoard());
    }

    public WebBoardDto getBoardByGameId(long gameId) {
        return WebBoardDto.from(toBoard(pieceDao.findAllByGameId(gameId)));
    }

    public String getTurn(Long id) {
        return gameDao.findTurnById(id);
    }

    public boolean isKingDead(Long id) {
        ChessGame chessGame = findChessGameById(id);
        return chessGame.isKingDead();
    }

    public GameResult getResult(Long id) {
        ChessGame chessGame = findChessGameById(id);
        return chessGame.getWinningResult();
    }

    public void deleteByGameId(String confirmPassword, Long id) {
        String storedPassword = gameDao.findPwdById(id);
        Turn turn = Turn.from(gameDao.findTurnById(id));
        turn.validateEnd();

        if (confirmPassword.equals(storedPassword)) {
            pieceDao.deleteByGameId(id);
            gameDao.deleteById(id);
            return;
        }
        throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
    }

    private long initGame(RoomDto roomDto) {
        return gameDao.initGame(roomDto.getRoomName(), roomDto.getPassword());
    }

    private void initBoard(Long gameId) {
        pieceDao.init(BoardFactory.create(), gameId);
    }

    private void updateGame(MoveDto moveDto, Long id, Turn turn) {
        Piece sourcePiece = PieceFactory.create(pieceDao.findPieceNameByPositionAndGameId(moveDto.getSource(), id));
        pieceDao.updateByPositionAndGameId(moveDto.getTarget(), PieceDao.getPieceName(sourcePiece), id);
        pieceDao.updateByPositionAndGameId(moveDto.getSource(), PIECE_NONE, id);
        gameDao.update(turn.change().getThisTurn(), id);
    }

    private ChessGame findChessGameById(long gameId) {
        return new ChessGame(toBoard(pieceDao.findAllByGameId(gameId)));
    }

    private Board toBoard(List<PieceEntity> rawBoard) {
        return new Board(rawBoard.stream()
                .collect(Collectors.toMap(
                        piece -> Position.from(piece.getPosition()),
                        piece -> PieceFactory.create(piece.getName()))
                ));
    }
}
