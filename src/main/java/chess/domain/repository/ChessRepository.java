package chess.domain.repository;

import chess.domain.entity.Entity;

public interface ChessRepository<E extends Entity<K>, K> {

    K save(E e);

    K update(E e);

    E findById(Long id);
}
