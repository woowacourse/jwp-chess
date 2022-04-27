

const clickStartButton = () => {
    let roomName = prompt("방 이름");
    if (roomName === undefined) {
        return;
    }
    let password = prompt("비밀번호");
    if (password === undefined ) {
        return;
    }
    console.log(roomName, password);
}

const getGames = () => {
    const games = document.getElementById("existed-game-info")

    const response = fetch(`/games`, {
        method: "GET",
        headers: {"Content-Type": "application/json"}
    });
    games.innerHTML = null;

    response
        .then(data => data.json())
        .then(body => {
            if (body.gameInfos.size === 0) {
                games.innerHTML = `<div> 존재하는 방이 없습니다. </div>`
            }
            body.gameInfos.map(game => {
                console.log(game)
                games.innerHTML +=
                            `<div>
                                <div onclick="continueGame(${game.gameId})"> ${game.name} </div>
                                <button onclick="deleteGame(${game.gameId})"> 삭제하기 </button>    
                            </div>`
            })
        });
}

const continueGame = (id) => {
    console.log("겜 시작 전");
    window.location.href = `${id}`;
    console.log("겜 시작 후");
}

const deleteGame = (id) => {
    const response = fetch(`/game/${id}`, {
        method: "DELETE",
        headers: {"Content-Type": "application/json"}
    });

    response.then(() => getGames());
}
