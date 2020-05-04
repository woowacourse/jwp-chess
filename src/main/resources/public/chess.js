const $board = document.querySelector('.board');
const $roomName = document.querySelector('#room-name');

let $sourceDiv;
let $targetDiv;
let sourceId;
let targetId;
let tempColor;
let pickFlag = false;

window.onload = function () {
	$board.addEventListener('click', onClickBoard)
}

async function onClickBoard(e) {
	if (e.target && e.target.nodeName === 'IMG') {
		if (!pickFlag) {
			$sourceDiv = e.target.closest('div');
			sourceId = $sourceDiv.id;

			tempColor = $sourceDiv.style.backgroundColor;
			$sourceDiv.style.backgroundColor = 'rgba(0, 0, 0, 0.5)';
			pickFlag = !pickFlag;
		} else {
			$targetDiv = e.target.closest('div');
			targetId = $targetDiv.id;

			const movedInfo = await getMovedInfo();
			renderMovedPiece();
			renderChangedTurn(movedInfo.turn);

			$sourceDiv.style.backgroundColor = tempColor;
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
			source: sourceId,
			target: targetId
		})
	}).then(res => res.json());
}

function renderMovedPiece() {
	[$sourceDiv.innerHTML, $targetDiv.innerHTML] = [$targetDiv.innerHTML, $sourceDiv.innerHTML]; // swap
}

function renderChangedTurn(turn) {
	const $turn = document.querySelector(`#turn`);
	if (turn) {
		$turn.innerText = turn;
	}
}