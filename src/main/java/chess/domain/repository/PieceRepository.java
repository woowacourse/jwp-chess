package chess.domain.repository;

import chess.dao.PieceDao;
import chess.dao.dto.piece.PieceDto;
import chess.domain.entity.Piece;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class PieceRepository implements ChessRepository<Piece, Long> {

    private final PieceDao pieceDao;
    private final ModelMapper modelMapper;

    public PieceRepository(PieceDao pieceDao, ModelMapper modelMapper) {
        this.pieceDao = pieceDao;
        this.modelMapper = modelMapper;
    }

    @Override
    public Long save(Piece piece) {
        PieceDto pieceDto = modelMapper.map(piece, PieceDto.class);
        return pieceDao.save(pieceDto);
    }

    public long[] savePieces(Long gameId, List<Piece> pieces) {
        List<PieceDto> pieceDtos = pieces.stream()
                .map(piece -> modelMapper.map(piece, PieceDto.class))
                .collect(Collectors.toList());
        return pieceDao.savePieces(gameId, pieceDtos);
    }

    @Override
    public Long update(Piece piece) {
        PieceDto pieceDto = modelMapper.map(piece, PieceDto.class);
        return pieceDao.updateByGameIdAndPosition(pieceDto);
    }

    @Override
    public Piece findById(Long id) {
        PieceDto pieceDto = pieceDao.findById(id);
        return modelMapper.map(pieceDto, Piece.class);
    }

    public List<Piece> findPiecesByGameId(Long gameId) {
        List<PieceDto> pieceDtos = pieceDao.findPiecesByGameId(gameId);
        return pieceDtos.stream()
                .map(pieceDto -> modelMapper.map(pieceDto, Piece.class))
                .collect(Collectors.toList());
    }
}
