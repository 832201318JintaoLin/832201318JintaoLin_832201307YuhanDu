function displayData(data) {
    const tbody = document.getElementById('phonebookTbody');
    tbody.innerHTML = ''; // Clear previous data
    data.forEach(entry => {
        const row = document.createElement('tr');
        const usernameCell = document.createElement('td');
        const phoneCell = document.createElement('td');
        const actionCell = document.createElement('td');
        actionCell.style.maxWidth = '120px';
        usernameCell.textContent = entry.username;
        phoneCell.textContent = entry.phone;
        let collectCell = entry.collect;

        const collectButton = document.createElement('button');
        collectButton.id = 'collectButton';
        if (collectCell === 1) {
            collectButton.textContent = '加入书签';
            collectButton.classList.toggle('collect');
        } else {
            collectButton.textContent = '取消书签';
            collectButton.classList.toggle('cancelCollect');
        }
        collectButton.onclick = () => toggleBookmark(entry.id, entry.collect);


        actionCell.appendChild(collectButton);

        row.appendChild(usernameCell);
        row.appendChild(phoneCell);
        row.appendChild(actionCell);

        tbody.appendChild(row);
    });
}




function toggleBookmark(id, currentStatus) {
    const newStatus = currentStatus === 2 ? 1 : 2;
    const collectButtonText = newStatus === 2 ? '取消书签' : '加入书签';

    fetch(`http://localhost:8888/toggleBookmark`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({id, collect: newStatus})
    })
        .then(loadMyCollect)
        .catch(console.error);

}


loadMyCollect();


function loadMyCollect(){
    fetch('http://localhost:8888/collectList')
        .then(response => response.json())
        .then(data => displayData(data))
        .catch(console.error);
}
