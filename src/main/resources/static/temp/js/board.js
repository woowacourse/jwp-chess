
$board = document.querySelector('.board');

drawBoard();

function drawBoard() {
  this.$board.innerHTML = '';
  for (let i = 7; i >= 0; i--) {
    for (let j = 0; j < 8; j++) {
      const boardElement = document.createElement('div');
      const position = `${j}${i}`;
      boardElement.id = position;
      boardElement.className = 'board-item';
      let color = '';
      if ((j - i) % 2 === 0) {
        color = 'white';
      } else {
        color = 'black';
      }
      boardElement.classList.add(color)
      this.$board.appendChild(boardElement);
    }
  }
}
