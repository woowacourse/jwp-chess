const $rooms = document.querySelector("#rooms");

$rooms.addEventListener('click', removeRoom);

function removeRoom(event) {
    const target = event.target;
    if (target && target.className === "destroy") {
        // fetch api
        const roomName = target.parentNode
            .querySelector("input[name=roomName]")
            .getAttribute("value");

        const option = {
            method: "DELETE",
            headers: {
                'Content-Type': 'application/json'
            }
        };

        fetch(`/delete/${roomName}`, option)
            .then(repsonse => {
                if (!repsonse.ok) {
                    throw new Error(roomName + "방을 삭제 할 수 없습니다.");
                }
                return repsonse.json();
            })
            .then(body => {
                // remove room list from ui
                const li = target.parentNode.parentNode;
                li.parentNode.removeChild(li);
            })
            .catch(error => {
                alert(error.name + ": " + error.message);
            })
    }
}

