let source = "";
let target = "";
let gameId = "";

function clicked(id, fb) {
    gameId = fb;
    setVariable(id);
    checkSendToServer();
}

function setVariable(id) {
    if (source !== "") {
        target = id;
    } else {
        source = id;
    }
}

function checkSendToServer() {
    if (source !== "" && target !== "") {
        sendToServer(source, target);
        source = "";
        target = "";
    }
}

function sendToServer() {
    fetch(gameId+'/move', {
        method: "PATCH",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            source: source,
            target: target
        })
    }).then((response) =>{
        if (response.status === 400 || response.status === 500) {
            throw response;
        }

        response.json().then(data => {
            if(data.isGameOver === true) {
                alert("게임이 종료되었습니다.")
                document.location.href = gameId+'/result'
                return;
            };

            location.reload();
        })
    }).catch(err => {
        err.text().then(msg => {
            alert(msg);
        })
    });
}
