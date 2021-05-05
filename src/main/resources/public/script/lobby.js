window.onload = function () {
    const createBtn = document.getElementById("create-btn");
    createBtn.addEventListener('click', createGame);

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
    console.log(gameName)
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
        if (res.status === 200) {
            res.json()
                .then(id => window.location.href = `/games/${id}`)
        } else {
            res.text()
                .then(text => alert(text))
        }
        gameName.value = "";
    })
}

async function enterGame(event) {
    const gameId = event.target.id;
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
    const gameId = event.target.id;
    await fetch(
        `/games/${gameId}`,
        {
            method: 'DELETE'
        }
    ).then(response => {
        if (response.status === 200) {
            alert('삭제 완료')
            location.href = '/'
        } else {
            alert('에러 발생')
        }
    });
}