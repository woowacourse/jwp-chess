function gameTemplate(id, gameName) {
    return `<tr>
        <th>${id}번방 :</th>
        <th>${gameName}</th>
        <button data-id=${id} class="enter-btn">ENTER</button>
        <button data-id=${id} class="delete-btn">DELETE</button>
        <br>
    </tr>`;
}

window.onload = async function () {
    const createBtn = document.getElementById("create-btn");
    createBtn.addEventListener('click', createGame);

    const gameList = await fetch(
        `/games/`
    ).then(res => {
            if (res.status === 200) {
                return res.json()
            } else {
                res.text()
                    .then(text => alert(text))
            }
        }
    );
    const gameObj = {}
    gameList.forEach(game => gameObj[game.id] = game);
    window.gameObj = gameObj;
    renderGameList();
}

function renderGameList() {
    const gameTable = document.getElementById("gameList");
    gameTable.innerHTML = Object.values(window.gameObj).reduce((acc, game) => {
        const {id, gameName} = game;
        acc += gameTemplate(id, gameName);
        return acc;
    }, '');

    const enterBtn = document.getElementsByClassName("enter-btn");
    Array.from(enterBtn).forEach((el) => {
        el.addEventListener('click', enterGame);
    })

    const deleteBtn = document.getElementsByClassName("delete-btn");
    Array.from(deleteBtn).forEach((el) => {
        el.addEventListener('click', deleteGame);
    })
}

async function createGame() {
    const gameName = document.getElementById('gameName').value;
    await fetch(
        `/games/`,
        {
            method: 'POST',
            body: JSON.stringify({
                'gameName': gameName
            }),
            headers: {
                'Content-type': 'application/json; charset=UTF-8',
                'Accept': 'application/json'
            }
        }
    ).then(res => {
        if (res.status === 201) {
            window.location.href = res.headers.get('location')
        } else {
            res.text()
                .then(text => alert(text))
        }
        gameName.value = "";
    })
}

async function enterGame(event) {
    const gameId = event.target.dataset.id;
    await fetch(
        `/games/${gameId}`,
    ).then(response => {
        if (response.status === 200) {
            location.href = `games/${gameId}`
        } else {
            alert('에러 발생')
        }
    });
}

async function deleteGame(event) {
    const gameId = event.target.dataset.id;
    await fetch(
        `/games/${gameId}`,
        {
            method: 'DELETE'
        }
    ).then(response => {
        if (response.status === 204) {
            delete window.gameObj[gameId];
            renderGameList();
            alert('삭제 완료')
        } else {
            alert('에러 발생')
        }
    });
}