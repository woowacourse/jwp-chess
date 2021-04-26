class CommandExecutor {

  execute(response) {
    switch (response.form) {
      case 'UPDATE_ROOM' :
        const roomList = document.querySelector('.room-list');
        roomList.innerHTML = '';
        roomList.innerHTML = response.data.reduce((prev, curr) => {
          const {title, locked, playerAmount} = curr;
          return prev + room(title, locked, playerAmount);
        }, '').toString() + createRoom;
        break;
    }
  }
}