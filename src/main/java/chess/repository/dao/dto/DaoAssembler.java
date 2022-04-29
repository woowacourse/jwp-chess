package chess.repository.dao.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import chess.domain.Color;
import chess.domain.Position;
import chess.domain.game.Game;
import chess.domain.game.state.GameStateFactory;
import chess.domain.piece.Piece;
import chess.domain.player.Player;
import chess.domain.player.Players;
import chess.repository.dao.dto.game.GameDto;
import chess.repository.dao.dto.game.GameUpdateDto;
import chess.repository.dao.dto.player.PlayerDto;

public class DaoAssembler {

    private DaoAssembler() {
    }

    public static Game game(final Long gameId, final List<Player> players, final GameDto gameDto) {
        return Game.loadGame(gameId, gameDto.getTitle(), gameDto.getPassword(), GameStateFactory.loadGameState(
                new Players(players), gameDto.getFinished(), Color.from(gameDto.getCurrentTurnColor())));
    }

    public static GameDto gameDto(final Game game, final List<Player> players) {
        final Color currentTurnColor = game.getColorOfCurrentTurn();
        final Boolean finished = game.isFinished();
        return new GameDto(0L, game.getTitle(), game.getPassword(),
                players.get(0).getId(), players.get(1).getId(),
                finished, currentTurnColor.getName());
    }

    public static GameUpdateDto gameUpdateDto(final Game game) {
        final Long gameId = game.getId();
        final Boolean finished = game.isFinished();
        final Color currentTurnColor = game.getColorOfCurrentTurn();
        return new GameUpdateDto(gameId, finished, currentTurnColor.getName());
    }

    public static Player player(final Long playerId, final PlayerDto playerDto) {
        final Color color = Color.from(playerDto.getColorName());
        final Map<Position, Piece> pieces = convertStringToPieces(color, playerDto.getPieces());
        return new Player(playerId, color, pieces);
    }

    public static Player player(final PlayerDto playerDto) {
        return player(playerDto.getId(), playerDto);
    }

    public static PlayerDto playerDto(final Player player) {
        final Color color = player.getColor();
        final Map<Position, Piece> pieces = player.getPieces();
        return new PlayerDto(player.getId(), color.getName(), convertPiecesToString(pieces));
    }

    private static String convertPiecesToString(final Map<Position, Piece> pieces) {
        final List<String> values = new ArrayList<>();
        for (Entry<Position, Piece> entry : pieces.entrySet()) {
            final Position position = entry.getKey();
            final Piece piece = entry.getValue();
            values.add(concatPositionAndPiece(position, piece));
        }
        return String.join(",", values);
    }

    private static String concatPositionAndPiece(final Position position, final Piece piece) {
        return String.format("%s:%s", convertPositionToString(position), piece.getPieceName());
    }

    private static String convertPositionToString(final Position position) {
        return String.valueOf(position.getColumn()) + position.getRow();
    }

    private static Map<Position, Piece> convertStringToPieces(final Color color, final String pieceUnits) {
        return Arrays.stream(pieceUnits.split(","))
                .map(pieceUnit -> pieceUnit.split(":"))
                .collect(Collectors.toMap(
                        value -> Position.from(value[0]),
                        value -> PieceConvertor.convertToPiece(color, value[1])
                ));
    }

}
