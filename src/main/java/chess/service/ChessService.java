package chess.service;

import chess.dao.GameDao;
import chess.dao.PieceDao;
import chess.domain.ChessGame;
import chess.domain.Score;
import chess.domain.command.MoveCommand;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceColor;
import chess.domain.piece.PieceFactory;
import chess.domain.position.Position;
import chess.service.dto.ChessResponseDto;
import chess.service.dto.GameDto;
import chess.service.dto.GameStatusDto;
import chess.service.dto.PieceDto;
import chess.service.dto.ScoresDto;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChessService {

    private final PieceDao pieceDao;
    private final GameDao gameDao;

    @Autowired
    public ChessService(PieceDao pieceDao, GameDao gameDao) {
        this.pieceDao = pieceDao;
        this.gameDao = gameDao;
    }

    public ChessResponseDto initializeGame() {
        int gameId = getGameId();
        try {
            pieceDao.saveAll(gameId, getInitPieceDtos());
            gameDao.save(gameId, GameDto.of(PieceColor.WHITE, true));
        } catch (Exception e) {
            throw new IllegalArgumentException("게임을 초기화할 수 없습니다.");
        }
        return getChess(gameId);
    }

    private int getGameId() {
        return gameDao.findLastGameId() + 1;
    }

    public ChessResponseDto getChess(final int id) {
        try {
            List<PieceDto> pieceDtos = pieceDao.findAll(id);
            GameDto gameDto = gameDao.find(id);
            return new ChessResponseDto(pieceDtos, gameDto.getTurn(), gameDto.getStatus());
        } catch (Exception e) {
            throw new IllegalArgumentException("체스 정보를 읽어올 수 없습니다.");
        }
    }

    private List<PieceDto> getInitPieceDtos() {
        Map<Position, Piece> initPieces = PieceFactory.createChessPieces();
        return initPieces.entrySet()
                .stream()
                .map(entry -> PieceDto.of(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    public ChessResponseDto movePiece(final int id, final MoveCommand moveCommand) {
        try {
            ChessGame game = getGame(id);
            game.proceedWith(moveCommand);
            pieceDao.remove(id, moveCommand.to());
            pieceDao.modifyPosition(id, moveCommand.from(), moveCommand.to());
            gameDao.modify(id, GameDto.of(game.getTurnColor(), game.isRunning()));
            return getChess(id);
        } catch (Exception e) {
            throw new IllegalArgumentException("기물을 움직일 수 없습니다.");
        }
    }

    private ChessGame getGame(final int id) {
        try {
            List<PieceDto> pieceDtos = pieceDao.findAll(id);
            GameDto gameDto = gameDao.find(id);
            Map<Position, Piece> pieces = pieceDtos.stream()
                    .map(PieceDto::toPieceEntry)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            return new ChessGame(pieces, PieceColor.find(gameDto.getTurn()));
        } catch (Exception e) {
            throw new IllegalArgumentException("게임 정보를 불러올 수 없습니다.");
        }
    }

    public ScoresDto getScore(final int id) {
        ChessGame game = getGame(id);
        Map<PieceColor, Score> scoresByColor = game.calculateScoreByColor();
        return ScoresDto.of(scoresByColor);
    }

    public ScoresDto finishGame(final int id) {
        try {
            gameDao.modifyStatus(id, GameStatusDto.FINISHED);
            return getScore(id);
        } catch (Exception e) {
            throw new IllegalArgumentException("게임을 종료시킬 수 없습니다.");
        }
    }
}
