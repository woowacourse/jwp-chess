package chess.service.dto;

import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import chess.domain.Color;
import chess.domain.Position;
import chess.domain.game.Game;
import chess.domain.piece.Piece;
import chess.domain.player.Players;
import chess.repository.dto.game.GameStatus;
import chess.service.dto.response.ColorResponseDto;
import chess.service.dto.response.GameResponseDto;
import chess.service.dto.response.GameStatusResponseDto;
import chess.service.dto.response.PieceResponseDto;
import chess.service.dto.response.PlayerResponseDto;
import chess.service.dto.response.PlayerScoresResponseDto;
import chess.service.dto.response.PlayersResponseDto;
import chess.service.dto.response.PositionResponseDto;

public class ChessDtoAssembler {

    private ChessDtoAssembler() {
    }

    public static GameResponseDto gameResponseDto(final Game game) {
        return new GameResponseDto(game.getId(), playersResponseDto(game.getPlayers()),
                game.isFinished(), game.isPromotable(),
                colorResponseDto(game.getColorOfCurrentTurn()));
    }

    public static GameStatusResponseDto gameStatusResponseDto(final GameStatus gameStatus) {
        return new GameStatusResponseDto(gameStatus.getId(), gameStatus.getTitle(), gameStatus.getFinished());
    }

    private static PlayersResponseDto playersResponseDto(final Players players) {
        return new PlayersResponseDto(Arrays.stream(Color.values())
                .collect(Collectors.toUnmodifiableMap(
                        ChessDtoAssembler::colorResponseDto,
                        color -> playerResponseDto(players.getPiecesByPlayer(color)))
                ));
    }

    private static PlayerResponseDto playerResponseDto(final Map<Position, Piece> pieceUnits) {
        return new PlayerResponseDto(pieceUnits.entrySet()
                .stream()
                .collect(Collectors.toUnmodifiableMap(
                        pieceUnit -> positionResponseDto(pieceUnit.getKey()),
                        pieceUnit -> pieceResponseDto(pieceUnit.getValue())
                )));
    }

    private static PositionResponseDto positionResponseDto(final Position position) {
        return new PositionResponseDto(String.valueOf(position.getColumn()) + position.getRow());
    }

    private static PieceResponseDto pieceResponseDto(final Piece piece) {
        return new PieceResponseDto(piece.getPieceName());
    }

    private static ColorResponseDto colorResponseDto(final Color color) {
        return new ColorResponseDto(color.getName());
    }

    public static PlayerScoresResponseDto playerScoresResponseDto(final Map<Color, Double> playerScores) {
        return new PlayerScoresResponseDto(playerScores.entrySet()
                .stream()
                .collect(Collectors.toUnmodifiableMap(
                        entry -> entry.getKey().getName(),
                        Entry::getValue
                )));
    }

}
