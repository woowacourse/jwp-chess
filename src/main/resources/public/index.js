const $roomContainer = document.querySelector(`.room-container`);
const $emptyRoom = document.querySelector(`.room`);
const $createBtn = document.querySelector(`#create-btn`);
const $createInput = document.querySelector(`#create-input`);
const $createForm = document.querySelector(`#create-form`);

window.onload = async function () {
	await getRooms();

	const $roomContainer = document.querySelector(`#room-container`);
	$roomContainer.addEventListener('click', onRoomClick);

	$createBtn.addEventListener('click', onCreateBtnClick);
}

function onCreateBtnClick(e) {
	let roomName = prompt('방 이름을 입력해주세요.');
	if (roomName) {
		$createInput.setAttribute('value', roomName);
		$createForm.submit();
	}
}

function onRoomClick(e) {
	if (e.target && e.target.className === 'room') {
		const roomName = e.target.innerText;
		location.href = `/room/${roomName}`;
	}
}

function getRooms() {
	return fetch('/api/rooms')
	.then(res => res.json())
	.then(data => {
		if (data.length === 0) {
			bindEmptyMsg();
		} else {
			bindRooms(data);
		}
	});
}

function bindEmptyMsg() {
	$roomContainer.removeChild($emptyRoom);
	const $emptyRoomMsg = document.createElement('h1');
	$emptyRoomMsg.innerText = '생성된 방이 없습니다.';
	$roomContainer.appendChild($emptyRoomMsg);
}

function bindRooms(data) {
	$roomContainer.removeChild($emptyRoom);
	for (let i in data) {
		const $room = document.createElement('div');
		$room.setAttribute('class', 'room');
		$room.innerText = data[i].name;
		$roomContainer.appendChild($room);
	}
}
