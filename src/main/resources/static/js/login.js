function login(){

    const pseudoInput = document.getElementById("inputPseudo").value;
    const passwordInput = document.getElementById("inputPassword").value;
    const errorMessage = document.getElementById("errorMessage")

    errorMessage.style.display="none"

    let params = {
        "pseudo":pseudoInput,
        "password": passwordInput,
    }

    const xhr = new XMLHttpRequest();
    xhr.open('POST', '/api/auth/login', true);
    xhr.setRequestHeader('Content-Type', 'application/json')
    xhr.onload = function (){
        if (xhr.status === 200) {
            window.location.href = "/upload";
        }else if(xhr.status != 200){
            errorMessage.style.display="flex"
            errorMessage.textContent = xhr.response
        }
    }
    xhr.send(JSON.stringify(params))
}