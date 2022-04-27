

const clickStartButton = () => {
    let roomName = prompt("방 이름");
    //TODO 여기 값 입력 안하면 안넘어가도록 수정
    if (roomName === null) {
        return;
    }
    const password = prompt("비밀번호");
    if (password === null ) {
        return;
    }
    const roomInfo = {roomName: roomName, password: password};
    const response = fetch(`/new`, {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(roomInfo),
    });
    response.then(() => getGames());
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
