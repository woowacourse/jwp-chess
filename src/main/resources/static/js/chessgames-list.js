const chessGameFunction = {
    init: function () {
        const _this = this;
        document.querySelector(".new-game-btn").addEventListener("click", event => {
            const title = prompt("방제는 뭘로 하시겠어요?");
            _this.create(title);
        });

        document.querySelector(".chess-game-list-table").addEventListener("click", event => {
            const classList = event.target.classList;
            if (!classList.contains("chess-game-id") && !classList.contains("chess-game-title")) {
                return
            }
            const chessGameId = parseInt(event.target.parentElement.firstElementChild.innerText);
            if (confirm(`${chessGameId}번방에 입장하시겠습니까?`)) {
                window.location = `/chessgames/${chessGameId}`;
            }
        })
    },
    create: function (title) {
        const data = {
            title: title
        };
        const option = {
            method: "POST",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        };

        fetch("/api/chessgames", option)
            .then(data => {
                return data.json();
            })
            .then(chessGameDto => {
                const chessGameId = chessGameDto.chessGameId.toString();
                window.location = `/chessgames/${chessGameId}`;
            })
            .catch(error => {
                alert("잘못된 명령입니다!");
            });
    }
}

chessGameFunction.init();
