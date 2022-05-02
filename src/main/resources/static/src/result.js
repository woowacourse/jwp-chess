function restartGame(gameId) {
    fetch(`/game/` + gameId, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            id: gameId,
        })
    }).then((response) => {
        if (response.status !== 204) {
            throw response;
        }
        location.href = '/game/' + gameId
    }).catch(err => {
        err.text().then(msg => {
            alert(msg);
        })
    });
}
