function deleteGame(gameId) {
    const inputPassword = prompt("패스워드를 입력해주세요");
    const deletePassword = {
        password: inputPassword
    }
    console.log(deletePassword)
    fetch("/chessgames/" + gameId, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify(deletePassword)
    }).then(response => handlingException(response))
        .then(() => window.location.reload())
        .catch(error => {
            console.log(error)
            alert(error.message);
        });
}

function loadGame(gameId){
    window.location.href = "/chessgames/" + gameId
}

async function startNewGame() {
    const inputTitle = prompt("체스방 이름을 입력해주세요");
    const inputPassword = prompt("체스방 비밀번호를 입력해주세요");
    const request = {
        title : inputTitle,
        password : inputPassword
    }
    await fetch("/chessgames", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify(request)
    }).then(response => handlingException(response))
        .then(function (response) {
            window.location.href = response.headers.get('Location');
        })
        .catch(error => {
            alert(error.message);
        });
}

async function handlingException(response) {
    console.log(response)
    if (response.ok) {
        return response;
    }
    if (response.status === 404) {
        throw Error("게임이 존재하지 않아 찾을 수 없습니다.");
    }
    const error = await response.json();
    throw Error(error.message);
}
