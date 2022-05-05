const createRoom = document.querySelector("#room-create");

createRoom.addEventListener("click", onClickMakeJoinButton);

async function onClickMakeJoinButton () {
    location.href="roommake.hbs";
}
