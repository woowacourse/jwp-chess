function loadChessGamePage(uri, password) {
    location.href = "/play?location=" + uri + "&password=" + password;
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
        .then(response => loadChessGamePage(response.headers.get('Location'), password))
        .catch(error => {
            alert(error.message);
        });
}

async function deleteChessGame(id, password){
    fetch('/chessgames/' + id + "?password=" + password, {
        method: 'DELETE',
    }).then(response => handlingException(response))
        .catch(function (error) {
            alert(error.message)
        })
}

async function openLoadGameWindowPop(url, title) {
    let options = "top=10, left=10, width=500, height=600, status=no, menubar=no, toolbar=no, resizable=no";
    let win = window.open(url, title, options);

    win.loadGame = function (id) {
        let password = win.prompt("비밀번호를 입력 해주세요.");
        if (password != null) {
            loadChessGamePage("/chessgames/" + id, password);
            win.close();
        }
    }

    win.deleteGame = function (id) {
        let password = win.prompt("비밀번호를 입력 해주세요.");
        if (password != null) {
            deleteChessGame(id ,password);
            win.close();
        }
    }
}

async function findAllChessGames() {
    await openLoadGameWindowPop('/games', '게임 목록 불러오기')
}
