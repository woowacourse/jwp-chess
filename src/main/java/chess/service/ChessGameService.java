package chess.service;

import chess.dao.ChessGameDAO;
import chess.dao.PieceDAO;
import chess.domain.board.Board;
import chess.domain.game.*;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceFactory;
import chess.domain.piece.Position;
import chess.dto.ChessGameDto;
import chess.dto.ChessRoomDto;
import chess.dto.ScoreDto;
import chess.exception.NoSuchPermittedChessPieceException;
import chess.exception.NotFoundPlayingChessGameException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChessGameService {

    private final ChessGameDAO chessGameDAO;
    private final PieceDAO pieceDAO;

    public ChessGameService(ChessGameDAO chessGameDAO, PieceDAO pieceDAO) {
        this.chessGameDAO = chessGameDAO;
        this.pieceDAO = pieceDAO;
    }

    @Transactional
    public ChessRoomDto createNewChessRoom() {
        Long chessGameId = chessGameDAO.create();
        List<Piece> pieces = PieceFactory.createPieces();
        pieceDAO.saveAll(chessGameId, pieces);
        ChessGame chessGame = new ChessGame(new Board(pieces));
        chessGame.changeState(new BlackTurn(chessGame));
        return new ChessRoomDto(chessGameId);
    }

    @Transactional
    public ChessGameDto moveChessPiece(Position source, Position target, long roomId) {
        ChessGameEntity chessGameEntity = findGameByRoomId(roomId);
        Long chessGameId = chessGameEntity.getId();
        ChessGame chessGame = findChessGameByChessGameId(chessGameEntity);
        Piece sourcePiece = pieceDAO.findOneByPosition(chessGameId, source.getRow(), source.getColumn())
                .orElseThrow(NoSuchPermittedChessPieceException::new);
        chessGame.move(sourcePiece.getPosition(), target);
        pieceDAO.findOneByPosition(chessGameId, target.getRow(), target.getColumn())
                .ifPresent(piece -> pieceDAO.delete(chessGameId, target.getRow(), target.getColumn()));

        sourcePiece.setPosition(target);
        pieceDAO.update(sourcePiece);
        chessGameDAO.updateState(chessGameId, chessGame.getState().getValue());

        return new ChessGameDto(chessGame);
    }

    @Transactional
    public ChessGameDto endGame(long roomId) {
        ChessGameEntity chessGameEntity = findGameByRoomId(roomId);
        ChessGame chessGame = findChessGameByChessGameId(chessGameEntity);
        chessGame.end();
        chessGameDAO.updateState(chessGameEntity.getId(), chessGame.getState().getValue());

        return new ChessGameDto(chessGame);
    }

    @Transactional(readOnly = true)
    public ScoreDto calculateScores(long roomId) {
        ChessGameEntity chessGameEntity = findGameByRoomId(roomId);
        ChessGame chessGame = findChessGameByChessGameId(chessGameEntity);

        return new ScoreDto(chessGame);
    }

    @Transactional(readOnly = true)
    public List<ChessRoomDto> findChessRooms() {
        List<ChessGameEntity> chessGameEntities = findAllStateIsBlackAndWhiteTurnGame();
        return  chessGameEntities.stream()
                .map(ChessGameEntity::getId)
                .map(ChessRoomDto::new)
                .collect(Collectors.toList());
    }

    private List<ChessGameEntity> findAllStateIsBlackAndWhiteTurnGame() {
        return chessGameDAO.findAllByStateIsBlackTurnOrWhiteTurn();
    }

    @Transactional
    public ChessGameDto findChessGame(long roomId) {
        ChessGameEntity chessGameEntity = findGameByRoomId(roomId);
        ChessGame chessGame = findChessGameByChessGameId(chessGameEntity);
        return new ChessGameDto(chessGame);
    }

    private ChessGameEntity findGameByRoomId(long roomId) {
        return chessGameDAO.findGameByRoomId(roomId)
                .orElseThrow(NotFoundPlayingChessGameException::new);
    }

    private ChessGame findChessGameByChessGameId(final ChessGameEntity chessGameEntity) {
        List<Piece> pieces = pieceDAO.findAllPiecesByChessGameId(chessGameEntity.getId());
        Board board = new Board(pieces);
        ChessGame chessGame = new ChessGame(board);
        State currentState = StateFactory.valueOf(chessGameEntity.getState(), chessGame);
        chessGame.changeState(currentState);

        return chessGame;
    }

}
