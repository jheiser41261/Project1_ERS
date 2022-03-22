document.getElementById("login-form").addEventListener("submit",

async function login(event){
    event.preventDefault();

    let usernameInputElem = document.getElementById("username");
    let passwordInputElem = document.getElementById("password");

    let user = {
        username: usernameInputElem.value,
        password: passwordInputElem.value
    }

    let response = await fetch("http://localhost:9000/fmlogin", {
        method: "POST",
        body: JSON.stringify(user)
    })

    let responseBody = await response.json()

    if(responseBody.success == false){
        let errorElem = document.getElementById("error")

        errorElem.innerHTML = 
        `<div class="alert alert-danger alert-dismissible fade show" role="alert">
            <strong>ERROR: </strong><span>` + responseBody.message + `</span>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>`
    } else{
        window.location = `./allreimbs/?userId=${responseBody.data.id}&username=${responseBody.data.username}`
    }
})