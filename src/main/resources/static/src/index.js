let source = '';
let target = '';

function join(id) {
    location.replace(`/boards/${id}`);
}

function create() {
    let title = document.getElementById('titleId').value;
    let password = document.getElementById('passwordId').value;

    fetch("/", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            title: title,
            password: password
        })
    }).then(async (res) => {
        if (!res.ok) {
            throw new Error(await res.text());
        }
        location.replace(res.url);
    }).catch(error =>
        alert(error.message));
}


function move(id) {
    if (source === '') {
        let elementById = document.getElementById(id);
        elementById.style.backgroundColor = "#FF0000";
        source = id;
        return;
    }

    if (target === '') {
        let elementById = document.getElementById(id);
        elementById.style.backgroundColor = "#FF0000";
        target = id;
        movePiece();
    }
}

function movePiece() {
    let url = window.location.pathname;
    const boardId = url.substring(url.lastIndexOf('/') + 1);
    fetch(`/boards/${boardId}/move`, {
        method: "PATCH",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            source: source,
            target: target
        })
    }).then(async (res) => {
        document.getElementById(source).style.backgroundColor = '';
        document.getElementById(target).style.backgroundColor = '';
        source = '';
        target = '';
        const body = await res.text();
        if (!res.ok) {
            throw new Error(body);
        }
        if (body === 'true') {
            alert("킹이 잡혀 게임이 종료 되었습니다!")
            end();
        } else if (body === 'false') {
            location.reload();
        }
    }).catch(error => alert(error.message));
}

function end() {
    let url = window.location.pathname;
    const boardId = url.substring(url.lastIndexOf('/') + 1);
    fetch(`/boards/${boardId}/end`, {
        method: "POST"
    }).then(async res => {
        if (!res.ok) {
            throw new Error(await res.text())
        }
        location.replace(`/boards/${boardId}/chess-result`);
    }).catch(error => alert(error.message));
}

function restart() {
    let url = window.location.pathname;
    const boardId = url.substring(url.lastIndexOf('/') + 1);
    fetch(`/boards/${boardId}/restart`, {
        method: "POST"
    }).then(async res => {
        if (!res.ok) {
            throw new Error(await res.text());
        }
        location.replace(`/boards/${boardId}`);
    }).catch(error => alert(error.message));
}

function showStatus() {
    let url = window.location.pathname;
    const boardId = url.substring(url.lastIndexOf('/') + 1);
    location.replace(`/boards/${boardId}/chess-status`);
}

function showBoard() {
    let url = window.location.pathname;
    const boardId = url.substring(url.lastIndexOf('/') + 1);
    location.replace(`/boards/${boardId}`);
}

function deleteBoard(id) {
    const inputPassword = prompt('비밀번호를 입력하세요');
    if (inputPassword == null) {
        return;
    }
    fetch(`/boards/${id}`, {
        method: "DELETE",
        headers: {
            "Content-Type": "text/plain"
        },
        body: inputPassword
    }).then(async res => {
        if (!res.ok) {
            throw new Error(await res.text());
        }
        location.reload();
    }).catch(error => alert(error.message));
}

