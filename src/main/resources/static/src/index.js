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

async function openLoadGameWindowPop(url, title) {
    let options = "top=10, left=10, width=500, height=600, status=no, menubar=no, toolbar=no, resizable=no";
    let win = window.open(url, title, options);

    win.loadGame = function (id) {
        win.close();
        loadChessGamePage("/chessgames/" + id);
    }

    win.deleteGame = function (id) {
        win.close();
        fetch('/game/' + id, {
            method: 'DELETE',
        }).then(handleErrors)
            .catch(function (error) {
                alert(error.message)
            })
    }
}

async function findAllChessGames() {
    await openLoadGameWindowPop('/games', '게임 목록 불러오기')
}
