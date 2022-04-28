package chess.repository;

import chess.repository.dto.player.PlayerDto;

public interface PlayerDao {

    Long save(final PlayerDto playerDto);

    PlayerDto findById(final Long id);

    void update(final PlayerDto playerDto);

    void remove(final Long id);

}
