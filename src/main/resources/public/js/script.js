import * as restartBtn from "./component/restartBtn.js";
import * as startBtn from "./component/startBtn.js";
import * as roomListPage from "./component/roomListPage.js"

(function () {
    restartBtn.addEvent();
    startBtn.addEvent();
})();

roomListPage.renderRoomListPage();