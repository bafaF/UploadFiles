import {token, disconnectUser} from "./token.js"

let editEmailInput = document.getElementById("inputEmail")
let editOldPasswordInput = document.getElementById("inputPassword")
let editNewPasswordInput = document.getElementById("inputNewPassword")
let editNewPasswordConfirm = document.getElementById("inputNewPasswordConfirm")
let settingsBody = document.getElementById("settingsBody")
let editProfileSettings = document.getElementById("profileSetting")

/*let deleteProfileLi = document.getElementById("deleteProfileLi")
deleteProfileLi.addEventListener("click", function (){
    showDeleteForm()
})
let deleteProfileForm = document.getElementById("deleteProfileForm")
let inputDeleteProfilePassword = document.getElementById("inputDeleteProfilePassword")
let inputDeleteProfilePasswordConfirm = document.getElementById("inputDeleteProfilePasswordConfirm")
let deleteBtn = document.getElementById("deleteBtn")
deleteBtn.addEventListener("click", function (){
    deleteProfile()
})*/
let returnDeleteProfileButton = document.getElementById("returnDeleteProfileButton")
returnDeleteProfileButton.addEventListener("click", function (){
    hideDeleteForm()
})
let errorDeleteProfileMessage = document.getElementById("errorDeleteProfileMessage")
let editProfileLi = document.getElementById("editProfileLi")
editProfileLi.addEventListener("click", function (){
    showEditForm()
})
let editProfileForm = document.getElementById("editProfileForm")
let editProfileBtn = document.getElementById("editProfileBtn")
editProfileBtn.addEventListener("click", function (){
    showProfileSettings()
})
let editBtn = document.getElementById("editBtn")
editBtn.addEventListener("click", function (){
    verifPassword()
})
let disconnectBtn = document.getElementById("disconnectBtn")
disconnectBtn.addEventListener("click", function (){
    disconnectUser()
})
let returnSettingsButton = document.getElementById("returnSettingsButton")
returnSettingsButton.addEventListener("click", function (){
    hideEditProfileSettings()
})
let returnBtn = document.getElementById("returnButton")
returnBtn.addEventListener("click", function (){
    hideEditForm()
})

window.onload = () => {
    if (token == null) {
        window.location.href = "/upload";
    } else {
        getUserInfo()
    }
}



function getUserInfo(){
    const xhr = new XMLHttpRequest();
    xhr.open('GET', '/api/profile/getUserInfo', true);
    xhr.setRequestHeader('Content-Type', 'application/json')
    xhr.setRequestHeader('Authorization', 'Bearer ' + token);
    xhr.onload = function (){
        if (xhr.status == 200){

            const userInfo = JSON.parse(xhr.response)
            editEmailInput.value = userInfo.email
        }else {
            console.log(xhr.response)
        }
    }
    xhr.send()
}

function showEditForm(){
    settingsBody.style.display = "none"
    editProfileForm.style.display = "flex"
    editOldPasswordInput.value = ""
    editNewPasswordInput.value = ""
    editNewPasswordConfirm.value = ""
    editProfileSettings.style.display = "none"
}

function hideEditForm(){
    editProfileForm.style.display = "none"
    editProfileSettings.style.display = "flex"
}

function hideDeleteForm(){
    deleteProfileForm.style.display = "none"
    editProfileSettings.style.display = "flex"
}

function hideEditProfileSettings(){
    editProfileSettings.style.display = "none"
    settingsBody.style.display = "flex"

}

function verifPassword(){

    if (editNewPasswordInput.value === editNewPasswordConfirm.value){
       edit()
    }else {
        console.log("false")
    }
}

function showProfileSettings(){
    settingsBody.style.display = "none"
    editProfileSettings.style.display = "flex"
}

function edit(){

    let params = {
        "email":editEmailInput.value,
        "oldPassword":editOldPasswordInput.value,
        "newPassword":editNewPasswordInput.value
    }

    const xhr = new XMLHttpRequest();
    xhr.open('POST', '/api/profile/edit', true);
    xhr.setRequestHeader('Content-Type', 'application/json')
    xhr.setRequestHeader('Authorization', 'Bearer ' + token);

    xhr.onload = function (){
        if (xhr.status === 200){
            disconnectUser()
        }else {
            console.log(xhr.response)
        }
    }
    xhr.send(JSON.stringify(params))
}

function showDeleteForm(){
    editProfileSettings.style.display = "none"
    deleteProfileForm.style.display = "flex"
}

/*function deleteProfile(){

    let params = {
        "password": inputDeleteProfilePassword.value
    }

    console.log(inputDeleteProfilePassword.value)

    if (inputDeleteProfilePassword.value === inputDeleteProfilePasswordConfirm.value && inputDeleteProfilePassword.value != ""){
        const xhr = new XMLHttpRequest();
        xhr.open('DELETE', '/api/profile/delete', true);
        xhr.setRequestHeader('Content-Type', 'application/json')
        xhr.setRequestHeader('Authorization', 'Bearer ' + token);
        xhr.onload = function (){
            if (xhr.status === 200){
                disconnectUser()
            }
        }
        xhr.send(JSON.stringify(params))
    } else {
        errorDeleteProfileMessage.textContent = "The password does not correspond"
    }

}*/