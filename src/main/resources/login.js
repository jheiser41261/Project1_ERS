let user;

window.onload = async function(){
    let response = await fetch("http://localhost:9000/check-session")
    let responseBody = await response.json()

    user = responseBody.data

    if(user != null){
        await fetch("http://localhost:9000/logout", {
            method: "DELETE"
        })
    }
}

document.getElementById("login-form").addEventListener("submit",

async function login(event){
    event.preventDefault();

    let usernameInputElem = document.getElementById("username");
    let passwordInputElem = document.getElementById("password");

    let response = await fetch("http://localhost:9000/login", {
        method: "POST",
        body: JSON.stringify({
            username: usernameInputElem.value,
            password : passwordInputElem.value
        })
    })

    let responseBody = await response.json()

    if(!responseBody.success){
        let errorElem = document.getElementById("error")

        errorElem.innerHTML = 
        `<div class="alert alert-danger alert-dismissible fade show" role="alert">
            <strong>ERROR: </strong><span>` + responseBody.message + `</span>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>`
    } else if(responseBody.data.role == 2){
        window.location = "./fm/allreimbs"
    } else{
        window.location = "./employee/"
    }
})