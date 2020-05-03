const $roomContainer = document.querySelector(`.room-container`);
const $emptyRoom = document.querySelector(`.room`);

window.onload = async function () {
	await getRooms();
	addClickEvent();
}

function addClickEvent() {
	const $rooms = document.querySelectorAll(`.room`);
	$rooms.forEach(room => room.addEventListener('click', onJoinEvent));
}

function onJoinEvent(e) {
	if (e.target && e.target.nodeName === 'DIV') {
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
