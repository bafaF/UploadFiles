import {token} from "./token.js";

const uploadedFilesTable = document.getElementById("uploadedFileTable")
const downloadedFilesTable = document.getElementById("downloadedFileTable")

let detailDiv = document.getElementById("detail")
const infoTable = document.getElementById("infoTable")

let infoFilename = document.createElement("dt")
let infoSize = document.createElement("dt")
let infoUploadDate = document.createElement("dt")
let infoDeleteDate = document.createElement("dt")
let infoLink = document.createElement("dt")
let infoDelete = document.createElement("dt")
let infoOwner = document.createElement("dt")

let infoFilenameValue = document.createElement("dd")
let infoSizeValue = document.createElement("dd")
let infoUploadDateValue = document.createElement("dd")
let infoDeletingDateValue = document.createElement("dd")
let infoLinkValue = document.createElement("dd")
let infoOwnerValue = document.createElement("dd")
let infoDownloadedDateValue = document.createElement("dd")

let closeDiv = document.getElementById("btnDiv")
let downloadedFilesBtn = document.getElementById("downloadedFilesBtn")
let uploadedFilesBtn = document.getElementById("uploadedFilesBtn")
let profileFileTable = document.getElementById("profileFileTable")
let profileDownloadTable = document.getElementById("profileDownloadTable")
let downloadedTable = document.getElementById("downloadedTable")

let files;

window.onload = () => {

    if (token == null) {
        window.location.href = "/upload";
    }

    downloadedFilesBtn.addEventListener("click", function (){
        profileFileTable.style.display = "none"
        loadHistory()
    })

    uploadedFilesBtn.addEventListener("click", function (){
        profileDownloadTable.style.display = "none"
        loadUploadedFiles()
    })

    loadUploadedFiles()

};

function formatDate(date) {

    var formatedDate = ("00" + (date.getMonth() + 1)).slice(-2) + "/" + ("00" + date.getDate()).slice(-2) + "/" + date.getFullYear() + " " + ("00" + date.getHours()).slice(-2) + ":" + ("00" + date.getMinutes()).slice(-2) + ":" + ("00" + date.getSeconds()).slice(-2);
    return formatedDate

}

function loadUploadedFiles(){

    profileFileTable.style.display = "flex"

    for (let i = uploadedFilesTable.rows.length; i!=0; i--){
        uploadedFilesTable.deleteRow(i-1)
    }

    const xhr = new XMLHttpRequest();
    xhr.open('GET', '/api/file/myFiles', true);
    xhr.setRequestHeader('Content-Type', 'application/json')
    xhr.setRequestHeader('Authorization', 'Bearer ' + token);

    xhr.onload = function () {
        if (xhr.status === 200) {
            files = JSON.parse(xhr.response)
            for (let i = 0; i < files.length; i++) {

                let upldate = new Date(files[i].uploadDate)

                let tableRow = document.createElement('tr');

                let filenameTd = document.createElement("td")
                filenameTd.textContent = files[i].originalFilename;

                let uploadDateTd = document.createElement("td")
                uploadDateTd.textContent = formatDate(upldate);

                let selectTd = document.createElement("td")

                let selectBtn = document.createElement("a")
                selectBtn.innerHTML = "<a class='button'>Select</a>"

                selectBtn.addEventListener("click", function () {
                    openUploadedFileInfo(i)
                })

                selectTd.appendChild(selectBtn)

                tableRow.appendChild(filenameTd)
                tableRow.appendChild(uploadDateTd)
                tableRow.appendChild(selectTd)

                uploadedFilesTable.appendChild(tableRow);
            }
        }
    }
    xhr.send()
}

