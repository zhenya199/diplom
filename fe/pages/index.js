document.addEventListener('DOMContentLoaded', () => {
    const registrationForm = document.getElementById('registrationForm');
    const loginForm = document.getElementById('loginForm');
    const imageForm = document.getElementById('imageForm');

    if (imageForm) {
        imageForm.addEventListener('submit', (event) => {
            event.preventDefault();
            saveImage(imageForm);
        });
    }

    if (registrationForm) {
        registrationForm.addEventListener('submit', (event) => {
            event.preventDefault();
            handleRegistration(registrationForm);
        });
    }

    if (loginForm) {
        loginForm.addEventListener('submit', (event) => {
            event.preventDefault();
            handleLogin(loginForm);
        });
    }
});

async function handleRegistration(form) {
    const formData = new FormData(form);
    console.log(formData);

    try {
        const response = await axios.post(
            'http://localhost:8081/auth/registration',
            formData,
            { headers: { 'Content-Type': 'multipart/form-data' } }
        );

        console.log('Registration Success:', response.data);
        alert('Вы успешно зарегистрированы!');
        window.location.href = './login.html';
    } catch (error) {
        const status = error.response ? error.response.status : 'Неизвестная ошибка';
        handleError(error, 'Произошла ошибка при регистрации', status);
    }
}

async function handleLogin(form) {
    const formData = new FormData(form);

    const data = {};
    formData.forEach((value, key) => {
        data[key] = value;
    });

    try {
        const response = await axios.post('http://localhost:8081/auth/login', JSON.stringify(data), {
            headers: { 'Content-Type': 'application/json' }
        });

        const { 'access_token': accessToken, 'refresh_token': refreshToken } = response.data;
        localStorage.setItem('access_token', accessToken);
        localStorage.setItem('refresh_token', refreshToken);
        window.location.href = 'home.html';
    } catch (error) {
        const status = error.response ? error.response.status : 'Неизвестная ошибка';
        handleError(error, 'Ошибка авторизации! Проверьте ваши данные.', status);
    }
}

function handleError(error, defaultMessage, status, retryFunction) {

     if(status === 401) {
         updateTokens();
         if (retryFunction) {
             retryFunction();
         }
        }

    else if (error.response) {
        alert(`Ошибка: ${error.response.status} - ${error.response.data.message || defaultMessage}`);
        console.error('Error Response:', error.response);
    } else if (error.request) {
        alert('Сервер не отвечает. Проверьте подключение к интернету.');
        console.error('Error Request:', error.request);
    }
    else {
        alert('error: ' + error.message);
        console.error('Error Message:', error.message);
    }

}

function updateTokens() {
    const refreshToken = localStorage.getItem( 'refresh_token');
    try {
        const response = axios.post(
            'http://localhost:8081/auth/refresh', {
                headers: {"Authorization": `Bearer ${refreshToken}`}
            });

        const {'access_token': newAccessToken, 'refresh_token': newRefreshToken} = response.data;
        localStorage.setItem('access_token', newAccessToken);
        localStorage.setItem('refresh_token', newRefreshToken);
    } catch (error) {
       window.location.href = '/login.html';
    }

}

function previewImage(event) {
    const preview = document.getElementById('preview');
    const file = event.target.files[0];
    const uploadBtn = document.querySelector('.upload-btn');

    if (file) {
        const reader = new FileReader();
        reader.onload = function(e) {
            preview.src = e.target.result;
            preview.style.display = 'block';
            uploadBtn.style.display = 'none';
        };
        reader.readAsDataURL(file);
    }
}

function getPerson() {
    const token = localStorage.getItem('access_token');

    try {
        const response = axios.get(
            'http://localhost:8081/person/account',
            {
                headers: { Authorization: `Bearer ${token}` }
            }
        );
        const status = response.status;



        if(status === 401) {
            alert("Произошла ошибка! Попробуйте атворизоваться еще раз")
        }
    }catch(error) {

    }

}

async function saveImage(form) {
    try {
        const formData = new FormData(form);

        const placeId = Number(localStorage.getItem("placeId"));
        const response = await axios.post(
            'http://localhost:8081/image/',
            formData,
            {
                headers: {
                    'Content-Type': 'multipart/form-data',
                    'Authorization': 'Bearer ' + localStorage.getItem('access_token'),
                },
                params: { placeId },
            }
        );

        const status =  response.status;

        if(status === 200) {
            alert("Фотография успешно сохранена");
        }

    } catch (error) {
        const status = error.response ? error.response.status : 'Неизвестная ошибка';
        alert('save image error: ' + status);
        handleError(error, 'Проблема сохранения фотографии', status, () => saveImage(form));
    }
}

function setupOutsideClickHandler(containerId) {
    document.addEventListener('click', (event) => {
        const container = document.getElementById(containerId);
        if (container.style.display === 'block' && !container.contains(event.target)) {
            container.style.display = 'none';
        }
    });
}

function updatePlaceContainer(places, containerId, onPlaceClick) {
    const container = document.getElementById(containerId);
    container.innerHTML = '';
    container.style.display = 'block';

    places.forEach(place => {
        if (!place.name || !place.typeOfPlace) return;

        const placeElement = createPlaceElement(place, onPlaceClick);
        container.appendChild(placeElement);
    });
}

function findLocation() {
    const placeName = document.getElementById('location').value;

    return axios.get('http://localhost:8081/place/find', {
        params: { cityName: placeName }
    }).then(response => {
        const places = response.data;
        if (places && places.length > 0) {
            updatePlaceContainer(places, 'cityContainer', handlePlaceSelection);
            setupOutsideClickHandler('cityContainer');
            return places;
        } else {
           alert("Такого места не найдено.")
        }
    });
}

async function handlePlaceSelection(place) {
    const locationInput = document.getElementById('location');
    const selectedPlaceInput = document.getElementById('selectedPlaceId');
    const savedPlace = await savePlace(place);
    console.log("savedPlace.id:", savedPlace.id, typeof savedPlace.id);
    if (typeof savedPlace.id === "number") {
        const placeId = savedPlace.id?.toString() || savedPlace.id;
        localStorage.setItem("placeId", placeId);
    } else {
        console.error("placeId is not a number!");
    }
    locationInput.value = `${place.name} (${place.typeOfPlace})`;

    if (selectedPlaceInput) {
        selectedPlaceInput.value = place;
    }
    const placeContainer = document.getElementById('cityContainer');
    placeContainer.style.display = 'none';
}


function createPlaceElement(place, onClick) {
    const placeDiv = document.createElement('div');
    placeDiv.classList.add('place-item');

    placeDiv.innerHTML = `
        <h3>${place.name}</h3>
        <p>Type: ${place.typeOfPlace}</p>
    `;

    placeDiv.addEventListener('click', () => {
        onClick(place);
    });

    return placeDiv;
}

async function savePlace(place) {
    try {
        if (!place || typeof place !== 'object') {
            throw new Error('Place data is invalid or missing.');
        }

        if (!place.name || !place.typeOfPlace) {
            throw new Error('Invalid place data: name and typeOfPlace are required.');
        }

        const response = await axios.post(
            'http://localhost:8081/place/',
            JSON.stringify(place), // Данные в формате JSON
            {
                headers: { 'Content-Type': 'application/json' } // Указываем тип данных
            }
        );
        return response.data;
    } catch (error) {
        handleError(error, 'Ошибка сохранения места');
        throw error;
    }
}