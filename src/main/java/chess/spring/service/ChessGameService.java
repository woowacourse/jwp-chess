package chess.spring.service;

import chess.spring.controller.dto.request.MoveRequestDTO;
import chess.spring.controller.dto.response.ChessGameDtoNew;
import chess.spring.controller.dto.response.GameStatusDto;
import chess.spring.controller.dto.response.MoveResponseDto;
import chess.spring.domain.ChessGameNew;
import chess.spring.repository.ChessGameRepositoryNew;
import chess.web.domain.position.Position;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ChessGameService {

    private final ChessGameRepositoryNew chessGameRepository;

    public ChessGameService(ChessGameRepositoryNew chessGameRepository) {
        this.chessGameRepository = chessGameRepository;
    }

    public List<ChessGameDtoNew> getAllGames() {
        List<ChessGameNew> allChessGames = chessGameRepository.findAll();
        return allChessGames.stream()
            .map(ChessGameDtoNew::new)
            .collect(Collectors.toList());
    }

    public Long createNewChessGame(String gameTitle) {
        ChessGameNew chessGame = new ChessGameNew(gameTitle);
        return chessGameRepository.save(chessGame);
    }

    public GameStatusDto getGameStatus(Long gameId) {
        ChessGameNew chessGame = chessGameRepository.findById(gameId);
        return new GameStatusDto(chessGame);
    }

    public void deleteGame(Long gameId) {
        chessGameRepository.deleteById(gameId);
    }

    public MoveResponseDto movePiece(MoveRequestDTO moveRequestDTO) {
        ChessGameNew chessGame = chessGameRepository.findById(moveRequestDTO.getGameId());
        Position startPosition = Position.of(moveRequestDTO.getStartPosition());
        Position destination = Position.of(moveRequestDTO.getDestination());
        try {
            chessGame.movePiece(startPosition, destination);
        } catch (IllegalArgumentException e) {
            return new MoveResponseDto(true, e.getMessage());
        }
        chessGameRepository.update(chessGame);
        return new MoveResponseDto(false);
    }
}
