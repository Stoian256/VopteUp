// script.js

checkCookieExpired()
const userId = getCookieValue("userId");
if (userId != null) {
    const apiUrl = `http://localhost:8085/voteup/api/checkValidated?userId=${encodeURIComponent(userId)}`;
// Face un request către backend pentru a obține informațiile utilizatorului
    fetch(apiUrl)
        .then(response => response.json())
        .then(data => {
            if (data === true) {
                document.getElementById('verification-form').style.display = 'none';
                verificationResult.textContent = 'Identitate validata. Datele introduse la inregistrare sunt confirmate.';
                document.getElementById('vote-button').disabled = false;
            }
        });
}
const logoutLink = document.getElementById("logout-link");
window.addEventListener('pageshow', function () {
    checkCookieExpired()
});
// Adăuga un eveniment de click pe link-ul de logout
logoutLink.addEventListener("click", function (event) {
    event.preventDefault();
    // Șterge cookie-ul cu numele "userId"
    deleteCookie("userId");
    // Redirecționeaza utilizatorul către pagina de login
    window.location.href = "login.html";
});

function deleteCookie(name) {
    document.cookie = name + "=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
}

document.getElementById('document-upload').addEventListener('change', function (event) {
    document.getElementById('upload-label').style.display = 'none';
    var file = event.target.files[0];
    var previewContainer = document.getElementById('preview-container');
    var previewImage = document.createElement('img');
    previewImage.id = 'uploaded-image-preview';

    var reader = new FileReader();
    reader.onload = function () {
        previewImage.src = reader.result.toString();
        previewContainer.appendChild(previewImage);
        previewContainer.style.display = 'block';
    };

    reader.readAsDataURL(file);
});


function isCookieExpired(cookieName) {
    const cookies = document.cookie.split('; ');

    for (let i = 0; i < cookies.length; i++) {
        const cookie = cookies[i].split('=');
        if (cookie[0] === cookieName) {
            return false;
        }
    }

    return true; // Returnează true dacă nu există cookie sau nu a fost găsită o dată de expirare
}

function checkCookieExpired() {
    // Verifica starea de autentificare
    if (!isCookieExpired('userId')) {
        // Utilizatorul este autentificat și sesiunea nu a expirat
        console.log('Utilizatorul este autentificat');
    } else {
        // Utilizatorul nu este autentificat sau sesiunea a expirat, redirecționați către pagina de autentificare
        console.log('Utilizatorul nu este autentificat sau sesiunea a expirat, redirecționare către pagina de autentificare');
        window.location.href = 'login.html';
    }
}

function getCookieValue(cookieName) {
    const cookies = document.cookie.split('; ');

    for (let i = 0; i < cookies.length; i++) {
        const cookie = cookies[i].split('=');
        if (cookie[0] === cookieName) {
            return cookie[1];
        }
    }

    return null;
}

const intervalCookie = setInterval(checkCookieExpired, 10 * 60 * 1000); // 10 minute în milisecunde

// Oprește verificarea automată când utilizatorul părăsește pagina
window.addEventListener('beforeunload', () => {
    //clearInterval(interval);
    clearInterval(intervalCookie);
});


const verificationForm = document.getElementById('verification-form');
const documentUpload = document.getElementById('document-upload');
const verificationResult = document.getElementById('verification-result');

verificationForm.addEventListener('submit', function (e) {
    e.preventDefault();

    const file = documentUpload.files[0];
    const formData = new FormData();
    formData.append('document', file);

    const userId = getCookieValue("userId");
    console.log(userId)
    fetch('http://localhost:8085/voteup/api/verify', {
        method: 'POST',
        body: formData,
        headers: {
            'Authorization': 'Bearer ' + userId
        }
    }).then(response => response.text())
        .then(data => {
            console.log(data)
            if (data === 'Identitate validata!') {
                // Ascunde formularul de încărcare a imaginii
                document.getElementById('verification-form').style.display = 'none';
                verificationResult.textContent = 'Identitate validata. Datele introduse la inregistrare sunt confirmate.';
            } else {
                alert(data);
                location.reload();
            }
        })
        .catch(error => {
            console.error(error);
            verificationResult.textContent = 'A aparut o eroare in timpul verificarii documentului.';
        });
});


// Obține evenimentele de votare din backend
fetch('http://localhost:8085/voteup/api/voting-events')
    .then(response => response.json())
    .then(data => {
        // Verific dacă există evenimente de votare
        if (data.length > 0) {
            const voteForm = document.getElementById('vote-form');
            const eventSelect = document.getElementById('event-select');

            const option = document.createElement('option');
            option.value = "";
            option.textContent = "Selecteaza un eveniment";
            eventSelect.appendChild(option);
            // Adăuga opțiuni pentru evenimentele de votare
            data.forEach(event => {
                const option = document.createElement('option');
                option.value = event.id;
                option.textContent = event.name;
                eventSelect.appendChild(option);
            });

            const optionContainer = document.getElementById('vote-options');


            // Manipuleaza evenimentul de schimbare a selectului de evenimente
            eventSelect.addEventListener('change', function () {
                const selectedEventId = this.value;
                if (selectedEventId !== "") {
                    const apiUrl = `http://localhost:8085/voteup/api/voting-options-event?eventId=${selectedEventId}`;
                    fetch(apiUrl)
                        .then(response => response.json())
                        .then(data => {
                            // Goleste containerul opțiunilor de vot
                            optionContainer.innerHTML = '';

                            // Adăuga opțiuni de vot
                            data.forEach(option => {
                                const optionInput = document.createElement('input');
                                optionInput.type = 'radio';
                                optionInput.name = 'vote-option';
                                optionInput.value = option.id;

                                const optionLabel = document.createElement('label');
                                optionLabel.textContent = option.name;

                                optionContainer.appendChild(optionInput);
                                optionContainer.appendChild(optionLabel);
                                optionContainer.appendChild(document.createElement('br'));
                            });
                        })
                        .catch(error => {
                            console.error(error);
                            optionContainer.innerHTML = 'A aparut o eroare in timpul obtinerii optiunilor de vot.';
                        });
                }
            });

            // Manipuleaza evenimentul de submit al formularului de vot
            voteForm.addEventListener('submit', function (e) {
                e.preventDefault();

                const selectedOption = document.querySelector('input[name="vote-option"]:checked');
                if (!selectedOption) {
                    alert('Selectati o optiune de vot.');
                    return;
                }

                const optionId = selectedOption.value;
                const eventId = eventSelect.value;
                const userId = getCookieValue("userId");
                fetch('http://localhost:8085/voteup/api/submit-vote', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({userId, eventId, optionId})
                })
                    .then(response => {
                        if (response.ok) {
                            return response.text();
                        } else {
                            alert('A aparut o eroare in timpul inregistrarii votului.');
                        }
                    })
                    .then(result => {
                        alert(result);
                    })
                    .catch(error => {
                        console.error(error);
                        alert('A aparut o eroare in timpul inregistrarii votului.');
                    });
            });
        } else {
            // Dacă nu există evenimente de votare disponibile
            const message = document.createElement('p');
            message.textContent = 'Nu exista evenimente de votare disponibile in prezent.';
            document.body.appendChild(message);
        }
    })
    .catch(error => {
        const message = document.createElement('p');
        message.textContent = 'A aparut o eroare in timpul obtinerii evenimentelor de votare.';
        document.body.appendChild(message);
    });
