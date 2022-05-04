const deleteButtons = document.getElementsByClassName('btn-primary')

for (const deleteButton of deleteButtons) {
  deleteButton.addEventListener('click', async (event) => {
    await deleteGame(event);
  })
}

async function deleteGame(event) {
  fetch('/game', {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json;charset=utf-8'
    },
    body: JSON.stringify({
      id: event.target.id,
      password : prompt('비밀번호를 입력하세요')
    })
  }).then(async res => {
    if (res.status === 204) {
      alert(event.target.id +'번 방이 삭제 완료되었습니다.');
      location.href = '/';
    }
    const json = await res.json()
    if (res.status === 400) {
      alert(json.message);
      return;
    }
    if (res.status === 500) {
      alert(json.message);
    }
  })
}
