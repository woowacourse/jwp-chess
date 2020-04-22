window.onload = function() {
    for (let i = 0; i < 64; i++) {
        let cell = "white-cell"
        if (((Math.floor(i / 8) % 2 === 0) && (i % 2 === 0)) || (Math.floor(i / 8) % 2 === 1) && (i % 2 === 1)) {
            // 홀수 줄의 홀수 번째 칸 또는 짝수줄의 짝수번째 칸이라면
            cell = "black-cell"
        }
        $(".cell:eq(" + i + ")").addClass(cell);
    }
}