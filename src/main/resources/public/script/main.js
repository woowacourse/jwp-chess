window.onload = function () {
    const deleteBtn = document.getElementsByClassName("delete-btn");
    Array.from(deleteBtn).forEach((el) => {
        el.addEventListener('click', deleteRoom());
    })
}

function deleteRoom() {
    return function (event) {
        const roomId = event.target.id;
        $.ajax({
            url: "/rooms/" + roomId,
            type: "DELETE",
            success: function () {
                alert("삭제 완료");
                location.href="/";
            },
            error: function () {
                alert("에러 발생");
            }
        })
    }
}