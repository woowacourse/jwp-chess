document.querySelectorAll(".position").forEach(element => {
    element.addEventListener("click", async function () {
        if (document.getElementById("moveStart").value) {
            document.getElementById("moveEnd").value = element.id;
            document.getElementById("moveCommand").value = "이동";
            document.getElementById("moveForm").submit();
        } else {
            document.getElementById("start_candidate").value = element.id;
            document.getElementById("movableCommand").value = "갈 수 있는 곳 확인";
            document.getElementById("movableForm").submit();
        }
    });
});