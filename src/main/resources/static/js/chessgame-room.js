const index = {
    init: function () {
        setTimeout(() => {
            loadPage();
        }, 500);

        const _this = this;
        document.querySelector(".continue").addEventListener("click", event => {
            if (event.target.classList.contains("hidden")) {
                return;
            }
            _this.continue();
        });

        document.querySelector(".start").addEventListener("click", event => {
            if (event.target.classList.contains("hidden")) {
                return;
            }
            _this.start();
        });

        document.querySelector(".exit").addEventListener("click", event => {
            if (event.target.classList.contains("hidden")) {
                return;
            }

            if (confirm("게임에서 나가시겠습니까?")) {
                window.location = "/";
            }
        });

        document.querySelector(".chess-end-btn").addEventListener("click", event => {
            _this.end();
        });

        document.querySelector(".chess-status-btn").addEventListener("click", event => {
            _this.scores();
        });

        document.querySelector(".chess-board").addEventListener("click", event => {
            const fromInput = document.querySelector(".from");
            const toInput = document.querySelector(".to");

            const clickedPosition = decideClickedPosition(event.target);
            if (fromInput.value === "") {
                if (event.target.tagName !== "IMG" && event.target.firstChild.tagName !== "IMG") {
                    alert("선택한 칸에 말이 없습니다!");
                    return;
                }

                fromInput.value = clickedPosition;
                return;
            }

            if (fromInput.value === clickedPosition) {
                fromInput.value = "";
                return;
            }

            toInput.value = clickedPosition;
            _this.move(fromInput.value, toInput.value);

            fromInput.value = "";
            toInput.value = "";
        });

    },

    move: function (source, target) {
        const chessGameId = document.querySelector(".chess-game-id").id;
        const option = {
            method: "PUT",
            headers: {
                'Content-Type': 'application/json'
            }
        };
        fetch(`/api/chessgames/${chessGameId}/pieces?source=${source}&target=${target}`, option)
            .then(data => {
                if (!data.ok) {
                    throw new Error("잘못된 명령입니다!");
                }
                return data.json();
            })
            .then(chessGameDto => {
                clearBoard();
                if (chessGameDto.finished) {
                    winToggleButtons(chessGameDto.finished);
                    placePieces(chessGameDto.pieceDtos);
                    toggleStartAndEndButtons(chessGameDto.state);
                    return;
                }

                placePieces(chessGameDto.pieceDtos);
                changeTurn(chessGameDto.state);
            })
            .catch(error => {
                alert(error.message);
            });
    },

    start: function () {
        const data = {
            state: 'BlackTurn'
        };
        const option = {
            method: "PUT",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        };
        const chessGameId = document.querySelector('.chess-game-id').id;
        fetch(`/api/chessgames/${chessGameId}`, option)
            .then(data => {
                return data.json()
            })
            .then(chessGameDto => {
                enrollChessGameId(chessGameDto.chessGameId);
                placePieces(chessGameDto.pieceDtos);
                toggleStartAndEndButtons(chessGameDto.state);
                changeTurn(chessGameDto.state);
            })
            .catch(error => {
                alert("잘못된 명령입니다!");
            });
    },
    end: function () {
        const option = {
            method: "DELETE",
            headers: {
                'Content-Type': 'application/json'
            }
        };
        const chessGameId = document.querySelector(".chess-game-id").id;
        fetch(`/api/chessgames/${chessGameId}`, option)
            .then(data => {
                if (!data.ok) {
                    throw new Error("잘못된 명령입니다!");
                }
                clearBoard();
                return data.json()
            })
            .then(chessGameDto => {
                toggleStartAndEndButtons(chessGameDto.state);
                document.querySelector(".chess-board").id = '';
            })
            .catch(error => {
                alert("[end] 잘못된 명령입니다!")
            });
    },
    scores: function () {
        const chessGameId = document.querySelector('.chess-game-id').id;
        fetch(`/api/chessgames/${chessGameId}/scores`)
            .then(data => {
                return data.json()
            })
            .then(scoreDtos => {
                printScores(scoreDtos);
            })
            .catch(error => {
                alert("잘못된 명령입니다!")
            });
    },
    continue: function () {
        const chessGameId = parseInt(document.querySelector(".chess-game-id").id);
        fetch(`/api/chessgames/${chessGameId}`)
            .then(data => {
                return data.json()
            })
            .then(chessGameDto => {
                enrollChessGameId(chessGameId);
                placePieces(chessGameDto.pieceDtos);
                changeTurn(chessGameDto.state);
                toggleStartAndEndButtons(chessGameDto.state);
                // toggleContinueAndEndButtons(chessGameDto.finished);
            })
            .catch(error => {
                alert("[continue] 잘못된 명령입니다!");
            });
    }
}

