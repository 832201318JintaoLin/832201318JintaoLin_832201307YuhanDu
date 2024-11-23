function showModal(mode, id) {
    const modal = document.getElementById('modal');
    modal.style.display = "block";
    document.getElementById('submit').onclick = mode === 'add' ? addEntry : updateEntry;

    if (mode === 'edit') {
        document.getElementById('entryId').value = id;
        document.getElementById('username').value = '';
        document.getElementById('phone').value = '';
        document.getElementById('phone1').value = '';
        document.getElementById('email').value = '';
        document.getElementById('socialmedia').value = '';
        document.getElementById('address').value = '';
        document.getElementById('previewImage').src = ''; // 清除预览图片
        document.getElementById('previewImage').style.display = 'none';
    }
}

window.onclick = function (event) {
    const modal = document.getElementById('modal');
    if (event.target == modal) {
        modal.style.display = "none";
        clearInputs();
    }
};

document.querySelector('.close').onclick = function () {
    const modal = document.getElementById('modal');
    modal.style.display = "none";
    clearInputs();
};

function clearInputs() {
    document.getElementById('username').value = '';
    document.getElementById('phone').value = '';
    document.getElementById('phone1').value = '';
    document.getElementById('email').value = '';
    document.getElementById('socialmedia').value = '';
    document.getElementById('address').value = '';
    document.getElementById('previewImage').src = ''; // 清除预览图片
    document.getElementById('previewImage').style.display = 'none';
    document.getElementById('imageInput').value = '';
}


document.getElementById('imageInput').addEventListener('change', function (event) {
    const file = event.target.files[0];
    if (file) {
        const reader = new FileReader();
        reader.onloadend = function () {
            document.getElementById('previewImage').src = reader.result;
            document.getElementById('previewImage').style.display = 'block';
        };
        reader.readAsDataURL(file);
    }
});


async function uploadImage(file) {
    const formData = new FormData();
    formData.append('file', file);

    try {
        const response = await fetch('http://localhost:8888/uploadImage', {
            method: 'POST',
            body: formData
        });

        return await response.text();
    } catch (error) {
        console.error('Error uploading image:', error);
        alert('上传图片失败，请重试');
        return null;
    }
}

async function addEntry() {
    const username = document.getElementById('username').value.trim();
    const phone = document.getElementById('phone').value.trim();
    const phone1 = document.getElementById('phone1').value.trim();
    const email = document.getElementById('email').value.trim();
    const socialmedia = document.getElementById('socialmedia').value.trim();
    const address = document.getElementById('address').value.trim();
    const imageFile = document.getElementById('imageInput').files[0];
    if (!username || !phone) {
        alert("姓名和电话号码不能为空");
        return;
    }

    let image = '';
    if (imageFile) {
        image = await uploadImage(imageFile);
    }
    console.log()
    fetch('http://localhost:8888/add', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({username, phone, phone1, email, socialmedia, address, image})
    })
        .then(loadPhoneBook)
        .catch(console.error);
    modal.style.display = "none";
    clearInputs();
}

function loadPhoneBook() {
    fetch('http://localhost:8888/list')
        .then(response => response.json())
        .then(data => displayData(data))
        .catch(console.error);
}

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

        const deleteButton = document.createElement('button');
        deleteButton.id = 'deleteButton';
        deleteButton.textContent = '删除';
        deleteButton.onclick = () => deleteEntry(entry.id);

        const editButton = document.createElement('button');
        editButton.id = 'editButton';
        editButton.textContent = '编辑';

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

        editButton.onclick = () => {
            showModal('edit', entry.id);
            document.getElementById('username').value = entry.username;
            document.getElementById('phone').value = entry.phone;
            document.getElementById('phone1').value = entry.phone1;
            document.getElementById('email').value = entry.email;
            document.getElementById('socialmedia').value = entry.socialmedia;
            document.getElementById('address').value = entry.address;
            document.getElementById('previewImage').src = entry.image || ''; // 显示已有的图片
            document.getElementById('previewImage').style.display = entry.image ? 'block' : 'none';
        };

        actionCell.appendChild(deleteButton);
        actionCell.appendChild(editButton);
        actionCell.appendChild(collectButton);

        row.appendChild(usernameCell);
        row.appendChild(phoneCell);
        row.appendChild(actionCell);

        tbody.appendChild(row);
    });
}

function deleteEntry(id) {
    if (confirm("确定要删除此条目吗?")) {
        fetch(`http://localhost:8888/delete?id=${id}`, {method: 'DELETE'})
            .then(loadPhoneBook)
            .catch(console.error);
    }
}

async function updateEntry() {
    const username = document.getElementById('username').value.trim();
    const phone = document.getElementById('phone').value.trim();
    const entryId = document.getElementById('entryId').value;
    const phone1 = document.getElementById('phone1').value.trim();
    const email = document.getElementById('email').value.trim();
    const socialmedia = document.getElementById('socialmedia').value.trim();
    const address = document.getElementById('address').value.trim();
    const imageFile = document.getElementById('imageInput').files[0];
    if (!username || !phone) {
        alert("姓名和电话号码不能为空");
        return;
    }
    let image = '';
    if (imageFile) {
        image = await uploadImage(imageFile);
    } else {
        image = document.getElementById('previewImage').src;
    }

    fetch('http://localhost:8888/update', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({id: entryId, username, phone, phone1, email, socialmedia, address, image})
    })
        .then(loadPhoneBook)
        .catch(console.error);
    modal.style.display = "none";
    clearInputs();
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
        .then(loadPhoneBook)
        .catch(console.error);

}


loadPhoneBook();


function loadMyCollect(){
    fetch('http://localhost:8888/collectList')
        .then(response => response.json())
        .then(data => displayData(data))
        .catch(console.error);
}
