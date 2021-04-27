let signupForm = document.getElementById("signupForm");
let signupBtn = document.getElementById("signupBtn");
let signupSubmit = document.getElementById("signupSubmit");
let signupCloseBtn = document.getElementById("signupCloseBtn");

signupBtn.addEventListener("click", showSignupForm);
signupSubmit.addEventListener("click", signup);
signupCloseBtn.addEventListener("click", close);

function showSignupForm() {
    signupForm.style.display = "flex";
}

function signup() {
    console.log("signup");
}

function close() {
    signupForm.style.display = "none";
}
