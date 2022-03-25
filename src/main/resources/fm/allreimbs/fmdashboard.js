let user;
let statusId = 0;

window.onload = async function(){
    let response = await fetch("http://localhost:9000/check-session")
    let responseBody = await response.json()

    user = responseBody.data

    if(!responseBody.success){
        window.location = "../../"
    }

    if(user.role != 2){
        window.location = "../../employee"
    }

    let welcomeMessage = document.getElementById("dashboard")
    welcomeMessage.innerText = "Welcome, " + user.firstName + "!"

    getAllReimbs()
}

async function getAllReimbs(){
    let spinner = document.getElementById("spinner")
    spinner.removeAttribute('disabled')

    let response = await fetch(`http://localhost:9000/${user.username}/all`)
    let responseBody = await response.json()

    let allReimbs = responseBody.data

    allReimbs.forEach(reimbursement => {
        createReimb(reimbursement)
    });

    spinner.setAttribute('disabled', '')
}

function createReimb(reimb){
    document.getElementById("spinner-container").style.display = "block"

    let reimbTableElem = document.getElementById("reimbs");

    let reimbElem = document.createElement("tr");
    reimbElem.className = "reimb-row";

    reimb.amount = parseFloat(reimb.amount).toFixed(2);

    reimb.submitted = new Date(reimb.submitted).toDateString();

    if(reimb.resolver == null){
        reimb.resolver = "N/A"
    }

    reimbElem.innerHTML = 
    `
        <tr>
            <td id="reimb-num">${reimb.id}</td>
            <td>${reimb.author}</td>
            <td>${reimb.type}</td>
            <td>${reimb.description}</td>
            <td>$${reimb.amount}</td>
            <td>${reimb.submitted}</td>
            <td>${reimb.status}</td>
            <td>${reimb.resolver}</td>
        </tr>
    `;

    reimbTableElem.appendChild(reimbElem);

    document.getElementById("spinner-container").style.display = "none"
}

async function filterReimbs(event){
    document.getElementById("spinner-container").style.display = "block"

    event.preventDefault()

    let filter = document.getElementById("status")
    statusId = filter.value

    let remove = document.getElementsByClassName("reimb-row")

    while(remove[0]){
        remove[0].parentNode.removeChild(remove[0])
    }

    if(statusId == 0){
        getAllReimbs()
    }

    let response = await fetch(`http://localhost:9000/${user.username}/all/filter?statusId=${statusId}`)
    let responseBody = await response.json()

    let allReimbs = responseBody.data

    allReimbs.forEach(reimbursement => {
        createReimb(reimbursement)
    });

    document.getElementById("spinner-container").style.display = "none"
}

async function updateStatus(event){
    event.preventDefault()

    let updateReimbId = document.getElementById("reimb-id").value
    let newReimbStatus = getStatus()

    if(newReimbStatus != null && updateReimbId != null){
        if(newReimbStatus == 2){
            let response = await fetch(`http://localhost:9000/${user.username}/approve?reimbId=${updateReimbId}`, {
                method: "PATCH",
            })

            let responseBody = await response.json()

            if(responseBody.success == true){
                location.reload()
            }
            if(responseBody.success == false){
                let errorElem = document.getElementById("error")
        
                errorElem.innerHTML = 
                `<div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <strong>ERROR: </strong><span>` + responseBody.message + `</span>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>`
            }

        } else{
            let response1 = await fetch(`http://localhost:9000/${user.username}/deny?reimbId=${updateReimbId}`, {
                method: "PATCH",
            })

            let responseBody1 = await response1.json()

            if(responseBody1.success == true){
                location.reload()
            }else if(responseBody1.success == false){
                let errorElem = document.getElementById("error")
        
                errorElem.innerHTML = 
                `<div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <strong>ERROR: </strong><span>` + responseBody1.message + `</span>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>`
            }
        }
    }
}

async function newReimb(event){
    event.preventDefault()

    let newReimbTypeInput = getType()
    let newReimbDescInput = document.getElementById("description")
    let newReimbAmountInput = document.getElementById("amount")

    let reimb = { 
        author: user.id,
        type: newReimbTypeInput, 
        description: newReimbDescInput.value, 
        amount: newReimbAmountInput.value, 
    }

    let response = await fetch(`http://localhost:9000/${user.username}/new`, {
        method: "POST",
        body: JSON.stringify(reimb)
    })

    let responseBody = await response.json()

    location.reload()
}

function getStatus() {
    var ele = document.getElementsByName("inlineRadioOptions");
      
    for(i = 0; i < ele.length; i++) {
        if(ele[i].type="radio") {
            if(ele[i].checked)
                return ele[i].value
        }
    }
}

function getType() {
    var ele = document.getElementsByName("inlineRadioOptions1");
      
    for(i = 0; i < ele.length; i++) {
        if(ele[i].type="radio") {
            if(ele[i].checked)
                return ele[i].value
        }
    }
}