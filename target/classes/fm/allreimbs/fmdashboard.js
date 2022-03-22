let userId;
let username;
let statusId = 0;

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
    let welcomeMessage = document.getElementById("dashboard")
    welcomeMessage.innerText = `Welcome to the Control Panel, ${username}!`
}

async function getUserByID(idnumber){
    let response = await fetch(`http://localhost:9000/user?userId=${idnumber}`)
    let responseBody = await response.json()

    return Promise.resolve(responseBody.data.username)
}

async function getAllReimbs(){
    let response = await fetch(`http://localhost:9000/${username}/all`)
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

    let author = await getUserByID(reimb.author)
    reimb.author = author

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

    reimb.amount = parseFloat(reimb.amount).toFixed(2);

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
            x = rows[i].getElementsByTagName("td")[1]
            y = rows[i + 1].getElementsByTagName("td")[1]

            if (x.innerHTML > y.innerHTML){
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

async function filterReimbs(event){
    event.preventDefault()

    let filter = document.getElementById("status")
    statusId = filter.value

    let remove = document.getElementsByClassName("reimb-row")

    while(remove[0]){
        remove[0].parentNode.removeChild(remove[0])
    }

    console.log(statusId)

    if(statusId == 0){
        getAllReimbs()
    }

    let response = await fetch(`http://localhost:9000/${username}/all/filter?statusId=${statusId}`)
    let responseBody = await response.json()

    let allReimbs = responseBody.data

    allReimbs.forEach(reimbursement => {
        createReimb(reimbursement)
    });
}

async function updateStatus(event){
    event.preventDefault()

    let updateReimbId = document.getElementById("reimb-id").value
    let newReimbStatus = getStatus()

    if(newReimbStatus != null && updateReimbId != null){
        if(newReimbStatus == 2){
            let response = await fetch(`http://localhost:9000/${username}/approve?reimbId=${updateReimbId}`, {
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
            let response1 = await fetch(`http://localhost:9000/${username}/deny?reimbId=${updateReimbId}`, {
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