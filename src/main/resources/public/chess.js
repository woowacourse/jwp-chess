const $board = document.querySelector('.board');
const $roomName = document.querySelector('#room-name');

let source;
let target;
let pickFlag = false;

window.onload = function () {
	$board.addEventListener('click', onClickBoard)
}

async function onClickBoard(e) {
	if (e.target && e.target.nodeName === 'IMG') {
		if (!pickFlag) {
			source = e.target.closest('div').id;
			pickFlag = !pickFlag;
		} else {
			target = e.target.closest('div').id;
			const movedInfo = await getMovedInfo();
			renderMovedPiece();
			renderChangedTurn(movedInfo.turn);
			pickFlag = !pickFlag;
		}
	}
}

function getMovedInfo() {
	return fetch('/api/move', {
		method: 'post',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify({
			roomName: $roomName.innerText,
			source: source,
			target: target
		})
	}).then(res => res.json());
}

function renderMovedPiece() {
	let $source = document.querySelector(`#${source}`);
	let $target = document.querySelector(`#${target}`);
	[$source.innerHTML, $target.innerHTML] = [$target.innerHTML, $source.innerHTML]; // swap
}

function renderChangedTurn(turn) {
	const $turn = document.querySelector(`#turn`);
	if (turn) {
		$turn.innerText = turn;
	}
}