package wooteco.chess.service.spring;

import org.springframework.stereotype.Service;

import wooteco.chess.dao.repository.PlayerRepository;
import wooteco.chess.dto.PlayerDto;

@Service
public class SpringPlayerService {
	private final PlayerRepository playerRepository;

	public SpringPlayerService(PlayerRepository playerRepository) {
		this.playerRepository = playerRepository;
	}

	public PlayerDto save(PlayerDto playerDto) {
		return playerRepository.save(playerDto);
	}
}
