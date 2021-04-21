package chess.service;

import chess.dao.ChessGameDAO;
import chess.dao.PieceDAO;
import chess.domain.board.Board;
import chess.domain.game.*;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceFactory;
import chess.domain.piece.Position;
import chess.dto.*;
import chess.exception.NoSuchPermittedChessPieceException;
import chess.exception.NotFoundChessGameException;
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
    public ChessGameInfoResponseDto createNewChessGame(String title) {
        Long chessGameId = chessGameDAO.save(title);
        List<Piece> pieces = PieceFactory.createPieces();
        pieceDAO.saveAll(chessGameId, pieces);
        ChessGame chessGame = new ChessGame(new Board(pieces));
        chessGame.changeState(new Ready(chessGame));
        return new ChessGameInfoResponseDto(chessGameId, chessGame, title);
    }

    @Transactional
    public ChessGameResponseDto moveChessPiece(Long chessGameId, final Position source, final Position target) {
        ChessGameEntity chessGameEntity = chessGameDAO.findById(chessGameId)
                .orElseThrow(NotFoundPlayingChessGameException::new);
        ChessGame chessGame = findChessGameByChessGameId(chessGameEntity);
        Piece sourcePiece = pieceDAO.findOneByPosition(chessGameId, source.getRow(), source.getColumn())
                .orElseThrow(NoSuchPermittedChessPieceException::new);
        chessGame.move(sourcePiece.getPosition(), target);
        pieceDAO.findOneByPosition(chessGameId, target.getRow(), target.getColumn())
                .ifPresent(piece -> pieceDAO.delete(chessGameId, target.getRow(), target.getColumn()));

        sourcePiece.setPosition(target);
        pieceDAO.update(sourcePiece);
        chessGameDAO.updateState(chessGameId, chessGame.getState().getValue());

        return new ChessGameResponseDto(chessGame);
    }

    @Transactional(readOnly = true)
    public ChessGameStatusDto findLatestChessGameStatus() {
        return chessGameDAO.findIsExistPlayingChessGameStatus();
    }

    @Transactional
    public ChessGameResponseDto endGame(Long chessGameId) {
        ChessGameEntity chessGameEntity = chessGameDAO.findById(chessGameId)
                .orElseThrow(NotFoundChessGameException::new);
        ChessGame chessGame = findChessGameByChessGameId(chessGameEntity);
        chessGame.end();
        chessGameDAO.updateState(chessGameId, chessGame.getState().getValue());

        return new ChessGameResponseDto(chessGame);
    }

    @Transactional(readOnly = true)
    public ScoreDto calculateScores(Long chessGameId) {
        ChessGameEntity chessGameEntity = chessGameDAO.findById(chessGameId)
                .orElseThrow(NotFoundChessGameException::new);
        ChessGame chessGame = findChessGameByChessGameId(chessGameEntity);

        return new ScoreDto(chessGame);
    }

    @Transactional(readOnly = true)
    public ChessGameResponseDto findChessGameById(Long chessGameId) {
        ChessGameEntity chessGameEntity = chessGameDAO.findById(chessGameId)
                .orElseThrow(NotFoundChessGameException::new);
        ChessGame chessGame = findChessGameByChessGameId(chessGameEntity);
        return new ChessGameResponseDto(chessGame);
    }

    @Transactional(readOnly = true)
    public ChessGameInfoResponseDto findChessGameInfoById(Long chessGameId) { //todo: 테스트코드짜기
        ChessGameEntity chessGameEntity = chessGameDAO.findById(chessGameId)
                .orElseThrow(NotFoundChessGameException::new);
        ChessGame chessGame = findChessGameByChessGameId(chessGameEntity);
        return new ChessGameInfoResponseDto(chessGameId, chessGame, chessGameEntity.getTitle());
    }

    @Transactional(readOnly = true)
    public List<PlayingChessgameEntityDto> findAllPlayingGames() { //todo: 테스트 코드 짜기
        List<ChessGameEntity> chessGameEntities = chessGameDAO.findAllNotEndGameOrderByIdDesc();
        return chessGameEntities.stream()
                .map(PlayingChessgameEntityDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public ChessGameResponseDto startGame(Long chessGameId) {
        ChessGameEntity chessGameEntity = chessGameDAO.findById(chessGameId)
                .orElseThrow(NotFoundPlayingChessGameException::new);
        ChessGame chessGame = findChessGameByChessGameId(chessGameEntity);
        chessGame.start();
        chessGameDAO.updateState(chessGameId, chessGame.getState().getValue());

        return new ChessGameResponseDto(chessGame);
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
