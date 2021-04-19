const interact = document.getElementById("interact").value;

roomInteract();

async function roomInteract() {
    console.log(interact);
    if (interact == -1) {
        confirm("이미 존재하는 방입니다.");
    }

    if (interact == -2) {
        confirm("존재하지 않는 방입니다.");
    }
}