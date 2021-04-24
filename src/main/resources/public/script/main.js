window.onload = function () {
    const deleteBtn = document.getElementsByClassName("delete-btn");
    Array.from(deleteBtn).forEach((el) => {
        el.addEventListener('click', deleteRoom);
    })
}

async function deleteRoom(event) {
    const roomId = event.target.id;
    await fetch(
        `/rooms/${roomId}`,
        {
            method: 'DELETE'
        }
    ).then(response => {
        if (response.status === 200) {
            alert('삭제 완료')
            location.href = '/'
        } else {
            alert('에러 발생')
        }
    });
}