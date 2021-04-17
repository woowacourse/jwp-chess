package chess.domain.board;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.domain.board.move.MoveRequest;
import chess.domain.board.score.Scores;
import chess.domain.color.type.TeamColor;
import chess.domain.position.Position;
import chess.utils.position.converter.PositionConverter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class BoardTest {

    private static final String INITIAL_BOARD_STATUS = ""
        + "RNBQKBNR"
        + "PPPPPPPP"
        + "........"
        + "........"
        + "........"
        + "........"
        + "pppppppp"
        + "rnbqkbnr";

    @DisplayName("출발 위치에 자신의 기물이 없는 경우, 이동 불가 - 빈 칸인 경우")
    @Test
    void cannotMovePieceWhenStartPositionEmpty() {
        Board board = new Board(INITIAL_BOARD_STATUS);
        TeamColor currentTurnTeamColor = TeamColor.WHITE;
        Position startPosition = Position.of("a3");
        Position destination = Position.of("a4");
        MoveRequest moveRequest = new MoveRequest(currentTurnTeamColor, startPosition, destination);

        assertThatThrownBy(() -> board.movePiece(moveRequest))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("출발 위치에 기물이 존재하지 않습니다.");
    }

    @DisplayName("출발 위치에 자신의 기물이 없는 경우, 이동 불가 - 적의 기물이 있는 경우")
    @Test
    void cannotMovePieceWhenStartPositionEnemyPiece() {
        Board board = new Board(INITIAL_BOARD_STATUS);
        TeamColor currentTurnTeamColor = TeamColor.WHITE;
        Position startPosition = Position.of("d7");
        Position destination = Position.of("d6");
        MoveRequest moveRequest = new MoveRequest(currentTurnTeamColor, startPosition, destination);

        assertThatThrownBy(() -> board.movePiece(moveRequest))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("자신의 기물이 아닙니다.");
    }

    @DisplayName("King이 잡혔는지 확인")
    @Nested
    class KingDead {
        @DisplayName("King이 1개만 잡혔을 때")
        @Test
        void isOneKingDead() {
            Board board = new Board(""
                + ".KR....."
                + "P.PB...."
                + ".P..Q..."
                + "........"
                + ".....nq."
                + ".....p.p"
                + ".....pp."
                + "....r..."
            );

            assertThat(board.isKingDead()).isTrue();
        }

        @DisplayName("2개의 킹들이 모두 잡혔을 때")
        @Test
        void isAllKingsDead() {
            Board board = new Board(""
                + "..R....."
                + "P.PB...."
                + ".P..Q..."
                + "........"
                + ".....nq."
                + ".....p.p"
                + ".....pp."
                + "....r..."
            );

            assertThat(board.isKingDead()).isTrue();
        }

        @DisplayName("King이 잡히지 않았을 때")
        @Test
        void isNotKingDead() {
            Board board = new Board(""
                + ".KR....."
                + "P.PB...."
                + ".P..Q..."
                + "........"
                + ".....nq."
                + ".....p.p"
                + ".....pp."
                + "....rk.."
            );

            assertThat(board.isKingDead()).isFalse();
        }
    }

    @DisplayName("점수 계산")
    @Nested
    class ScoreCalculate {
        @DisplayName("Pawn이 한 File에 2개 이상 존재하는 경우")
        @Test
        void scores() {
            Board board = new Board(""
                + ".KR....."
                + "P.PB...."
                + ".P..Q..."
                + "........"
                + ".....nq."
                + ".....p.p"
                + ".....pp."
                + "....rk.."
            );

            Scores scores = board.getScores();
            assertThat(scores.getWhitePlayerScore()).isEqualTo(19.5);
            assertThat(scores.getBlackPlayerScore()).isEqualTo(20);
        }
    }

    @DisplayName("말 이동 가능 여부 판단")
    @Nested
    class Movable {

        @DisplayName("Rook, Bishop, Queen, King")
        @Nested
        class RookBishopQueenKing {

            @DisplayName("Rook 이동")
            @Nested
            class Rook {
                @DisplayName("유효하지 않은 경로로 이동할 수 없다.")
                @ParameterizedTest
                @ValueSource(strings = {"b7", "e7", "g1"})
                void cannotMoveInvalidRoute(String destinationInput) {
                    Board board = new Board(""
                        + "........"
                        + "........"
                        + "........"
                        + "...R...."
                        + "........"
                        + "........"
                        + "........"
                        + "........"
                    );

                    String startPositionInput = "d5";

                    assertCannotMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
                }

                @DisplayName("위 방향으로 이동")
                @Test
                void moveUp() {
                    Board board = new Board(""
                        + "........"
                        + "........"
                        + "........"
                        + "...R...."
                        + "........"
                        + "........"
                        + "........"
                        + "........"
                    );

                    String startPositionInput = "d5";
                    String destinationInput = "d8";

                    assertCanMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
                }

                @DisplayName("아래 방향으로 이동")
                @Test
                void moveDown() {
                    Board board = new Board(""
                        + "........"
                        + "........"
                        + "........"
                        + "...R...."
                        + "........"
                        + "........"
                        + "........"
                        + "........"
                    );

                    String startPositionInput = "d5";
                    String destinationInput = "d1";

                    assertCanMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
                }

                @DisplayName("오른쪽 방향으로 이동")
                @Test
                void moveRight() {
                    Board board = new Board(""
                        + "........"
                        + "........"
                        + "........"
                        + "...R...."
                        + "........"
                        + "........"
                        + "........"
                        + "........"
                    );

                    String startPositionInput = "d5";
                    String destinationInput = "h5";

                    assertCanMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
                }

                @DisplayName("왼쪽 방향으로 이동")
                @Test
                void moveLeft() {
                    Board board = new Board(""
                        + "........"
                        + "........"
                        + "........"
                        + "...R...."
                        + "........"
                        + "........"
                        + "........"
                        + "........"
                    );

                    String startPositionInput = "d5";
                    String destinationInput = "a5";

                    assertCanMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
                }

                @DisplayName("이동경로 중간에 기물이 존재하면, 이동할 수 없다.")
                @Test
                void cannotMoveWhenPieceExistsOnRoute() {
                    Board board = new Board(""
                        + "........"
                        + "........"
                        + "........"
                        + ".b.R...."
                        + "........"
                        + "........"
                        + "........"
                        + "........"
                    );

                    String startPositionInput = "d5";
                    String destinationInput = "a5";

                    assertCannotMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
                }

                @DisplayName("도착위치에 아군 기물이 존재하면, 이동할 수 없다.")
                @Test
                void cannotMoveWhenMyPieceExistsAtDestination() {
                    Board board = new Board(""
                        + "........"
                        + "........"
                        + "........"
                        + "P..R...."
                        + "........"
                        + "........"
                        + "........"
                        + "........"
                    );

                    String startPositionInput = "d5";
                    String destinationInput = "a5";

                    assertCannotMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
                }

                @DisplayName("도착위치에 적 기물이 존재하면, 이동할 수 있다.")
                @Test
                void canMoveWhenEnemyPieceExistsAtDestination() {
                    Board board = new Board(""
                        + "........"
                        + "........"
                        + "........"
                        + "b..R...."
                        + "........"
                        + "........"
                        + "........"
                        + "........"
                    );

                    String startPositionInput = "d5";
                    String destinationInput = "a5";

                    assertCanMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
                }
            }

            @DisplayName("Bishop 이동")
            @Nested
            class Bishop {
                @DisplayName("유효하지 않은 경로로 이동할 수 없다.")
                @ParameterizedTest
                @ValueSource(strings = {"b5", "e7", "g1"})
                void cannotMoveInvalidRoute(String destinationInput) {
                    Board board = new Board(""
                        + "........"
                        + "........"
                        + "........"
                        + "...B...."
                        + "........"
                        + "........"
                        + "........"
                        + "........"
                    );

                    String startPositionInput = "d5";

                    assertCannotMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
                }

                @DisplayName("왼쪽 위 대각선 방향으로 이동")
                @Test
                void moveLeftUpDiagonal() {
                    Board board = new Board(""
                        + "........"
                        + "........"
                        + "........"
                        + "...B...."
                        + "........"
                        + "........"
                        + "........"
                        + "........"
                    );

                    String startPositionInput = "d5";
                    String destinationInput = "a8";

                    assertCanMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
                }

                @DisplayName("왼쪽 아래 대각선 방향으로 이동")
                @Test
                void moveLeftDownDiagonal() {
                    Board board = new Board(""
                        + "........"
                        + "........"
                        + "........"
                        + "...B...."
                        + "........"
                        + "........"
                        + "........"
                        + "........"
                    );

                    String startPositionInput = "d5";
                    String destinationInput = "a2";

                    assertCanMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
                }

                @DisplayName("오른쪽 위 대각선 방향으로 이동")
                @Test
                void moveRightUpDiagonal() {
                    Board board = new Board(""
                        + "........"
                        + "........"
                        + "........"
                        + "...B...."
                        + "........"
                        + "........"
                        + "........"
                        + "........"
                    );

                    String startPositionInput = "d5";
                    String destinationInput = "g8";

                    assertCanMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
                }

                @DisplayName("오른쪽 아래 대각선 방향으로 이동")
                @Test
                void moveRightDownDiagonal() {
                    Board board = new Board(""
                        + "........"
                        + "........"
                        + "........"
                        + "...B...."
                        + "........"
                        + "........"
                        + "........"
                        + "........"
                    );

                    String startPositionInput = "d5";
                    String destinationInput = "h1";

                    assertCanMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
                }

                @DisplayName("이동경로 중간에 기물이 존재하면, 이동할 수 없다.")
                @Test
                void cannotMoveWhenPieceExistsOnRoute() {
                    Board board = new Board(""
                        + "........"
                        + ".b......"
                        + "........"
                        + "...B...."
                        + "........"
                        + "........"
                        + "........"
                        + "........"
                    );

                    String startPositionInput = "d5";
                    String destinationInput = "a8";

                    assertCannotMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
                }

                @DisplayName("도착위치에 아군 기물이 존재하면, 이동할 수 없다.")
                @Test
                void cannotMoveWhenMyPieceExistsAtDestination() {
                    Board board = new Board(""
                        + "P......."
                        + "........"
                        + "........"
                        + "...B...."
                        + "........"
                        + "........"
                        + "........"
                        + "........"
                    );

                    String startPositionInput = "d5";
                    String destinationInput = "a8";

                    assertCannotMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
                }

                @DisplayName("도착위치에 적 기물이 존재하면, 이동할 수 있다.")
                @Test
                void canMoveWhenEnemyPieceExistsAtDestination() {
                    Board board = new Board(""
                        + "p......."
                        + "........"
                        + "........"
                        + "...B...."
                        + "........"
                        + "........"
                        + "........"
                        + "........"
                    );

                    String startPositionInput = "d5";
                    String destinationInput = "a8";

                    assertCanMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
                }
            }

            @DisplayName("Queen 이동")
            @Nested
            class Queen {
                @DisplayName("유효하지 않은 경로로 이동할 수 없다.")
                @ParameterizedTest
                @ValueSource(strings = {"b4", "e7", "g1"})
                void cannotMoveInvalidRoute(String destinationInput) {
                    Board board = new Board(""
                        + "........"
                        + "........"
                        + "........"
                        + "...Q...."
                        + "........"
                        + "........"
                        + "........"
                        + "........"
                    );

                    String startPositionInput = "d5";

                    assertCannotMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
                }

                @DisplayName("위 방향으로 이동")
                @Test
                void moveUp() {
                    Board board = new Board(""
                        + "........"
                        + "........"
                        + "........"
                        + "...Q...."
                        + "........"
                        + "........"
                        + "........"
                        + "........"
                    );

                    String startPositionInput = "d5";
                    String destinationInput = "d8";

                    assertCanMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
                }

                @DisplayName("아래 방향으로 이동")
                @Test
                void moveDown() {
                    Board board = new Board(""
                        + "........"
                        + "........"
                        + "........"
                        + "...Q...."
                        + "........"
                        + "........"
                        + "........"
                        + "........"
                    );

                    String startPositionInput = "d5";
                    String destinationInput = "d1";

                    assertCanMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
                }

                @DisplayName("오른쪽 방향으로 이동")
                @Test
                void moveRight() {
                    Board board = new Board(""
                        + "........"
                        + "........"
                        + "........"
                        + "...Q...."
                        + "........"
                        + "........"
                        + "........"
                        + "........"
                    );

                    String startPositionInput = "d5";
                    String destinationInput = "h5";

                    assertCanMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
                }

                @DisplayName("왼쪽 방향으로 이동")
                @Test
                void moveLeft() {
                    Board board = new Board(""
                        + "........"
                        + "........"
                        + "........"
                        + "...Q...."
                        + "........"
                        + "........"
                        + "........"
                        + "........"
                    );

                    String startPositionInput = "d5";
                    String destinationInput = "a5";

                    assertCanMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
                }

                @DisplayName("왼쪽 위 대각선 방향으로 이동")
                @Test
                void moveLeftUpDiagonal() {
                    Board board = new Board(""
                        + "........"
                        + "........"
                        + "........"
                        + "...Q...."
                        + "........"
                        + "........"
                        + "........"
                        + "........"
                    );

                    String startPositionInput = "d5";
                    String destinationInput = "a8";

                    assertCanMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
                }

                @DisplayName("왼쪽 아래 대각선 방향으로 이동")
                @Test
                void moveLeftDownDiagonal() {
                    Board board = new Board(""
                        + "........"
                        + "........"
                        + "........"
                        + "...Q...."
                        + "........"
                        + "........"
                        + "........"
                        + "........"
                    );

                    String startPositionInput = "d5";
                    String destinationInput = "a2";

                    assertCanMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
                }

                @DisplayName("오른쪽 위 대각선 방향으로 이동")
                @Test
                void moveRightUpDiagonal() {
                    Board board = new Board(""
                        + "........"
                        + "........"
                        + "........"
                        + "...Q...."
                        + "........"
                        + "........"
                        + "........"
                        + "........"
                    );

                    String startPositionInput = "d5";
                    String destinationInput = "g8";

                    assertCanMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
                }

                @DisplayName("오른쪽 아래 대각선 방향으로 이동")
                @Test
                void moveRightDownDiagonal() {
                    Board board = new Board(""
                        + "........"
                        + "........"
                        + "........"
                        + "...Q...."
                        + "........"
                        + "........"
                        + "........"
                        + "........"
                    );

                    String startPositionInput = "d5";
                    String destinationInput = "h1";

                    assertCanMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
                }

                @DisplayName("이동경로 중간에 기물이 존재하면, 이동할 수 없다.")
                @Test
                void cannotMoveWhenPieceExistsOnRoute() {
                    Board board = new Board(""
                        + "........"
                        + ".b......"
                        + "........"
                        + "...Q...."
                        + "........"
                        + "........"
                        + "........"
                        + "........"
                    );

                    String startPositionInput = "d5";
                    String destinationInput = "a8";

                    assertCannotMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
                }

                @DisplayName("도착위치에 아군 기물이 존재하면, 이동할 수 없다.")
                @Test
                void cannotMoveWhenMyPieceExistsAtDestination() {
                    Board board = new Board(""
                        + "P......."
                        + "........"
                        + "........"
                        + "...Q...."
                        + "........"
                        + "........"
                        + "........"
                        + "........"
                    );

                    String startPositionInput = "d5";
                    String destinationInput = "a8";

                    assertCannotMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
                }

                @DisplayName("도착위치에 적 기물이 존재하면, 이동할 수 있다.")
                @Test
                void canMoveWhenEnemyPieceExistsAtDestination() {
                    Board board = new Board(""
                        + "p......."
                        + "........"
                        + "........"
                        + "...Q...."
                        + "........"
                        + "........"
                        + "........"
                        + "........"
                    );

                    String startPositionInput = "d5";
                    String destinationInput = "a8";

                    assertCanMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
                }
            }

            @DisplayName("King 이동")
            @Nested
            class King {
                @DisplayName("유효하지 않은 경로로 이동할 수 없다.")
                @ParameterizedTest
                @ValueSource(strings = {"b7", "e7", "d3"})
                void cannotMoveInvalidRoute(String destinationInput) {
                    Board board = new Board(""
                        + "........"
                        + "........"
                        + "........"
                        + "...K...."
                        + "........"
                        + "........"
                        + "........"
                        + "........"
                    );

                    String startPositionInput = "d5";

                    assertCannotMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
                }

                @DisplayName("위 방향으로 한 칸 이동")
                @Test
                void moveUp() {
                    Board board = new Board(""
                        + "........"
                        + "........"
                        + "........"
                        + "...K...."
                        + "........"
                        + "........"
                        + "........"
                        + "........"
                    );

                    String startPositionInput = "d5";
                    String destinationInput = "d6";

                    assertCanMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
                }

                @DisplayName("아래 방향으로 한 칸 이동")
                @Test
                void moveDown() {
                    Board board = new Board(""
                        + "........"
                        + "........"
                        + "........"
                        + "...K...."
                        + "........"
                        + "........"
                        + "........"
                        + "........"
                    );

                    String startPositionInput = "d5";
                    String destinationInput = "d4";

                    assertCanMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
                }

                @DisplayName("오른쪽 방향으로 한 칸 이동")
                @Test
                void moveRight() {
                    Board board = new Board(""
                        + "........"
                        + "........"
                        + "........"
                        + "...K...."
                        + "........"
                        + "........"
                        + "........"
                        + "........"
                    );

                    String startPositionInput = "d5";
                    String destinationInput = "e5";

                    assertCanMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
                }

                @DisplayName("왼쪽 방향으로 한 칸 이동")
                @Test
                void moveLeft() {
                    Board board = new Board(""
                        + "........"
                        + "........"
                        + "........"
                        + "...K...."
                        + "........"
                        + "........"
                        + "........"
                        + "........"
                    );

                    String startPositionInput = "d5";
                    String destinationInput = "c5";

                    assertCanMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
                }

                @DisplayName("왼쪽 위 대각선 방향으로 한 칸 이동")
                @Test
                void moveLeftUpDiagonal() {
                    Board board = new Board(""
                        + "........"
                        + "........"
                        + "........"
                        + "...K...."
                        + "........"
                        + "........"
                        + "........"
                        + "........"
                    );

                    String startPositionInput = "d5";
                    String destinationInput = "c6";

                    assertCanMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
                }

                @DisplayName("왼쪽 아래 대각선 방향으로 한 칸 이동")
                @Test
                void moveLeftDownDiagonal() {
                    Board board = new Board(""
                        + "........"
                        + "........"
                        + "........"
                        + "...K...."
                        + "........"
                        + "........"
                        + "........"
                        + "........"
                    );

                    String startPositionInput = "d5";
                    String destinationInput = "c4";

                    assertCanMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
                }

                @DisplayName("오른쪽 위 대각선 방향으로 한 칸 이동")
                @Test
                void moveRightUpDiagonal() {
                    Board board = new Board(""
                        + "........"
                        + "........"
                        + "........"
                        + "...K...."
                        + "........"
                        + "........"
                        + "........"
                        + "........"
                    );

                    String startPositionInput = "d5";
                    String destinationInput = "e6";

                    assertCanMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
                }

                @DisplayName("오른쪽 아래 대각선 방향으로 한 칸 이동")
                @Test
                void moveRightDownDiagonal() {
                    Board board = new Board(""
                        + "........"
                        + "........"
                        + "........"
                        + "...K...."
                        + "........"
                        + "........"
                        + "........"
                        + "........"
                    );

                    String startPositionInput = "d5";
                    String destinationInput = "e4";

                    assertCanMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
                }

                @DisplayName("도착위치에 아군 기물이 존재하면, 이동할 수 없다.")
                @Test
                void cannotMoveWhenMyPieceExistsAtDestination() {
                    Board board = new Board(""
                        + "........"
                        + "........"
                        + "..P....."
                        + "...K...."
                        + "........"
                        + "........"
                        + "........"
                        + "........"
                    );

                    String startPositionInput = "d5";
                    String destinationInput = "c6";

                    assertCannotMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
                }

                @DisplayName("도착위치에 적 기물이 존재하면, 이동할 수 있다.")
                @Test
                void canMoveWhenEnemyPieceExistsAtDestination() {
                    Board board = new Board(""
                        + "........"
                        + "........"
                        + "..p....."
                        + "...K...."
                        + "........"
                        + "........"
                        + "........"
                        + "........"
                    );

                    String startPositionInput = "d5";
                    String destinationInput = "c6";

                    assertCanMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
                }
            }
        }

        @DisplayName("Knight 이동")
        @Nested
        class Knight {

            @DisplayName("유효하지 않은 경로로 이동할 수 없다.")
            @ParameterizedTest
            @ValueSource(strings = {"b7", "d8", "f3"})
            void cannotMoveInvalidRoute(String destinationInput) {
                Board board = new Board(""
                    + "........"
                    + "........"
                    + "........"
                    + "...N...."
                    + "........"
                    + "........"
                    + "........"
                    + "........"
                );

                String startPositionInput = "d5";

                assertCannotMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
            }

            @DisplayName("왼쪽 왼쪽 위 방향으로 한 번 이동")
            @Test
            void moveLeftLeftUp() {
                Board board = new Board(""
                    + "........"
                    + "........"
                    + "........"
                    + "...N...."
                    + "........"
                    + "........"
                    + "........"
                    + "........"
                );

                String startPositionInput = "d5";
                String destinationInput = "b6";

                assertCanMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
            }

            @DisplayName("왼쪽 왼쪽 아래 방향으로 한 번 이동")
            @Test
            void moveLeftLeftDown() {
                Board board = new Board(""
                    + "........"
                    + "........"
                    + "........"
                    + "...N...."
                    + "........"
                    + "........"
                    + "........"
                    + "........"
                );

                String startPositionInput = "d5";
                String destinationInput = "c3";

                assertCanMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
            }

            @DisplayName("왼쪽 위 위 방향으로 한 번 이동")
            @Test
            void moveLeftUpUp() {
                Board board = new Board(""
                    + "........"
                    + "........"
                    + "........"
                    + "...N...."
                    + "........"
                    + "........"
                    + "........"
                    + "........"
                );

                String startPositionInput = "d5";
                String destinationInput = "c7";

                assertCanMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
            }

            @DisplayName("왼쪽 아래 아래 방향으로 한 번 이동")
            @Test
            void moveLeftDownDown() {
                Board board = new Board(""
                    + "........"
                    + "........"
                    + "........"
                    + "...N...."
                    + "........"
                    + "........"
                    + "........"
                    + "........"
                );

                String startPositionInput = "d5";
                String destinationInput = "c3";

                assertCanMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
            }

            @DisplayName("오른쪽 오른쪽 위 방향으로 한 번 이동")
            @Test
            void moveRightRightUp() {
                Board board = new Board(""
                    + "........"
                    + "........"
                    + "........"
                    + "...N...."
                    + "........"
                    + "........"
                    + "........"
                    + "........"
                );

                String startPositionInput = "d5";
                String destinationInput = "f6";

                assertCanMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
            }

            @DisplayName("오른쪽 오른쪽 아래 방향으로 한 번 이동")
            @Test
            void moveRightRightDown() {
                Board board = new Board(""
                    + "........"
                    + "........"
                    + "........"
                    + "...N...."
                    + "........"
                    + "........"
                    + "........"
                    + "........"
                );

                String startPositionInput = "d5";
                String destinationInput = "f4";

                assertCanMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
            }

            @DisplayName("오른쪽 위 위 방향으로 한 번 이동")
            @Test
            void moveRightUpUp() {
                Board board = new Board(""
                    + "........"
                    + "........"
                    + "........"
                    + "...N...."
                    + "........"
                    + "........"
                    + "........"
                    + "........"
                );

                String startPositionInput = "d5";
                String destinationInput = "e7";

                assertCanMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
            }

            @DisplayName("오른쪽 아래 아래 방향으로 한 번 이동")
            @Test
            void moveRightDownDown() {
                Board board = new Board(""
                    + "........"
                    + "........"
                    + "........"
                    + "...N...."
                    + "........"
                    + "........"
                    + "........"
                    + "........"
                );

                String startPositionInput = "d5";
                String destinationInput = "e3";

                assertCanMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
            }

            @DisplayName("도착위치에 아군 기물이 존재하면, 이동할 수 없다.")
            @Test
            void cannotMoveWhenMyPieceExistsAtDestination() {
                Board board = new Board(""
                    + "........"
                    + "..P....."
                    + "........"
                    + "...N...."
                    + "........"
                    + "........"
                    + "........"
                    + "........"
                );

                String startPositionInput = "d5";
                String destinationInput = "c7";

                assertCannotMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
            }

            @DisplayName("이동 경로 중간에 적 기물이 존재해도, 이동할 수 있다.")
            @Test
            void canMoveWhenEnemyPieceExistsOnRoute() {
                Board board = new Board(""
                    + "........"
                    + "........"
                    + "...p...."
                    + "...N...."
                    + "........"
                    + "........"
                    + "........"
                    + "........"
                );

                String startPositionInput = "d5";
                String destinationInput = "c7";

                assertCanMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
            }

            @DisplayName("도착위치에 적 기물이 존재하면, 이동할 수 있다.")
            @Test
            void canMoveWhenEnemyPieceExistsAtDestination() {
                Board board = new Board(""
                    + "........"
                    + "..p....."
                    + "........"
                    + "...N...."
                    + "........"
                    + "........"
                    + "........"
                    + "........"
                );

                String startPositionInput = "d5";
                String destinationInput = "c7";

                assertCanMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
            }
        }

        @DisplayName("Pawn 이동")
        @Nested
        class Pawn {
            @DisplayName("흑 팀인 경우")
            @Nested
            class BlackTeam {

                @DisplayName("유효하지 않은 경로로 이동할 수 없다.")
                @ParameterizedTest
                @ValueSource(strings = {"d2", "e3", "a5"})
                void cannotMoveInvalidRoute(String destinationInput) {
                    Board board = new Board(""
                        + "........"
                        + "........"
                        + "........"
                        + "...P...."
                        + "........"
                        + "........"
                        + "........"
                        + "........"
                    );

                    String startPositionInput = "d5";

                    assertCannotMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
                }

                @DisplayName("한 칸 전진")
                @Nested
                class MoveForwardOneCell {
                    @DisplayName("아래 방향으로 한 칸 이동")
                    @Test
                    void moveForwardOneCell() {
                        Board board = new Board(""
                            + "........"
                            + "........"
                            + "........"
                            + "...P...."
                            + "........"
                            + "........"
                            + "........"
                            + "........"
                        );

                        String startPositionInput = "d5";
                        String destinationInput = "d4";

                        assertCanMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
                    }

                    @DisplayName("도착위치에 기물이 존재하면, 이동할 수 없다.")
                    @Test
                    void cannotMoveWhenPieceExistsAtDestination() {
                        Board board = new Board(""
                            + "........"
                            + "........"
                            + "........"
                            + "...P...."
                            + "...b...."
                            + "........"
                            + "........"
                            + "........"
                        );

                        String startPositionInput = "d5";
                        String destinationInput = "d4";

                        assertCannotMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
                    }

                    @DisplayName("위 방향으로 이동할 수 없다.")
                    @Test
                    void cannotMoveBackwardOneCell() {
                        Board board = new Board(""
                            + "........"
                            + "........"
                            + "........"
                            + "...P...."
                            + "........"
                            + "........"
                            + "........"
                            + "........"
                        );

                        String startPositionInput = "d5";
                        String destinationInput = "d6";

                        assertCannotMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
                    }

                    @DisplayName("처음 위치가 아닌 곳에서 앞으로 두 칸 전진할 수 없다.")
                    @Test
                    void cannotMoveForwardTwoCellWhenNotAtFirstPosition() {
                        Board board = new Board(""
                            + "........"
                            + "........"
                            + "........"
                            + "...P...."
                            + "........"
                            + "........"
                            + "........"
                            + "........"
                        );

                        String startPositionInput = "d5";
                        String destinationInput = "d3";

                        assertCannotMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
                    }
                }

                @DisplayName("두 칸 전진")
                @Nested
                class MoveForwardTwoCells {
                    @DisplayName("처음 위치에 있을 때, 앞으로 두 칸 전진 이동")
                    @Test
                    void moveForwardTwoCellWhenAtFirstPosition() {
                        Board board = new Board(""
                            + "........"
                            + "...P...."
                            + "........"
                            + "........"
                            + "........"
                            + "........"
                            + "........"
                            + "........"
                        );

                        String startPositionInput = "d7";
                        String destinationInput = "d5";

                        assertCanMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
                    }

                    @DisplayName("도착위치에 기물이 존재하면, 이동할 수 없다.")
                    @Test
                    void cannotMoveWhenPieceExistsAtDestination() {
                        Board board = new Board(""
                            + "........"
                            + "...P...."
                            + "........"
                            + "...p...."
                            + "........"
                            + "........"
                            + "........"
                            + "........"
                        );

                        String startPositionInput = "d7";
                        String destinationInput = "d5";

                        assertCannotMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
                    }

                    @DisplayName("이동 경로 중간에 기물이 존재하면, 이동할 수 없다.")
                    @Test
                    void cannotMoveWhenPieceExistsOnRoute() {
                        Board board = new Board(""
                            + "........"
                            + "...P...."
                            + "...p...."
                            + "........"
                            + "........"
                            + "........"
                            + "........"
                            + "........"
                        );

                        String startPositionInput = "d7";
                        String destinationInput = "d5";

                        assertCannotMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
                    }

                    @DisplayName("위 방향으로 이동할 수 없다.")
                    @Test
                    void moveBackwardTwoCell() {
                        Board board = new Board(""
                            + "........"
                            + "........"
                            + "........"
                            + "...P...."
                            + "........"
                            + "........"
                            + "........"
                            + "........"
                        );

                        String startPositionInput = "d5";
                        String destinationInput = "d7";

                        assertCannotMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
                    }
                }

                @DisplayName("대각선 한 칸 이동")
                @Nested
                class MoveDiagonalOneCell {
                    @DisplayName("적이 왼쪽 대각선에 있을 때, 이동 가능")
                    @Test
                    void moveDiagonalLeftWhenEnemyPieceExists() {
                        Board board = new Board(""
                            + "........"
                            + "...P...."
                            + "..b....."
                            + "........"
                            + "........"
                            + "........"
                            + "........"
                            + "........"
                        );

                        String startPositionInput = "d7";
                        String destinationInput = "c6";

                        assertCanMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
                    }

                    @DisplayName("적이 오른쪽 대각선에 있을 때, 이동 가능")
                    @Test
                    void moveDiagonalRightWhenEnemyPieceExists() {
                        Board board = new Board(""
                            + "........"
                            + "...P...."
                            + "....b..."
                            + "........"
                            + "........"
                            + "........"
                            + "........"
                            + "........"
                        );

                        String startPositionInput = "d7";
                        String destinationInput = "e6";

                        assertCanMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
                    }

                    @DisplayName("도착위치에 적이 존재하지 않을 때, 왼쪽 대각선 이동 불가능")
                    @Test
                    void cannotMoveDiagonalLeftWhenEnemyPieceNotExistsAtDestination() {
                        Board board = new Board(""
                            + "........"
                            + "...P...."
                            + "........"
                            + "........"
                            + "........"
                            + "........"
                            + "........"
                            + "........"
                        );

                        String startPositionInput = "d7";
                        String destinationInput = "c6";

                        assertCannotMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
                    }

                    @DisplayName("도착위치에 적이 존재하지 않을 때, 오른쪽 대각선 이동 불가능")
                    @Test
                    void cannotMoveDiagonalRightWhenEnemyPieceNotExistsAtDestination() {
                        Board board = new Board(""
                            + "........"
                            + "...P...."
                            + "........"
                            + "........"
                            + "........"
                            + "........"
                            + "........"
                            + "........"
                        );

                        String startPositionInput = "d7";
                        String destinationInput = "e6";

                        assertCannotMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
                    }
                }
            }
//
//            @DisplayName("백 팀인 경우")
//            @Nested
//            class WhiteTeam {
//
//                @DisplayName("유효하지 않은 경로로 이동할 수 없다.")
//                @ParameterizedTest
//                @ValueSource(strings = {"d8", "e7", "a8"})
//                void cannotMoveInvalidRoute(String destinationInput) {
//                    BoardSetting customBoardSetting = new BoardCustomSetting(
//                        Arrays.asList(
//                            null, null, null, null, null, null, null, null,
//                            null, null, null, null, null, null, null, null,
//                            null, null, null, null, null, null, null, null,
//                            null, null, null, W_PN, null, null, null, null,
//                            null, null, null, null, null, null, null, null,
//                            null, null, null, null, null, null, null, null,
//                            null, null, null, null, null, null, null, null,
//                            null, null, null, null, null, null, null, null)
//                    );
//
//                    
//
//                    String startPositionInput = "d5";
//
//                    assertCannotMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
//                }
//
//                @DisplayName("한 칸 전진")
//                @Nested
//                class MoveForwardOneCell {
//                    @DisplayName("위 방향으로 한 칸 이동")
//                    @Test
//                    void moveForwardOneCell() {
//                        BoardSetting customBoardSetting = new BoardCustomSetting(
//                            Arrays.asList(
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, W_PN, null, null, null, null,
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, null, null, null, null, null)
//                        );
//
//                        
//
//                        String startPositionInput = "d5";
//                        String destinationInput = "d6";
//
//                        assertCanMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
//                    }
//
//                    @DisplayName("도착위치에 기물이 존재하면, 이동할 수 없다.")
//                    @Test
//                    void cannotMoveWhenPieceExistsAtDestination() {
//                        BoardSetting customBoardSetting = new BoardCustomSetting(
//                            Arrays.asList(
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, B_BP, null, null, null, null,
//                                null, null, null, W_PN, null, null, null, null,
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, null, null, null, null, null)
//                        );
//
//                        
//
//                        String startPositionInput = "d5";
//                        String destinationInput = "d6";
//
//                        assertCannotMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
//                    }
//
//                    @DisplayName("아래 방향으로 이동할 수 없다.")
//                    @Test
//                    void cannotMoveBackwardOneCell() {
//                        BoardSetting customBoardSetting = new BoardCustomSetting(
//                            Arrays.asList(
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, W_PN, null, null, null, null,
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, null, null, null, null, null)
//                        );
//
//                        
//
//                        String startPositionInput = "d5";
//                        String destinationInput = "d4";
//
//                        assertCannotMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
//                    }
//
//                    @DisplayName("처음 위치가 아닌 곳에서 앞으로 두 칸 전진할 수 없다.")
//                    @Test
//                    void cannotMoveForwardTwoCellWhenNotAtFirstPosition() {
//                        BoardSetting customBoardSetting = new BoardCustomSetting(
//                            Arrays.asList(
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, W_PN, null, null, null, null,
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, null, null, null, null, null)
//                        );
//
//                        
//
//                        String startPositionInput = "d5";
//                        String destinationInput = "d7";
//
//                        assertCannotMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
//                    }
//                }
//
//                @DisplayName("두 칸 전진")
//                @Nested
//                class MoveForwardTwoCells {
//                    @DisplayName("처음 위치에 있을 때, 앞으로 두 칸 전진 이동")
//                    @Test
//                    void moveForwardTwoCellWhenAtFirstPosition() {
//                        BoardSetting customBoardSetting = new BoardCustomSetting(
//                            Arrays.asList(
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, W_PN, null, null, null, null,
//                                null, null, null, null, null, null, null, null)
//                        );
//
//                        
//
//                        String startPositionInput = "d2";
//                        String destinationInput = "d4";
//
//                        assertCanMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
//                    }
//
//                    @DisplayName("도착위치에 기물이 존재하면, 이동할 수 없다.")
//                    @Test
//                    void cannotMoveWhenPieceExistsAtDestination() {
//                        BoardSetting customBoardSetting = new BoardCustomSetting(
//                            Arrays.asList(
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, B_PN, null, null, null, null,
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, W_PN, null, null, null, null,
//                                null, null, null, null, null, null, null, null)
//                        );
//
//                        
//
//                        String startPositionInput = "d2";
//                        String destinationInput = "d4";
//
//                        assertCannotMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
//                    }
//
//                    @DisplayName("이동 경로 중간에 기물이 존재하면, 이동할 수 없다.")
//                    @Test
//                    void cannotMoveWhenPieceExistsOnRoute() {
//                        BoardSetting customBoardSetting = new BoardCustomSetting(
//                            Arrays.asList(
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, B_PN, null, null, null, null,
//                                null, null, null, B_PN, null, null, null, null,
//                                null, null, null, W_PN, null, null, null, null,
//                                null, null, null, null, null, null, null, null)
//                        );
//
//                        
//
//                        String startPositionInput = "d2";
//                        String destinationInput = "d4";
//
//                        assertCannotMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
//                    }
//
//                    @DisplayName("아래 방향으로 이동할 수 없다.")
//                    @Test
//                    void moveBackwardTwoCell() {
//                        BoardSetting customBoardSetting = new BoardCustomSetting(
//                            Arrays.asList(
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, W_PN, null, null, null, null,
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, null, null, null, null, null)
//                        );
//
//                        
//
//                        String startPositionInput = "d3";
//                        String destinationInput = "d1";
//
//                        assertCannotMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
//                    }
//                }
//
//                @DisplayName("대각선 한 칸 이동")
//                @Nested
//                class MoveDiagonalOneCell {
//                    @DisplayName("적이 왼쪽 대각선에 있을 때, 이동 가능")
//                    @Test
//                    void moveDiagonalLeftWhenEnemyPieceExists() {
//                        BoardSetting customBoardSetting = new BoardCustomSetting(
//                            Arrays.asList(
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, null, null, null, null, null,
//                                null, null, B_PN, null, null, null, null, null,
//                                null, null, null, W_PN, null, null, null, null,
//                                null, null, null, null, null, null, null, null)
//                        );
//
//                        
//
//                        String startPositionInput = "d2";
//                        String destinationInput = "c3";
//
//                        assertCanMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
//                    }
//
//                    @DisplayName("적이 오른쪽 대각선에 있을 때, 이동 가능")
//                    @Test
//                    void moveDiagonalRightWhenEnemyPieceExists() {
//                        BoardSetting customBoardSetting = new BoardCustomSetting(
//                            Arrays.asList(
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, null, B_PN, null, null, null,
//                                null, null, null, W_PN, null, null, null, null,
//                                null, null, null, null, null, null, null, null)
//                        );
//
//                        
//
//                        String startPositionInput = "d2";
//                        String destinationInput = "e3";
//
//                        assertCanMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
//                    }
//
//                    @DisplayName("도착위치에 적이 존재하지 않을 때, 왼쪽 대각선 이동 불가능")
//                    @Test
//                    void cannotMoveDiagonalLeftWhenEnemyPieceNotExistsAtDestination()
//                        {
//                        BoardSetting customBoardSetting = new BoardCustomSetting(
//                            Arrays.asList(
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, W_PN, null, null, null, null,
//                                null, null, null, null, null, null, null, null)
//                        );
//
//                        
//
//                        String startPositionInput = "d2";
//                        String destinationInput = "c3";
//
//                        assertCannotMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
//                    }
//
//                    @DisplayName("도착위치에 적이 존재하지 않을 때, 오른쪽 대각선 이동 불가능")
//                    @Test
//                    void cannotMoveDiagonalRightWhenEnemyPieceNotExistsAtDestination()
//                        {
//                        BoardSetting customBoardSetting = new BoardCustomSetting(
//                            Arrays.asList(
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, null, null, null, null, null,
//                                null, null, null, W_PN, null, null, null, null,
//                                null, null, null, null, null, null, null, null)
//                        );
//
//                        
//
//                        String startPositionInput = "d2";
//                        String destinationInput = "e3";
//
//                        assertCannotMove(board, startPositionInput, destinationInput, TeamColor.BLACK);
//                    }
//                }
//            }
        }
    }

    private void assertCanMove(Board board, String startPosition, String destination, TeamColor pieceColor) {
        String boardStatusBeforeMove = board.getBoardStatus();
        int startPositionBoardStatusIndex = PositionConverter.convertToBoardStatusIndex(startPosition);
        String pieceValueToMove = String.valueOf(boardStatusBeforeMove.charAt(startPositionBoardStatusIndex));
        MoveRequest moveRequest = new MoveRequest(pieceColor, Position.of(startPosition), Position.of(destination));
        assertThatCode(() -> board.movePiece(moveRequest))
            .doesNotThrowAnyException();
        String boardStatusAfterMove = board.getBoardStatus();
        assertThat(boardStatusAfterMove).isNotEqualTo(boardStatusBeforeMove);
        assertThat(String.valueOf(boardStatusAfterMove.charAt(startPositionBoardStatusIndex))).isEqualTo(".");
        int destinationBoardStatusIndex = PositionConverter.convertToBoardStatusIndex(destination);
        assertThat(String.valueOf(boardStatusAfterMove.charAt(destinationBoardStatusIndex))).isEqualTo(pieceValueToMove);
    }

    private void assertCannotMove(Board board, String startPosition, String destination, TeamColor pieceColor) {
        String boardStatusBeforeMove = board.getBoardStatus();
        MoveRequest moveRequest = new MoveRequest(pieceColor, Position.of(startPosition), Position.of(destination));
        assertThatCode(() -> board.movePiece(moveRequest))
            .isInstanceOf(IllegalArgumentException.class);
        assertThat(board.getBoardStatus()).isEqualTo(boardStatusBeforeMove);
    }
}
