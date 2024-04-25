let token = getCookie()
function getCookie() {
    let cookie = {};
    document.cookie.split(';').forEach(function(el) {
        let [key,value] = el.split('=');
        cookie[key.trim()] = value;
    })
    return cookie["user-token"]
}

function disconnectUser(){
    const xhr = new XMLHttpRequest();
    xhr.open('GET', '/api/auth/disconnect', true);
    xhr.setRequestHeader('Content-Type', 'application/json')
    xhr.onload = function (){
        if (xhr.status === 200) {
            window.location.href = "/upload";
        }
    }
    xhr.send()
}

export {token, disconnectUser}