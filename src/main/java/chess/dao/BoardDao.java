package chess.dao;

import chess.domain.pieces.Color;

import java.util.List;
import java.util.Optional;

public interface BoardDao<T> {

    T save(T target);

    Optional<T> findById(int id);

    List<T> findAll();

    int deleteByIdAndPassword(int id, String password);

    void deleteAll();

    void updateTurn(Color color, int id);
}
