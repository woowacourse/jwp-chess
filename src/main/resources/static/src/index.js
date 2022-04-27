function deleteGame(gameId) {
    const password = prompt('비밀번호를 입력하세요.', '');

    fetch(`/delete`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            gameId: gameId,
            password: password
        })
    }).then((response) => {
        if (response.status === 400) {
            throw response;
            return;
        }
        if (response.status === 200) {
            alert("성공적으로 삭제하였습니다.");
            location.reload();
        }
    }).catch(err => {
        err.text().then(msg => {
            alert(msg);
        })
    });
}

