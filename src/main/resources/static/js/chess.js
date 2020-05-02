function on_games() {
  fetch("http://localhost:4567/room/games").then(response => response.json()).then(playersDto => {
    console.log(playersDto.player1Name);
    console.log(playersDto);
  })
}
