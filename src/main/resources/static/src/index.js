let id;

async function start() {
    console.log("start")
    const response = await fetch(`/game/start`, {
        headers: {'Content-Type': 'application/json'},
        method: "post",
        body: JSON.stringify({
            gameId: gameId
        })
    }).then((res) => {
        gameId = res.text();
    })
    console.log("response go")
    // gameId = response.text();
    // console.log("gameId go" + gameId)

    // window.location.replace(`/game/${gameId}`);
}
