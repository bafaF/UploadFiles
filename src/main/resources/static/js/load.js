import {token} from "./token.js";

window.onload = () => {

    const fileId = location.pathname.slice(6)
    const filename = document.getElementById("filename-text")
    const uploadingDate = document.getElementById("uploadingDate")
    const fileSize = document.getElementById("fileSize")
    const fileDeletingDate = document.getElementById("fileDeletingDate")
    const fileOwner = document.getElementById("fileOwner")
    const downloadButton = document.getElementById("downloadButton")
    const buttonDwnl = document.createElement("button")

    const xhr = new XMLHttpRequest();

    xhr.open('GET', '/api/file/load/' + fileId, true);

    if (token != null){
        xhr.setRequestHeader('Authorization', 'Bearer ' + token);
    }

    xhr.onload = function () {
        if (xhr.status === 200) {
            const file = JSON.parse(xhr.response)
            filename.textContent = file.originalFilename
            uploadingDate.textContent = "Downloading date : " + formatDate(new Date(file.downloadDate))
            fileSize.textContent = "Size : " + file.size
            fileDeletingDate.textContent = "Deleting date : " + formatDate(new Date(file.deletingDate))
            buttonDwnl.type = "submit"
            buttonDwnl.textContent = "Download"
            buttonDwnl.addEventListener("click", function () {
                load(file.id)
            });
            buttonDwnl.id = "downloadBtn"
            downloadButton.appendChild(buttonDwnl)
            if (file.owner != null){
                fileOwner.textContent = "Owner : " + file.owner
            }

        }
    }

    xhr.send()
}



function formatDate(date){

    var formatedDate =
        ("00" + (date.getMonth() + 1)).slice(-2) + "/" +
        ("00" + date.getDate()).slice(-2) + "/" +
        date.getFullYear() + " " +
        ("00" + date.getHours()).slice(-2) + ":" +
        ("00" + date.getMinutes()).slice(-2) + ":" +
        ("00" + date.getSeconds()).slice(-2);
    return formatedDate

}

function load(fileId){

    const xhr = new XMLHttpRequest();

    xhr.open('GET', '/api/file/setDownloadedUser/' + fileId, true);
    if (token != null){
        xhr.setRequestHeader('Authorization', 'Bearer ' + token);
    }

    xhr.onload = function () {
        if (xhr.status == 200){
            window.location.href = '/api/file/download/' + xhr.response;
        }
    }

    xhr.send()

}