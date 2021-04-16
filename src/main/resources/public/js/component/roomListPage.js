export async function renderRoomListPage() {
    const $layoutWrap = document.getElementById("layout-wrap");
    const div = document.createElement("div");
    div.innerHTML = `
<div id="layout">
    <button class="btn">
        체스방 만들기
    </button>
    <div>
        <div>
            <button class="btn">1번방</button>
        </div>
        <div>
            <button class="btn">1번방</button>
        </div>
    </div>
</div>
`
    $layoutWrap.appendChild(div);
}

