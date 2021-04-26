const room = function (title, locked, playerAmount) {
  return `    <div class="room-item">
      <div class="room-contents">${title}</div>
      <div class="room-detail">
        <div class="room-people">${playerAmount}/2</div>
        <div class="room-access"><i class="fas fa-${locked ? 'lock' : 'unlock'}"></i></div>
      </div>
    </div>`
}

const createRoom = '<div class="room-item create-room"><span>+</span></div>';