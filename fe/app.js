document.addEventListener('DOMContentLoaded', function() {
    const urlParams = new URLSearchParams(window.location.search);
    const imageId = urlParams.get('id');
    const container = document.querySelector('.container');

    if (!imageId) {
        showError('Не указан ID фотографии');
        return;
    }

    // Показ загрузки
    container.innerHTML = '<div class="loading">Загрузка...</div>';

    fetch(`http://localhost:8081/image/${imageId}`)
        .then(response => {
            if (!response.ok) throw new Error('Фото не найдено');
            return response.json();
        })
        .then(data => {
            container.innerHTML = document.querySelector('template').innerHTML;
            updatePageContent(data);
        })
        .catch(error => {
            showError(error.message);
        });

    document.querySelector('.back-btn')?.addEventListener('click', function(e) {
        e.preventDefault();
        window.history.back();
    });
});

function updatePageContent(data) {
    // Основное изображение
    document.querySelector('.left-panel img').src = data.pathToFile;

    // Заголовок
    document.querySelector('.photo-title').textContent = `Фото от ${data.authorUsername}`;

    // Информация
    const photoInfo = document.querySelector('.photo-info');
    photoInfo.innerHTML = `
        <div class="info-item">
            <div class="info-label">Местоположение</div>
            <div class="info-content">${data.place?.name || 'Не указано'}</div>
        </div>
        <div class="info-item">
            <div class="info-label">Дата публикации</div>
            <div class="info-content">${new Date(data.createdAt).toLocaleDateString()}</div>
        </div>
        <div class="info-item">
            <div class="info-label">Лайки</div>
            <div class="info-content">${data.likes.length}</div>
        </div>
    `;

    // Комментарии
    const commentsSection = document.querySelector('.comments-section');
    commentsSection.innerHTML = `
        <h3>Комментарии (${data.comments.length})</h3>
        ${data.comments.map(comment => `
            <div class="comment">
                <div class="comment-author">${comment.authorUsername}</div>
                <div class="comment-text">${comment.text}</div>
            </div>
        `).join('')}
    `;
}

function showError(message) {
    const errorDiv = document.createElement('div');
    errorDiv.className = 'error-message';
    errorDiv.textContent = message;
    document.body.prepend(errorDiv);
}