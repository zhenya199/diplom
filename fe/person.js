document.addEventListener('DOMContentLoaded', async () => {
    const token = localStorage.getItem('access_token');
    if (!token) {
        window.location.href = '/login';
        return;
    }

    const photoGrid = document.getElementById('photoGrid');
    const showMoreBtn = document.getElementById('showMoreBtn');
    const noPhotosMessage = document.getElementById('noPhotosMessage');
    const modal = document.getElementById('imageModal');
    const closeModalBtn = document.getElementById('closeModal');
    const modalImage = document.getElementById('modalImage');
    const modalAuthor = document.getElementById('modalAuthor');
    const modalDescription = document.getElementById('modalDescription');
    const modalPlace = document.getElementById('modalPlace');
    const modalLikes = document.getElementById('modalLikes');
    const modalComments = document.getElementById('modalComments');
    const authorActions = document.getElementById('authorActions');
    const editDescriptionBtn = document.getElementById('editDescriptionBtn');
    const deletePhotoBtn = document.getElementById('deletePhotoBtn');
    const editModal = document.getElementById('editModal');
    const closeEditModalBtn = document.getElementById('closeEditModal');
    const saveDescriptionBtn = document.getElementById('saveDescriptionBtn');
    const editDescriptionInput = document.getElementById('editDescriptionInput');

    let allPhotos = [];
    let userData = null
    let visiblePhotos = 4;
    let currentPhotoId = null;
    let currentPhotoAuthor = null;

    try {
        const response = await fetch('http://localhost:8081/person/account', {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (response.status === 401) {
            alert("Авторизуйтесь");
            window.location.href = '/login';
            return;
        }

        userData = await response.json();
        allPhotos = userData.images || [];

        updateGallery();

    } catch (error) {
        alert(error.message);
    }

    photoGrid.addEventListener('click', async (event) => {
        const photoItem = event.target.closest('.photo-item');
        if (photoItem) {
            const photoId = photoItem.dataset.id;
            if (!photoId) return;

            currentPhotoId = photoId;

            try {
                const response = await fetch(`http://localhost:8081/image/${photoId}`, {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });

                if (!response.ok) throw new Error('Ошибка загрузки данных');

                if(response == 401) {
                    throw new Error("Необходимо атворизоваться")
                }

                const photoDetails = await response.json();
                console.log(photoDetails);
                currentPhotoAuthor = photoDetails.authorUsername; // Сохраняем автора фотографии
                console.error(photoDetails);
                modalImage.src = photoDetails.pathToFile;
                modalAuthor.textContent = `Автор: ${photoDetails.authorUsername}`;
                modalPlace.textContent = `Место: ${photoDetails.place?.name || 'Не указано'}`;
                modalDescription.textContent = `Описание: ${photoDetails.description || "Нет описания"}`;
                // Обработка лайков
                if (photoDetails.likes && photoDetails.likes.length > 0) {
                    modalLikes.textContent = `Лайки: ${photoDetails.likes.length}`;
                } else {
                    modalLikes.textContent = "Лайков пока нет";
                }

                if (photoDetails.comments && photoDetails.comments.length > 0) {
                    modalComments.innerHTML = photoDetails.comments.map(comment => `
                        <div class="comment-item">
                            <div class="comment-author">${comment.usernameAuthor}</div>
                            <div class="comment-text">${comment.body}</div>
                        </div>
                    `).join('');
                } else {
                    modalComments.innerHTML = `<p>Комментариев пока нет</p>`;
                }

                const currentUser = userData.username;
                if (currentUser === currentPhotoAuthor) {
                    authorActions.style.display = 'flex';
                } else {
                    authorActions.style.display = 'none';
                }

                modal.style.display = 'flex';
            } catch (error) {
                alert('Ошибка при загрузке деталей фотографии');
                console.error(error);
            }
        }
    });

    editDescriptionBtn.addEventListener('click', () => {
        editModal.style.display = 'flex';
    });

    closeEditModalBtn.addEventListener('click', () => {
        editModal.style.display = 'none';
    });

    saveDescriptionBtn.addEventListener('click', async () => {
        const newDescription = editDescriptionInput.value.trim();
        if (!newDescription) {
            alert('Введите описание');
            return;
        }

        try {
            const response = await fetch(`http://localhost:8081/image/${currentPhotoId}`, {
                method: 'PATCH',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                },
                body: newDescription
            });

            if (!response.ok) throw new Error('Ошибка обновления описания');

            alert('Описание успешно обновлено');
            editModal.style.display = 'none';
            // Можно обновить данные на странице, если нужно
        } catch (error) {
            alert('Ошибка при обновлении описания');
            console.error(error);
        }
    });

    // Удаление фотографии
    deletePhotoBtn.addEventListener('click', async () => {
        const confirmDelete = confirm('Вы уверены, что хотите удалить эту фотографию?');
        if (!confirmDelete) return;

        try {
            const response = await fetch(`http://localhost:8081/image/${currentPhotoId}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });

            if (!response.ok) throw new Error('Ошибка удаления фотографии');

            alert('Фотография успешно удалена');
            modal.style.display = 'none';
            // Можно обновить данные на странице, если нужно
        } catch (error) {
            alert('Ошибка при удалении фотографии');
            console.error(error);
        }
    });

    // Закрытие модального окна
    closeModalBtn.addEventListener('click', () => {
        modal.style.display = 'none';
    });

    // Закрытие модального окна при клике вне его
    window.addEventListener('click', (event) => {
        if (event.target === modal) {
            modal.style.display = 'none';
        }
        if (event.target === editModal) {
            editModal.style.display = 'none';
        }
    });

    function getAllPhoto() {
        window.location.href = 'pages/myImages.html';
    }

    function updateGallery() {
        photoGrid.innerHTML = allPhotos.slice(0, visiblePhotos).map(photo => `
            <div class="photo-item" data-id="${photo.id}">
                <img src="${photo.pathToFile}" alt="Моя фотография">
            </div>
        `).join('');

        showMoreBtn.style.display = visiblePhotos >= allPhotos.length ? 'none' : 'block';
    }

    showMoreBtn.addEventListener('click', () => {
        visiblePhotos += 4;
        updateGallery();
    });
});