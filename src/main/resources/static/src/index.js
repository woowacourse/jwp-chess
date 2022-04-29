let source = '';
let target = '';

function join(id) {
    location.replace(`/board?id=${id}`);
}

function create() {
    let title = document.getElementById('titleId').value;
    let password = document.getElementById('passwordId').value;

    fetch("/create", {
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
        location.reload();
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
    const params = new URLSearchParams(location.search);
    const boardId = params.get('id');
    fetch(`/board/move?id=${boardId}`, {
        method: "POST",
        headers: {
            "Content-Type": "text/plain"
        },
        body: source + " " + target
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
    const params = new URLSearchParams(location.search);
    const boardId = params.get('id');
    fetch(`/board/end?id=${boardId}`, {
        method: "POST"
    }).then(async res => {
        if (!res.ok) {
            throw new Error(await res.text())
        }
        location.replace(`/board/chess-result?id=${boardId}`);
    }).catch(error => alert(error.message));
}

function restart() {
    const params = new URLSearchParams(location.search);
    const boardId = params.get('id');
    fetch(`/board/restart?id=${boardId}`, {
        method: "POST"
    }).then(async res => {
        if (!res.ok) {
            throw new Error(await res.text());
        }
        location.replace(`/board?id=${boardId}`);
    }).catch(error => alert(error.message));
}

function showStatus() {
    const params = new URLSearchParams(location.search);
    const boardId = params.get('id');
    location.replace(`/board/chess-status?id=${boardId}`);
}

function showBoard() {
    const params = new URLSearchParams(location.search);
    const boardId = params.get('id');
    location.replace(`/board?id=${boardId}`);
}

function deleteBoard(id) {
    const inputPassword = prompt('비밀번호를 입력하세요');
    fetch(`/board/delete?id=${id}`, {
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

