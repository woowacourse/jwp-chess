document.querySelectorAll(".position").forEach(element => {
    element.addEventListener("click", async function () {
        let command;

        if (document.getElementById("moveStart").value) {
            document.getElementById("moveEnd").value = element.id;
            command = "이동";
            document.getElementById("moveForm").submit();
        } else {
            document.getElementById("moveStart").value = element.id;
            document.getElementById("moveStart-movable").value = element.id;
            command = "갈 수 있는 곳 확인";
            document.getElementById("movableForm").submit();
        }
    });
});
