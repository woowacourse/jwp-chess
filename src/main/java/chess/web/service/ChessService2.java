package chess.web.service;

import chess.domain.Board;
import chess.domain.ChessBoard;
import chess.domain.ChessGame;
import chess.domain.Color;
import chess.domain.generator.InitBoardGenerator;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.domain.state.StateType;
import chess.web.dao.GameDao;
import chess.web.dao.PieceDao2;
import chess.web.dto.ChessResultDto;
import chess.web.dto.ChessStatusDto;
import chess.web.dto.DeleteGameRequestDto;
import chess.web.dto.GameResponseDto;
import chess.web.dto.CreateGameRequestDto;
import chess.web.dto.MovePositionsDto;
import chess.web.dto.MoveResultDto;
import chess.web.dto.PieceDto;
import chess.web.dto.PiecesDto;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ChessService2 {

    private static final int BOARD_START_INDEX = 0;
    private static final int BOARD_END_INDEX = 7;

    private final GameDao gameDao;
    private final PieceDao2 pieceDao;

    public ChessService2(GameDao gameDao, PieceDao2 pieceDao) {
        this.gameDao = gameDao;
        this.pieceDao = pieceDao;
    }

    // 인덱스
    public int newGame(CreateGameRequestDto createGameRequestDto) {
        int gameId = gameDao.save(createGameRequestDto, StateType.WHITE_TURN);
        start(gameId);
        return gameId;
    }

    private void start(int gameId) {
        ChessBoard chessBoard = new ChessBoard(new InitBoardGenerator());
        initChessBoard(chessBoard.getBoard(), gameId);
    }

    private void initChessBoard(Board board, int gameId) {
        for (int rankIndex = BOARD_START_INDEX; rankIndex <= BOARD_END_INDEX; rankIndex++) {
            initOneRank(board, rankIndex, gameId);
        }
    }

    private void initOneRank(Board board, int rankIndex, int gameId) {
        for (int fileIndex = BOARD_START_INDEX; fileIndex <= BOARD_END_INDEX; fileIndex++) {
            Position position = new Position(fileIndex, rankIndex);
            Piece piece = board.findPiece(position);
            pieceDao.save(gameId, new PieceDto(piece, position));
        }
    }

    // 인덱스
    public List<GameResponseDto> getAllGame() {
        return gameDao.findAll();
    }


    // 인덱스
    public void deleteGame(DeleteGameRequestDto deleteGameRequestDto) {
        int gameId = deleteGameRequestDto.getGameId();
        String password = deleteGameRequestDto.getPassword();
        String realPassword = gameDao.findPasswordById(gameId);

        if (realPassword.equals(password) && !getChessGame(gameId).isRunning()) {
            gameDao.deleteGameById(gameId);
        }
    }

    private ChessGame getChessGame(int gameId) {
        PiecesDto piecesDto = new PiecesDto(getPieces(gameId));
        ChessBoard chessBoard = new ChessBoard(piecesDto.toBoard());
        return new ChessGame(getStateType(gameId).newState(chessBoard));
    }

    private List<PieceDto> getPieces(int gameId) {
        return pieceDao.findAllByGameId(gameId);
    }

    private StateType getStateType(int gameId) {
        return gameDao.findStateById(gameId);
    }

    // 체스방 참여, 체스판 보여주기
    public ChessStatusDto getChessStatus(int gameId) {
        return new ChessStatusDto(new PiecesDto(getPieces(gameId)), getScore(gameId, Color.BLACK),
                getScore(gameId, Color.WHITE));
    }

    private double getScore(int gameId, Color color) {
        return getChessGame(gameId).score(color);
    }

    // move
    public MoveResultDto getMoveResult(int gameId, MovePositionsDto movePositionsDto) {
        ChessGame chessGame = getChessGame(gameId);

        try {
            chessGame.move(movePositionsDto.getSource(), movePositionsDto.getTarget());
            Position sourcePosition = new Position(movePositionsDto.getSource());
            Position targetPosition = new Position(movePositionsDto.getTarget());
            move(gameId, chessGame, sourcePosition, targetPosition);
        } catch (Exception ex) {
            return new MoveResultDto(400, ex.getMessage(), chessGame.isFinished());
        }

        return new MoveResultDto(200, "", chessGame.isFinished());
    }

    private void move(int gameId, ChessGame chessGame, Position target, Position source) {
        gameDao.updateStateById(gameId, chessGame.getStateType());
        pieceDao.updateByGameId(gameId, new PieceDto(chessGame.board().findPiece(target), target));
        pieceDao.updateByGameId(gameId, new PieceDto(chessGame.board().findPiece(source), source));
    }

    // result
    public ChessResultDto getChessResult(int gameId) {
        ChessGame chessGame = getChessGame(gameId);
        endGame(chessGame);
        return new ChessResultDto(getScore(gameId, Color.BLACK), getScore(gameId, Color.WHITE), chessGame.result());
    }

    private void endGame(ChessGame chessGame) {
        if (!chessGame.isFinished()) {
            chessGame.end();
        }
    }
}
