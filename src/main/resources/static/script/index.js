const gameUrl = '/game';

const deleteGameButtons = document.getElementsByClassName('delete-game-button');

for (const deleteButton of deleteGameButtons) {
    deleteButton.addEventListener('click', async (event) => {
        await deleteGame(event);
    })
}

async function deleteGame(event) {
    fetch(gameUrl, {
        method: 'DELETE',
        headers: {
                'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify({
            id : event.target.id,
            password : prompt('비밀번호를 입력해주세요!')
        })
    }).then(res => res.json())
        .then(json => {

            if (json.success !== undefined && json.success === false) {
                alert(json.message);
            }

            if (json.success === true) {
                alert(`${json.gameId} 번 방이 삭제됐습니다`);
                location.href = '/';
            }
        })
}