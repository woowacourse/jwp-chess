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
    const url = "/chessgames/" + gameId;
    window.location.href = url
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
