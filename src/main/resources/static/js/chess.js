function on_games() {
    fetch("http://localhost:4567/games").then(response => response.json()).then(playersDto => {
        console.log(playersDto);
    })
}
