const chessGameFunction = {
    init: function () {
        const _this = this;
        document.querySelector(".new-game-btn").addEventListener("click", event => {
            const title = prompt("방제는 뭘로 하시겠어요?");
            _this.create(title);
        });
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
                // enrollChessGameId(chessGameDto.chessGameId);
                // placePieces(chessGameDto.pieceDtos);
                // toggleStartAndEndButtons(chessGameDto.finished);
                // changeTurn(chessGameDto.state);
                const chessGameId = chessGameDto.chessGameId.toString();
                window.location = `/`;
                // window.location = `/chessgames/${chessGameId}`;
            })
            .catch(error => {
                alert("잘못된 명령입니다!");
            });
    }
}

chessGameFunction.init();
