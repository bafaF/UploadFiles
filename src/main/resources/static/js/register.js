function register(){

    const pseudoInput = document.getElementById("inputPseudo").value;
    const passwordInput = document.getElementById("inputPassword").value;
    const emailInput = document.getElementById("inputEmail").value;
    let errorMessage = document.getElementById("errorMessage")

    var params = {
        "pseudo":pseudoInput,
        "password": passwordInput,
        "email": emailInput
    }

    const xhr = new XMLHttpRequest();
    xhr.open('POST', '/api/auth/register', true);
    xhr.setRequestHeader('Content-Type', 'application/json')
    xhr.onload = function (){
        if (xhr.status === 200) {
            window.location.href = "/login";
        }else {
            errorMessage.textContent = xhr.response
        }
    }
    xhr.send(JSON.stringify(params))
}