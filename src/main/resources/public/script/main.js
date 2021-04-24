window.onload = function () {
    const deleteBtn = document.getElementsByClassName("delete-btn");
    Array.from(deleteBtn).forEach((el) => {
        el.addEventListener('click', deleteGame);
    })
}

async function deleteGame(event) {
    const gameId = event.target.id;
    await fetch(
        `/games/${gameId}`,
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