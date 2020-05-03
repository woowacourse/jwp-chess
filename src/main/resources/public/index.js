window.onload = function () {
	getRooms();
}

function getRooms() {
	fetch('/api/rooms')
	.then(async (res) => {
		if (res.status !== 200) {
			console.log(res);
		}
		bindRooms(await res.json());
	})
}

function bindRooms(data) {
	for (let i in data) {
		const roomName = document.createElement('div');
		roomName.setAttribute('class', 'room');
		roomName.innerText = data[i].name;
		document.querySelector(`.room-container`).appendChild(roomName);
	}
}