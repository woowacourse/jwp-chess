package wooteco.chess.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.chess.domain.Color;
import wooteco.chess.domain.GameManager;
import wooteco.chess.domain.PieceScore;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.PieceMapper;
import wooteco.chess.domain.piece.Pieces;
import wooteco.chess.dto.GameResponseDto;
import wooteco.chess.dto.GameStatusDto;
import wooteco.chess.dto.MoveRequestDto;
import wooteco.chess.dto.PiecesResponseDto;
import wooteco.chess.repository.RoomRepository;
import wooteco.chess.repository.entity.GameEntity;
import wooteco.chess.repository.entity.PieceEntity;
import wooteco.chess.repository.entity.RoomEntity;

@Service
public class SpringGameService {

    @Autowired
    private RoomRepository roomRepository;

    @Transactional
    public GameResponseDto initialize(Long roomId) throws SQLException {
        RoomEntity roomEntity = roomRepository.findById(roomId)
            .orElseThrow(RuntimeException::new);
        Set<PieceEntity> pieceEntities = convertPiecesToPieceEntity(Pieces.initPieces());
        GameEntity gameEntity = new GameEntity(Color.WHITE, pieceEntities);
        roomEntity.updateGame(gameEntity);
        RoomEntity persistEntity = roomRepository.save(roomEntity);

        return convertRoomEntityToGameDto(persistEntity);
    }

    public GameResponseDto move(MoveRequestDto moveRequestDto) throws SQLException {
        Long id = moveRequestDto.getRoomId();
        String sourcePosition = moveRequestDto.getSourcePosition();
        String targetPosition = moveRequestDto.getTargetPosition();

        RoomEntity roomEntity = roomRepository.findById(id)
            .orElseThrow(RuntimeException::new);
        GameEntity gameEntity = roomEntity.getGame();

        Pieces pieces = new Pieces(convertPieceEntityToPieces(gameEntity.getPiece()));

        GameManager gameManager = new GameManager(pieces, gameEntity.getTurn());
        gameManager.validateEndGame();
        gameManager.moveFromTo(Position.of(sourcePosition), Position.of(targetPosition));

        Pieces movedPieces = gameManager.getPieces();
        gameEntity.updatePiece(convertPiecesToPieceEntity(movedPieces.getPieces()));
        gameEntity.updateTurn(gameManager.getCurrentColor());

        roomEntity.updateGame(gameEntity);
        roomRepository.save(roomEntity);

        return convertRoomEntityToGameDto(roomEntity);
    }

    public GameStatusDto getScore(final Long roomId) throws SQLException {
        RoomEntity roomEntity = roomRepository.findById(roomId)
            .orElseThrow(RuntimeException::new);

        GameEntity gameEntity = roomEntity.getGame();

        Map<Position, Piece> originPieces = convertPieceEntityToPieces(gameEntity.getPiece());
        Pieces pieces = new Pieces(originPieces);
        GameManager gameManager = new GameManager(pieces, gameEntity.getTurn());

        return new GameStatusDto(PieceScore.calculateByColor(gameManager, Color.WHITE),
            PieceScore.calculateByColor(gameManager, Color.BLACK));
    }

    public List<String> getMovablePositions(final MoveRequestDto moveRequestDto) throws
        SQLException {
        Long id = moveRequestDto.getRoomId();
        String sourcePosition = moveRequestDto.getSourcePosition();

        RoomEntity roomEntity = roomRepository.findById(id)
            .orElseThrow(RuntimeException::new);
        GameEntity game = roomEntity.getGame();
        Map<Position, Piece> originPieces = convertPieceEntityToPieces(game.getPiece());
        Pieces pieces = new Pieces(originPieces);

        GameManager gameManager = new GameManager(pieces, game.getTurn());

        return gameManager.getMovablePositions(Position.of(sourcePosition));
    }

    private GameResponseDto convertRoomEntityToGameDto(final RoomEntity roomEntity) {
        GameEntity game = roomEntity.getGame();
        Color turn = game.getTurn();
        Set<PieceEntity> pieceEntities = game.getPiece();
        Map<Position, Piece> originPieces = convertPieceEntityToPieces(pieceEntities);
        Pieces pieces = new Pieces(originPieces);
        boolean kingDead = pieces.isKingDead(turn);

        return new GameResponseDto(new PiecesResponseDto(pieces).getPieces(), turn, kingDead);
    }

    private Set<PieceEntity> convertPiecesToPieceEntity(final Map<Position, Piece> pieces) {
        return pieces.entrySet().stream()
            .map(entry ->
                new PieceEntity(entry.getValue().getSymbol(),
                    entry.getValue().getColor().name(),
                    entry.getKey().getPosition()))
            .collect(Collectors.toSet());
    }

    private Map<Position, Piece> convertPieceEntityToPieces(final Set<PieceEntity> entity) {
        return entity.stream()
            .collect(Collectors.toMap(
                pieceEntity -> Position.of(pieceEntity.getPosition()),
                pieceEntity -> PieceMapper.getInstance().findDBPiece(pieceEntity.getName())
            ));
    }

    public GameResponseDto findAllPieces(final Long roomId) {
        return convertRoomEntityToGameDto(roomRepository
            .findById(roomId)
            .orElseThrow(RuntimeException::new)
        );
    }
}
