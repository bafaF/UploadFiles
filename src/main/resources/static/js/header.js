import {token, disconnectUser} from "./token.js"

document.addEventListener('DOMContentLoaded', (event) => {

        const connectedDiv = document.getElementById("connected")
        const notConnectedDiv = document.getElementById("notconnected")

        if(token == null){
            connectedDiv.style.display="none";
        }else {
            notConnectedDiv.style.display="none";
        }
}
);