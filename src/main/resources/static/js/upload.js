import {token} from "./token.js"

function formatDate(date){

    return ("00" + (date.getMonth() + 1)).slice(-2) + "/" +
        ("00" + date.getDate()).slice(-2) + "/" +
        date.getFullYear() + " " +
        ("00" + date.getHours()).slice(-2) + ":" +
        ("00" + date.getMinutes()).slice(-2) + ":" +
        ("00" + date.getSeconds()).slice(-2)

}

let uploadBtn = document.querySelector("#uploadBtn");

uploadBtn.addEventListener("click", function (){
    upload()
});

const infoArea = document.getElementById( 'filename-text' );
const fileSize = document.getElementById("fileSize");
const fileModif = document.getElementById("fileLastModification");
const fileDeletingDate = document.getElementById("fileDeletingDate");
const fileLink = document.getElementById("fileLink");
let fileInput = document.getElementById("input-file")
let circularProgress = document.querySelector(".circular-progress");
let progressValue = document.querySelector(".progress-value");

fileInput.addEventListener("change", function (){
    showFileName(event)
})

let input;
function showFileName( event ) {

    circularProgress.style.background = "conic-gradient(limegreen 0deg, #ededed 0deg)"
    progressValue.textContent = ""
    fileInput = document.getElementById("input-file")

    let date = new Date();
    let uploadDate = formatDate(date)

    fileLink.textContent = "";
    input = null;
    // the change event gives us the input it occurred in
    input = event.target.files[0];
    const size = input.size;
    const uploadingDate = uploadDate;

    // the input has an array of files in the `files` property, each one has a name that you can use. We're just using the name here.
    const fileName = input.name;
    const sizeInKo = size /1024;
    const sizeInMo = sizeInKo /1024;
    const sizeInGo = sizeInMo /1024;
    let deletingDate;
    fileSize.textContent = "File size : "

    if(sizeInGo >= 1){
        fileSize.textContent += Math.round(sizeInGo *100 )/100 + " Go"
    }else if(sizeInMo >= 1){
        fileSize.textContent += Math.round(sizeInMo *100 )/100 + " Mo"
    }else {
        fileSize.textContent += Math.round(sizeInKo *100 )/100 + " Ko"
    }

    if (sizeInGo > 50){
        deletingDate = new Date(date.getTime() + 86400000)
    } else {
        deletingDate = new Date(date.getTime() + (86400000 * 7))
    }

    let delDate = formatDate(deletingDate)

    fileDeletingDate.textContent = "Auto deleting : " + delDate;
    fileModif.textContent = "Uploading date : " + uploadingDate;
    infoArea.textContent = "Filename : " + fileName;
    uploadBtn.disabled = false;

}

function upload(){

    const fileLink = document.getElementById("fileLink");
    let progressStartValue = 0;
    let file = input
    let formData = new FormData();
    formData.append('file', file);

    const xhr = new XMLHttpRequest();
    xhr.open('POST', '/api/file/upload', true);
    if (token != null){
        xhr.setRequestHeader('Authorization', 'Bearer ' + token);
    }

    uploadBtn.disabled = true;

    xhr.onload = function (){
        if (xhr.status === 200){

            let fileId = xhr.response
            fileLink.innerHTML = '<a href="load/'  + fileId + '"' + ' id="fileLink" class="card-text">Download link here</a>'
            fileInput.disabled = false
            input = null
            fileInput = null
        } else {
            console.log("Error while uploading the file, try again")
        }
    }
    xhr.upload.onprogress = e => {
        if (e.lengthComputable) {
            progressStartValue = (e.loaded / e.total) * 100;
            circularProgress.style.background = `conic-gradient(limegreen ${progressStartValue * 3.6}deg, #ededed 0deg)`
            progressValue.textContent = Math.round(progressStartValue * 1) /1 + "%"
        }
        fileInput.disabled = true;
    };
    xhr.send(formData);
}


