let user;

window.onload = async function(){
    let response = await fetch("http://localhost:9000/check-session")
    let responseBody = await response.json()

    user = responseBody.data

    if(!responseBody.success){
        window.location = "../"
    }

    if(user.role == 2){
        window.location = "../fm/allreimbs"
    }

    let welcomeMessage = document.getElementById("dashboard")
    welcomeMessage.innerText = "Welcome, " + user.firstName + "!"

    getAllReimbs()
}

async function getAllReimbs(){
    let response = await fetch(`http://localhost:9000/employee/dashboard?userId=${user.id}`)
    let responseBody = await response.json()

    let allReimbs = responseBody.data

    allReimbs.forEach(reimbursement => {
        createReimb(reimbursement)
    });
}

function createReimb(reimb){
    document.getElementById("spinner-container").style.display = "block"

    let reimbTableElem = document.getElementById("reimbs");

    let reimbElem = document.createElement("tr");
    reimbElem.className = "reimb-row";

    reimb.amount = parseFloat(reimb.amount).toFixed(2)

    reimb.submitted = new Date(reimb.submitted).toDateString();

    if(reimb.resolver == null){
        reimb.resolver = "N/A"
    }

    if(reimb.resolved != null){
        reimb.resolved = new Date(reimb.resolved).toDateString()
    } else{
        reimb.resolved = "N/A"
    };

    reimbElem.innerHTML = 
    `
        <tr>
            <td>${reimb.id}</td>
            <td>${reimb.type}</td>
            <td>${reimb.description}</td>
            <td>$${reimb.amount}</td>
            <td>${reimb.submitted}</td>
            <td>${reimb.status}</td>
            <td>${reimb.resolver}</td>
            <td>${reimb.resolved}</td>
        </tr>
    `;

    reimbTableElem.appendChild(reimbElem);

    document.getElementById("spinner-container").style.display = "none"
}

async function newReimb(event){
    event.preventDefault()

    let newReimbTypeInput = displayRadioValue()
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

    location.reload()
}

function displayRadioValue() {
    var ele = document.getElementsByClassName('form-check-input');
      
    for(i = 0; i < ele.length; i++) {
        if(ele[i].type="radio") {
            if(ele[i].checked)
                return ele[i].value
        }
    }
}