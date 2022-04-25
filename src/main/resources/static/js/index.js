$(document).ready(function () {
  triggerEvents();
});

function triggerEvents() {
  $("button#move").click(function () {
    movePiece();
  });

  $("button#save-game").click(function () {
    saveGame();
  });
}