function openUploadedFileInfo(fileIndex) {

    infoOwner.style.display = "none"

    infoDelete.style.display = "flex"
    infoDelete.style.flexDirection = "column"
    infoDeleteDate.style.display = "flex"
    infoDeleteDate.style.flexDirection = "column"

    detailDiv.style.display = "flex"

    let infoDeleteValue = document.createElement("dd")

    let file = files[fileIndex]

    infoFilename.textContent = "Filename"
    infoSize.textContent = "Size"
    infoUploadDate.textContent = "Upload date"
    infoDeleteDate.textContent = "Delete date"
    infoLink.textContent = "Link"
    infoDelete.textContent = "Delete file"

    let uploadDate = formatDate(new Date(file.uploadDate))
    let deletingDate = formatDate(new Date(file.deletingDate))

    infoFilenameValue.textContent = file.originalFilename
    infoSizeValue.textContent = file.size
    infoUploadDateValue.textContent = uploadDate
    infoDeletingDateValue.textContent = deletingDate

    infoLinkValue.innerHTML = "<a href='/load/" + file.id + "' class='button'>Download</a>"

    infoDeleteValue.innerHTML = "<button class='button'>Delete</button>"
    infoDeleteValue.addEventListener("click", function () {
        deleteFile(file.id)
    })

    closeDiv.innerHTML = "<button class='close button' id='btnClose'>Close</button>"
    let closeBtn = document.getElementById("btnClose")
    closeBtn.style.width = "100%"
    closeBtn.addEventListener("click", function () {
        detailDiv.style.display = "none"
    })

    infoFilename.appendChild(infoFilenameValue)
    infoSize.appendChild(infoSizeValue)
    infoUploadDate.appendChild(infoUploadDateValue)
    infoDeleteDate.appendChild(infoDeletingDateValue)
    infoLink.appendChild(infoLinkValue)
    infoDelete.appendChild(infoDeleteValue)

    infoTable.appendChild(infoFilename)
    infoTable.appendChild(infoSize)
    infoTable.appendChild(infoUploadDate)
    infoTable.appendChild(infoDeleteDate)
    infoTable.appendChild(infoLink)
    infoTable.appendChild(infoDelete)
}

function openDownloadedFileInfo(fileIndex){

    detailDiv.style.display = "flex"
    infoOwner.style.display = "flex"
    infoOwner.style.flexDirection = "column"

    infoDeleteDate.style.display = "none"
    infoDelete.style.display = "none"

    let file = files[fileIndex]

    infoFilename.textContent = "Filename"
    infoSize.textContent = "Size"
    infoUploadDate.textContent = "Download date"
    infoLink.textContent = "Link"
    infoOwner.textContent = "Owner"

    let downloadDate = formatDate(new Date(file.lastDownloadedDate))

    infoFilenameValue.textContent = file.filename
    infoSizeValue.textContent = file.size
    infoDownloadedDateValue.textContent = downloadDate
    infoOwnerValue.textContent = file.owner

    if (file.link != "Expired"){
        infoLinkValue.innerHTML = "<a href='/load/" + file.link + "' class='button'>Download</a>"
    }else {
        infoLinkValue.textContent = file.link
    }

    closeDiv.innerHTML = "<button class='close button' id='btnClose'>Close</button>"
    let closeBtn = document.getElementById("btnClose")
    closeBtn.style.width = "100%"
    closeBtn.addEventListener("click", function () {
        detailDiv.style.display = "none"
    })

    infoFilename.appendChild(infoFilenameValue)
    infoSize.appendChild(infoSizeValue)
    infoUploadDate.appendChild(infoDownloadedDateValue)
    infoOwner.appendChild(infoOwnerValue)
    infoLink.appendChild(infoLinkValue)

    infoTable.appendChild(infoFilename)
    infoTable.appendChild(infoSize)
    infoTable.appendChild(infoUploadDate)
    infoTable.appendChild(infoOwner)
    infoTable.appendChild(infoLink)
}

function deleteFile(id) {

    const xhr = new XMLHttpRequest();

    xhr.open('DELETE', '/api/file/delete/' + id, true);
    xhr.setRequestHeader('Authorization', 'Bearer ' + token);

    xhr.onload = function () {
        if (xhr.status === 200) {
            location.reload();
        }
    }
    xhr.send()
}

function loadHistory() {

    for (let i = downloadedFilesTable.rows.length; i!=0; i--){
        downloadedFilesTable.deleteRow(i-1)
    }
    downloadedTable.style.display = "table"
    profileDownloadTable.style.display = "table"

    let table = document.getElementById("downloadedFileTable")

    const xhr = new XMLHttpRequest();
    xhr.open('GET', '/api/file/filehistory', true);
    xhr.setRequestHeader('Authorization', 'Bearer ' + token);

    xhr.onload = function () {
        if (xhr.status === 200) {

            files = JSON.parse(xhr.response)
            for (let i = 0; i < files.length; i++) {

                let filenameTd = document.createElement("td")
                filenameTd.textContent = files[i].filename;

                let dwnldate = new Date(files[i].lastDownloadedDate)

                let downloadDateTd = document.createElement("td")
                downloadDateTd.textContent = formatDate(dwnldate);

                let tableRow = document.createElement('tr');

                let selectTd = document.createElement("td")

                let selectBtn = document.createElement("a")
                selectBtn.innerHTML = "<a class='button'>Select</a>"

                selectBtn.addEventListener("click", function () {
                    openDownloadedFileInfo(i)
                })

                selectTd.appendChild(selectBtn)

                tableRow.appendChild(filenameTd)
                tableRow.appendChild(downloadDateTd)
                tableRow.appendChild(selectTd)

                table.appendChild(tableRow);
            }
        }
    }
    xhr.send()
}