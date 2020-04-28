package wooteco.chess.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import wooteco.chess.domain.entity.Piece;

public interface PieceRepository extends CrudRepository<Piece, Long> {
}