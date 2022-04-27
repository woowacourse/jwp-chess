package chess.dao;

import chess.domain.pieces.Color;

import java.util.List;

public interface BoardDao<T> {

    T save(T target);

    T getById(int id);

    List<T> findAll();

    int deleteByIdAndPassword(int id, String password);

    void deleteAll();

    void updateTurn(Color color, int id);
}