loadPage = () => {
    let loadingText = document.querySelector("#loading-text");
    loadingText.parentNode.removeChild(loadingText);
    let loadingContent = document.querySelector("#loading-content");
    loadingContent.parentNode.removeChild(loadingContent);
    let loadingWrapper = document.querySelector("#loading-wrapper");
    loadingWrapper.parentNode.removeChild(loadingWrapper);
}

enrollChessGameId = (chessGameId) => {
    document.querySelector(".chess-board").id = chessGameId;
}

decideClickedPosition = (target) => {
    if (target.tagName === "IMG") {
        return target.parentElement.id;
    }

    if (target.tagName === "TD") {
        return target.id;
    }
}

changeTurn = (state) => {
    if (state === "End") {
        return;
    }

    const turnInfoClassList = document.querySelector(".turn-info.color").classList;
    turnInfoClassList.remove("is-white");
    turnInfoClassList.remove("is-black");

    if (state === "BlackTurn") {
        turnInfoClassList.remove("is-white");
        turnInfoClassList.add("is-black");
        return;
    }

    turnInfoClassList.remove("is-black");
    turnInfoClassList.add("is-white");
}

printScores = (scoreDtos) => {
    document.querySelectorAll(".score")
        .forEach(scoreElement => scoreElement.classList.remove("hidden"));
    document.querySelector(".score-black").value = scoreDtos.blackScore;
    document.querySelector(".score-white").value = scoreDtos.whiteScore;
    document.querySelector(".score-black-value-tag").innerText = scoreDtos.blackScore;
    document.querySelector(".score-white-value-tag").innerText = scoreDtos.whiteScore;
}

winToggleButtons = (finished) => {
    if (!finished) {
        return;
    }

    document.querySelector(".chess-status-btn").classList.add("hidden");
    document.querySelector(".chess-end-btn").classList.add("hidden");
    document.querySelector(".chess-end-btn").classList.remove("hidden");
    document.querySelector(".turn-info.text").innerText = "승리!";
}

toggleStartAndEndButtons = (state) => {
    console.log(`State = ${state}`)
    if (state === "End") {
        document.querySelector(".turn-info.text").innerText = "게임 끝!";
        document.querySelector(".chess-status-btn").classList.add("hidden");
        document.querySelector(".chess-end-btn").classList.add("hidden");
        document.querySelector(".exit").classList.remove("hidden");
        document.querySelector(".start").classList.add("hidden");
        document.querySelector(".continue").classList.add("hidden");

        const statusBar = document.querySelector(".turn-info.color");
        statusBar.parentElement.removeChild(statusBar);
        return;
    }

    if (state === "BlackTurn" || state === "WhiteTurn") {
        document.querySelector(".turn-info.text").innerText = "누구 차례?";
        document.querySelector(".start").classList.add("hidden");
        document.querySelector(".continue").classList.add("hidden");
        document.querySelector(".exit").classList.add("hidden");
        document.querySelector(".chess-status-btn").classList.remove("hidden");
        document.querySelector(".chess-end-btn").classList.remove("hidden");
        return;
    }

    if (state === "Ready") {
        document.querySelector(".turn-info.text").innerText = "게임 준비중";
        document.querySelector(".start").classList.remove("hidden");
        document.querySelector(".continue").classList.add("hidden");
        document.querySelector(".exit").classList.add("hidden");
        document.querySelector(".chess-status-btn").classList.add("hidden");
        document.querySelector(".chess-end-btn").classList.add("hidden");
    }
}

toggleContinueAndEndButtons = () => {
    document.querySelector(".continue").classList.add("hidden");
    document.querySelector(".chess-status-btn").classList.remove("hidden");
    document.querySelector(".chess-end-btn").classList.remove("hidden");
}

clearBoard = () => {
    document.querySelectorAll(".piece")
        .forEach(piece => piece.parentNode.removeChild(piece));
}

placePieces = pieceDtos => {
    pieceDtos.forEach(pieceDto => this.changeChessBoardUnitTemplate(pieceDto));
}

changeChessBoardUnitTemplate = (pieceDto) => {
    const position = pieceDto.position;
    const chessBoardUnit = document.querySelector(`#${position}`);
    const inputValue = `<img class="piece" src="/images/${decidePieceColor(pieceDto.notation)}.png" alt=${pieceDto.notation}>`
    chessBoardUnit.innerHTML = inputValue;
}

decidePieceColor = (notation) => {
    return notation.charCodeAt(0) === notation.toUpperCase().charCodeAt(0)
        ? `black-${notation}` : `white-${notation}`;
}

index.init();
