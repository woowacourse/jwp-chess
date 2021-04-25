const startBtn = document.getElementById('start-btn');
const roomListBtn = document.getElementById('room-list-btn');

startBtn.addEventListener('click', function () {
    window.location.href = '/start';
});

roomListBtn.addEventListener('click', function () {
   window.location.href = '/room-list';
});