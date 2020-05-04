function on_games() {
  fetch("http://localhost:4567/room/games").then(response => response.json()).then(playersDto => {
    console.log(playersDto.player1Name);
    console.log(playersDto);
    const point = document.querySelector(".rooms")
    console.log(point);

    let result = "";
    for (let i = 0; i < playersDto.length; i++) {
      let id = playersDto[i].id
      console.log(playersDto[i].name)
      result += `<li onclick="location.href = 'http://localhost:4567/room' + id;"> ${playersDto[i].name} </li>`;
    }
    point.innerHTML = result;
  })
}
