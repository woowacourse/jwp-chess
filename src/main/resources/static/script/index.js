const deleteGameButtons = document.getElementsByClassName('delete-game-button');

for (const deleteButton of deleteGameButtons) {
    deleteButton.addEventListener('click', async (event) => {
        await deleteGame(event);
    })
}

async function deleteGame(event) {
    const target = event.target;
    const res = await fetch("/game/"+target.id, {
        method: 'DELETE'
    })
    if (!res.ok) {
        alert(res.json())
        return
    }
    target.closest('tr').innerHTML = '';
}