package chess.service;

import chess.controller.dto.request.MoveRequestDTO;
import chess.controller.dto.response.ChessGameResponseDto;
import chess.controller.dto.response.GameStatusResponseDto;
import chess.controller.dto.response.MoveResponseDto;
import chess.domain.ChessGame;
import chess.domain.position.Position;
import chess.repository.ChessGameRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ChessGameService {

    private final ChessGameRepository chessGameRepository;

    public ChessGameService(ChessGameRepository chessGameRepository) {
        this.chessGameRepository = chessGameRepository;
    }

    public List<ChessGameResponseDto> getAllGames() {
        List<ChessGame> allChessGames = chessGameRepository.findAll();
        return allChessGames.stream()
            .map(ChessGameResponseDto::new)
            .collect(Collectors.toList());
    }

    public Long createNewChessGame(String gameTitle) {
        ChessGame chessGame = new ChessGame(gameTitle);
        return chessGameRepository.save(chessGame);
    }

    public GameStatusResponseDto getGameStatus(Long gameId) {
        ChessGame chessGame = chessGameRepository.findById(gameId);
        return new GameStatusResponseDto(chessGame);
    }

    public void deleteGame(Long gameId) {
        chessGameRepository.deleteById(gameId);
    }

    public MoveResponseDto movePiece(MoveRequestDTO moveRequestDTO) {
        ChessGame chessGame = chessGameRepository.findById(moveRequestDTO.getGameId());
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
