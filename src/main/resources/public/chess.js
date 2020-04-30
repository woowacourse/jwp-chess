const BLANK_IMAGE = `<img class="piece-image" src="/img/BLANK.png">`;
let source;
let target;

window.onload = function () {
  const elements = document.querySelectorAll(".cell");
  [].forEach.call(elements, (elem) => {
    elem.addEventListener("click", (ev) => {
      if (source === undefined) {
        source = elem.id;
      } else {
        target = elem.id;

        fetch("/api/move", {
          method: "post",
          headers: {
            "Content-Type": "application/json"
          },
          body: JSON.stringify({
            roomName: document.getElementById('room-name').innerText,
            source: source,
            target: target
          })
        }).then(async res => {
          if (res.status !== 200) {
            source = undefined;
          } else {
            renderMovedPiece();
            renderChangedTurn(await res.json());
          }
        })
      }
    })
  })
}

function renderMovedPiece() {
  const sourceElem = document.querySelector(`#${source}`);
  const targetElem = document.querySelector(`#${target}`);
  const tempElem = sourceElem.innerHTML;
  sourceElem.innerHTML = BLANK_IMAGE;
  targetElem.innerHTML = tempElem;
}

function renderChangedTurn(data) {
  const turn = document.querySelector(`#turn`);
  turn.innerText = data.turn;
}