package wooteco.chess.service.spring;

import org.springframework.stereotype.Service;

import wooteco.chess.db.entity.PlayerEntity;
import wooteco.chess.db.repository.PlayerRepository;

@Service
public class SpringPlayerService {
	private final PlayerRepository playerRepository;

	public SpringPlayerService(PlayerRepository playerRepository) {
		this.playerRepository = playerRepository;
	}

	public long save(PlayerEntity playerEntity) {
		return playerRepository.save(playerEntity).getId();
	}
}
