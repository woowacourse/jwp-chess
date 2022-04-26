function loadChessGamePage(uri) {
    location.href = "/play?location=" + uri;
}

async function startChessGame() {
    const title = document.getElementById("title").value;
    const password = document.getElementById("password").value;

    const game = {
        title: title,
        password: password
    }
    await fetch("/chessgames", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify(game)
    }).then(response => handlingException(response))
        .then(response => loadChessGamePage(response.headers.get('Location')))
        .catch(error => {
            alert(error.message);
        });
}

async function handlingException(response) {
    if (response.ok) {
        return response;
    }
    const error = await response.json();
    throw Error(error.message);
}
