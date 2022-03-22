let userId;
let username;

window.onload = function(){
    const params = new Proxy(new URLSearchParams(window.location.search), {
        get: (searchParams, prop) => searchParams.get(prop),
    });

    userId = params.userId; 
    username = params.username;

    getCurrentUserInfo()
    getAllReimbs()
}

async function getCurrentUserInfo(){
    let response = await fetch(`http://localhost:9000/user?userId=${userId}`)
    let responseBody = await response.json()
    
    let welcomeMessage = document.getElementById("dashboard")
    welcomeMessage.innerText = "Welcome, " + responseBody.data.username + "!"
}

async function getUserByID(idnumber){
    let response = await fetch(`http://localhost:9000/user?userId=${idnumber}`)
    let responseBody = await response.json()

    return Promise.resolve(responseBody.data.username)
}

async function getAllReimbs(){
    let response = await fetch(`http://localhost:9000/employee/dashboard?userId=${userId}`)
    let responseBody = await response.json()

    let allReimbs = responseBody.data

    allReimbs.forEach(reimbursement => {
        createReimb(reimbursement)
    });
}

async function createReimb(reimb){
    let reimbTableElem = document.getElementById("reimbs");

    let reimbElem = document.createElement("tr");
    reimbElem.className = "reimb-row";

    switch(reimb.type){
        case 1:
            reimb.type = "Lodging"
            break
        case 2:
            reimb.type = "Food"
            break
        case 3:
            reimb.type = "Travel"
            break
    }

    reimb.amount = parseFloat(reimb.amount).toFixed(2)

    reimb.submitted = new Date(reimb.submitted).toDateString();

    switch(reimb.status){
        case 1:
            reimb.status = "Pending"
            break
        case 2:
            reimb.status = "Approved"
            break
        case 3:
            reimb.status = "Denied"
            break
    }

    if(reimb.resolver != 0){
        let resolver = await getUserByID(reimb.resolver)
        reimb.resolver = resolver
    } else{
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
    sortTable()
}

function sortTable() {
    var table, rows, switching, i, x, y, shouldSwitch
    table = document.getElementById("reimbs")
    switching = true

    while(switching){
        switching = false
        rows = table.rows

        for(i = 1; i < (rows.length - 1); i++) {
            shouldSwitch = false;
            x = rows[i].getElementsByTagName("td")[4]
            y = rows[i + 1].getElementsByTagName("td")[4]

            if (Number(x.innerHTML) > Number(y.innerHTML)){
                shouldSwitch = true
                break
            }
        }

        if (shouldSwitch){
            rows[i].parentNode.insertBefore(rows[i + 1], rows[i])
            switching = true
        }
    }
}

async function newReimb(event){
    event.preventDefault()

    let newReimbTypeInput = displayRadioValue()
    let newReimbDescInput = document.getElementById("description")
    let newReimbAmountInput = document.getElementById("amount")

    let reimb = { 
        author: userId,
        type: newReimbTypeInput, 
        description: newReimbDescInput.value, 
        amount: newReimbAmountInput.value, 
    }

    let response = await fetch(`http://localhost:9000/${username}/new`, {
        method: "POST",
        body: JSON.stringify(reimb)
    })

    let responseBody = await response.json()

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