window.onload = function () {
    const enterBtn = document.getElementsByClassName("enter-btn");
    Array.from(enterBtn).forEach((el) => {
        el.addEventListener('click', enterGame);
    })

    const deleteBtn = document.getElementsByClassName("delete-btn");
    Array.from(deleteBtn).forEach((el) => {
        el.addEventListener('click', deleteGame);
    })
}

async function enterGame(event) {
    const gameId = event.target.id;
    await fetch(
        `/games/${gameId}`,
    ).then(response => {
        if (response.status === 200) {
            location.href = `games/${gameId}`
        } else {
            alert('에러 발생')
        }
    });
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