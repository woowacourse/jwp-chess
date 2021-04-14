export const PIECES = {
  K: `<img src= "/image/K.png" class = "black piece"/>`,
  Q: `<img src= "/image/Q.png" class = "black piece" />`,
  B: `<img src= "/image/B.png" class = "black piece"/>`,
  N: `<img src= "/image/N.png" class = "black piece"/>`,
  R: `<img src= "/image/R.png" class = "black piece"/>`,
  P: `<img src= "/image/P.png" class = "black piece"/>`,
  k: `<img src= "/image/w_K.png" class = "white piece"/>`,
  q: `<img src= "/image/w_Q.png" class = "white piece"/>`,
  b: `<img src= "/image/w_B.png" class = "white piece"/>`,
  n: `<img src= "/image/w_N.png" class = "white piece"/>`,
  r: `<img src= "/image/w_R.png" class = "white piece"/>`,
  p: `<img src= "/image/w_P.png" class = "white piece"/>`,
  '.': `<img/>`,
}

export function SCORE_TEMPLATE(whiteScore, blackScore) {
  return `
  <div class="white">
          <div class="player-color">W</div>
          <div class="player">(PL1)</div>
          <div class="player-score">${whiteScore || 38}</div>
        </div>
        <div class="vertical-divider"></div>
        <div class="black">
          <div class="player-color">B</div>
          <div class="player">(PL2)</div>
          <div class="player-score">${blackScore || 38}</div>
        </div>`;
}