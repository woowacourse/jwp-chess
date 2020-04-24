package wooteco.chess.dao;

import wooteco.chess.dto.Commands;

import java.util.List;

public abstract class AbstractChessDao implements ChessDao {
    @Override
    public List<Commands> findAll() {
        return null;
    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Commands> S save(S entity) {
        return null;
    }
}
