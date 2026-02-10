const baseUrl = '/service1-spring';

// Pagination state
let currentPage = 0;
const pageSize = 10;

// Initialize
document.addEventListener('DOMContentLoaded', () => {
	loadPersons();
});

function setFormMode(mode) {
    const btnCreate = document.getElementById('btn-create');
    const btnUpdate = document.getElementById('btn-update');
    
    if (mode === 'edit') {
        btnCreate.style.display = 'none';
        btnUpdate.style.display = 'inline-block';
    } else {
        btnCreate.style.display = 'inline-block';
        btnUpdate.style.display = 'none';
    }
}

function cancelEdit() {
    document.getElementById('person-form').reset();
    document.getElementById('person-id').value = '';
    setFormMode('create');
}

function showMessage(text, type) {
    const messageDiv = document.getElementById('message');
    // Only show error messages
    if (type === 'error') {
        messageDiv.textContent = text;
        messageDiv.className = `message ${type}`;
        setTimeout(() => {
            messageDiv.className = 'message';
        }, 5000);
    }
}

function showTab(tabName) {
    document.querySelectorAll('.tab-content').forEach(tab => {
        tab.classList.remove('active');
    });
    document.querySelectorAll('.tab-button').forEach(btn => {
        btn.classList.remove('active');
    });
    
    document.getElementById(`${tabName}-tab`).classList.add('active');
    event.target.classList.add('active');
}

async function makeRequest(url, method = 'GET', body = null) {
    try {
        const options = {
            method: method,
            headers: {
                'Content-Type': 'application/xml',
                'Accept': 'application/xml'
            },
            mode: 'cors'
        };
        
        if (body) {
            options.body = body;
        }
        
        const response = await fetch(url, options);
        const text = await response.text();
        
        if (!response.ok) {
            // Try to parse error
            const parser = new DOMParser();
            const xml = parser.parseFromString(text, 'application/xml');
            const error = xml.querySelector('error');
            if (error) {
                const message = error.querySelector('message')?.textContent || 'Unknown error';
                const code = error.querySelector('code')?.textContent || response.status;
                throw new Error(`${code}: ${message}`);
            }
            throw new Error(`HTTP ${response.status}: ${text}`);
        }
        
        return { response, text };
    } catch (error) {
        console.error('Request error:', error);
        throw error;
    }
}

function xmlToPerson(xmlDoc) {
    const person = xmlDoc.querySelector('Person');
    if (!person) return null;
    
    return {
        id: person.querySelector('id')?.textContent,
        name: person.querySelector('name')?.textContent,
        coordinates: {
            x: person.querySelector('coordinates > x')?.textContent,
            y: person.querySelector('coordinates > y')?.textContent
        },
        creationDate: person.querySelector('creationDate')?.textContent,
        height: person.querySelector('height')?.textContent,
        birthday: person.querySelector('birthday')?.textContent,
        eyeColor: person.querySelector('eyeColor')?.textContent,
        nationality: person.querySelector('nationality')?.textContent,
        location: {
            x: person.querySelector('location > x')?.textContent,
            y: person.querySelector('location > y')?.textContent,
            name: person.querySelector('location > name')?.textContent
        }
    };
}

function personToXml(person, includeId = true) {
    let xml = '<person>';
    if (includeId && person.id) {
        xml += `<id>${person.id}</id>`;
    }
    xml += `<name>${escapeXml(person.name)}</name>`;
    xml += '<coordinates>';
    xml += `<x>${person.coordinates.x}</x>`;
    xml += `<y>${person.coordinates.y}</y>`;
    xml += '</coordinates>';
    if (person.birthday) {
        xml += `<birthday>${person.birthday}</birthday>`;
    }
    xml += `<height>${person.height}</height>`;
    xml += `<eyeColor>${person.eyeColor}</eyeColor>`;
    if (person.nationality) {
        xml += `<nationality>${person.nationality}</nationality>`;
    }
    xml += '<location>';
    xml += `<x>${person.location.x}</x>`;
    xml += `<y>${person.location.y}</y>`;
    xml += `<name>${escapeXml(person.location.name)}</name>`;
    xml += '</location>';
    xml += '</person>';
    return xml;
}

function escapeXml(text) {
    if (!text) return '';
    return text
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&apos;');
}

async function loadPersons() {
    try {
        const sort = document.getElementById('sortInput').value;
        const filter = document.getElementById('filterInput').value;
        
        let url = `${baseUrl}/persons?page=${currentPage}&size=${pageSize}`;
        if (sort) url += `&sort=${encodeURIComponent(sort)}`;
        if (filter) url += `&filter=${encodeURIComponent(filter)}`;
        
        const { text } = await makeRequest(url);
        const parser = new DOMParser();
        const xml = parser.parseFromString(text, 'application/xml');
        
        const persons = Array.from(xml.querySelectorAll('PersonsWrapper > persons > persons')).map(person => {
            return {
                id: person.querySelector('id')?.textContent,
                name: person.querySelector('name')?.textContent,
                coordinates: {
                    x: person.querySelector('coordinates > x')?.textContent,
                    y: person.querySelector('coordinates > y')?.textContent
                },
                creationDate: person.querySelector('creationDate')?.textContent,
                height: person.querySelector('height')?.textContent,
                birthday: person.querySelector('birthday')?.textContent,
                eyeColor: person.querySelector('eyeColor')?.textContent,
                nationality: person.querySelector('nationality')?.textContent,
                location: {
                    x: person.querySelector('location > x')?.textContent,
                    y: person.querySelector('location > y')?.textContent,
                    name: person.querySelector('location > name')?.textContent
                }
            };
        });
        
        displayPersons(persons);
        updatePaginationInfo(persons.length);
    } catch (error) {
        showMessage(`Ошибка загрузки: ${error.message}`, 'error');
    }
}

function previousPage() {
    if (currentPage > 0) {
        currentPage--;
        loadPersons();
    }
}

function nextPage() {
    currentPage++;
    loadPersons();
}

function updatePaginationInfo(itemsCount) {
    document.getElementById('pageInfo').textContent = `Страница ${currentPage + 1}`;
    document.getElementById('prevPage').disabled = currentPage === 0;
    document.getElementById('nextPage').disabled = itemsCount < pageSize;
}

function displayPersons(persons) {
    const container = document.getElementById('persons-list');
    
    if (persons.length === 0) {
        container.innerHTML = '<p>Люди не найдены</p>';
        return;
    }
    
    let html = '<table><thead><tr>';
    html += '<th>id</th><th>name</th><th>coordinates</th><th>creationDate</th><th>height</th>';
    html += '<th>birthday</th><th>eyeColor</th><th>nationality</th><th>location</th><th>Действия</th>';
    html += '</tr></thead><tbody>';
    
    persons.forEach(person => {
        html += '<tr>';
        html += `<td>${person.id}</td>`;
        html += `<td>${escapeXml(person.name)}</td>`;
        html += `<td>(${person.coordinates.x}, ${person.coordinates.y})</td>`;
        html += `<td>${person.creationDate || '-'}</td>`;
        html += `<td>${person.height}</td>`;
        html += `<td>${person.birthday || '-'}</td>`;
        html += `<td>${person.eyeColor}</td>`;
        html += `<td>${person.nationality || '-'}</td>`;
        html += `<td>${escapeXml(person.location.name)}</td>`;
        html += `<td>
            <button class="btn-edit" onclick="editPerson(${person.id})">Редактировать</button>
            <button class="btn-delete" onclick="deletePerson(${person.id})">Удалить</button>
        </td>`;
        html += '</tr>';
    });
    
    html += '</tbody></table>';
    container.innerHTML = html;
}

async function editPerson(id) {
    try {
        const { text } = await makeRequest(`${baseUrl}/persons/${id}`);
        const parser = new DOMParser();
        const xml = parser.parseFromString(text, 'application/xml');
        const person = xmlToPerson(xml);
        
        if (!person) throw new Error('Person not found');
        
        // Fill form
        document.getElementById('person-id').value = person.id;
        document.getElementById('person-name').value = person.name;
        document.getElementById('coord-x').value = person.coordinates.x;
        document.getElementById('coord-y').value = person.coordinates.y;
        document.getElementById('person-height').value = person.height;
        // Convert birthday to YYYY-MM-DD format for input type="date"
        if (person.birthday) {
            const date = new Date(person.birthday);
            const year = date.getFullYear();
            const month = String(date.getMonth() + 1).padStart(2, '0');
            const day = String(date.getDate()).padStart(2, '0');
            document.getElementById('person-birthday').value = `${year}-${month}-${day}`;
        } else {
            document.getElementById('person-birthday').value = '';
        }
        document.getElementById('person-eye-color').value = person.eyeColor;
        document.getElementById('person-nationality').value = person.nationality || '';
        document.getElementById('loc-x').value = person.location.x;
        document.getElementById('loc-y').value = person.location.y;
        document.getElementById('loc-name').value = person.location.name;
        
        setFormMode('edit');
        showTab('create');
        // Activate the create tab button
        document.querySelectorAll('.tab-button').forEach(btn => btn.classList.remove('active'));
        document.querySelectorAll('.tab-button')[1].classList.add('active');
    } catch (error) {
        console.error('Edit person error:', error);
        // Silently fail - form will just not be filled
    }
}

async function deletePerson(id) {
    if (!confirm('Вы уверены, что хотите удалить этого человека?')) return;
    
    try {
        await makeRequest(`${baseUrl}/persons/${id}`, 'DELETE');
        loadPersons();
    } catch (error) {
        showMessage(`Ошибка удаления: ${error.message}`, 'error');
    }
}

document.getElementById('person-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const person = {
        name: document.getElementById('person-name').value,
        coordinates: {
            x: parseFloat(document.getElementById('coord-x').value),
            y: parseInt(document.getElementById('coord-y').value)
        },
        height: parseFloat(document.getElementById('person-height').value),
        birthday: document.getElementById('person-birthday').value || null,
        eyeColor: document.getElementById('person-eye-color').value,
        nationality: document.getElementById('person-nationality').value || null,
        location: {
            x: parseFloat(document.getElementById('loc-x').value),
            y: parseFloat(document.getElementById('loc-y').value),
            name: document.getElementById('loc-name').value
        }
    };
    
    try {
        const xml = personToXml(person, false);
        await makeRequest(`${baseUrl}/persons`, 'POST', xml);
        document.getElementById('person-form').reset();
        setFormMode('create');
        loadPersons();
    } catch (error) {
        showMessage(`Ошибка создания: ${error.message}`, 'error');
    }
});

async function updatePerson() {
    try {
        const id = document.getElementById('person-id').value;
        if (!id) {
            showMessage('Укажите ID человека для обновления', 'error');
            return;
        }
        
        const person = {
            id: parseInt(id),
            name: document.getElementById('person-name').value,
            coordinates: {
                x: parseFloat(document.getElementById('coord-x').value),
                y: parseInt(document.getElementById('coord-y').value)
            },
            height: parseFloat(document.getElementById('person-height').value),
            birthday: document.getElementById('person-birthday').value || null,
            eyeColor: document.getElementById('person-eye-color').value,
            nationality: document.getElementById('person-nationality').value || null,
            location: {
                x: parseFloat(document.getElementById('loc-x').value),
                y: parseFloat(document.getElementById('loc-y').value),
                name: document.getElementById('loc-name').value
            }
        };
        
        const xml = personToXml(person, true);
        await makeRequest(`${baseUrl}/persons/${id}`, 'PUT', xml);
        document.getElementById('person-form').reset();
        document.getElementById('person-id').value = '';
        setFormMode('create');
        await loadPersons();
    } catch (error) {
        console.error('Update error:', error);
        showMessage(`Ошибка обновления: ${error.message}`, 'error');
    }
}

async function searchPersons() {
    const name = document.getElementById('search-name').value;
    if (!name) {
        showMessage('Введите имя для поиска', 'error');
        return;
    }
    
    try {
        const { text } = await makeRequest(`${baseUrl}/persons/search?name=${encodeURIComponent(name)}`);
        const parser = new DOMParser();
        const xml = parser.parseFromString(text, 'application/xml');
        
        const persons = Array.from(xml.querySelectorAll('PersonsWrapper > persons > persons')).map(person => {
            return {
                id: person.querySelector('id')?.textContent,
                name: person.querySelector('name')?.textContent,
                coordinates: {
                    x: person.querySelector('coordinates > x')?.textContent,
                    y: person.querySelector('coordinates > y')?.textContent
                },
                height: person.querySelector('height')?.textContent,
                eyeColor: person.querySelector('eyeColor')?.textContent,
                nationality: person.querySelector('nationality')?.textContent,
                location: {
                    name: person.querySelector('location > name')?.textContent
                }
            };
        });
        
        let html = '<h3>Результаты поиска:</h3>';
        if (persons.length === 0) {
            html += '<p>Ничего не найдено</p>';
        } else {
            html += '<table><thead><tr><th>id</th><th>name</th><th>height</th><th>eyeColor</th><th>nationality</th><th>location</th></tr></thead><tbody>';
            persons.forEach(person => {
                html += '<tr>';
                html += `<td>${person.id}</td>`;
                html += `<td>${escapeXml(person.name)}</td>`;
                html += `<td>${person.height}</td>`;
                html += `<td>${person.eyeColor}</td>`;
                html += `<td>${person.nationality || '-'}</td>`;
                html += `<td>${escapeXml(person.location.name)}</td>`;
                html += '</tr>';
            });
            html += '</tbody></table>';
        }
        
        document.getElementById('search-results').innerHTML = html;
    } catch (error) {
        showMessage(`Ошибка поиска: ${error.message}`, 'error');
    }
}

async function getMaxCoordinates() {
    try {
        const { text } = await makeRequest(`${baseUrl}/persons/max-coordinates`);
        const parser = new DOMParser();
        const xml = parser.parseFromString(text, 'application/xml');
        const person = xmlToPerson(xml);
        
        if (!person) {
            document.getElementById('additional-results').innerHTML = '<p>Человек не найден</p>';
            return;
        }
        
        let html = '<h3>Человек с максимальными координатами:</h3>';
        html += `<p><strong>id:</strong> ${person.id}</p>`;
        html += `<p><strong>name:</strong> ${escapeXml(person.name)}</p>`;
        html += `<p><strong>coordinates:</strong> (${person.coordinates.x}, ${person.coordinates.y})</p>`;
        html += `<p><strong>height:</strong> ${person.height}</p>`;
        html += `<p><strong>eyeColor:</strong> ${person.eyeColor}</p>`;
        html += `<p><strong>location:</strong> ${escapeXml(person.location.name)}</p>`;
        
        document.getElementById('additional-results').innerHTML = html;
    } catch (error) {
        showMessage(`Ошибка: ${error.message}`, 'error');
    }
}

async function getAllLocations() {
    try {
        const { text } = await makeRequest(`${baseUrl}/persons/locations`);
        const parser = new DOMParser();
        const xml = parser.parseFromString(text, 'application/xml');
        
        const locations = Array.from(xml.querySelectorAll('LocationsWrapper > locations > locations')).map(loc => {
            return {
                x: loc.querySelector('x')?.textContent,
                y: loc.querySelector('y')?.textContent,
                name: loc.querySelector('name')?.textContent
            };
        });
        
        let html = '<h3>Уникальные локации:</h3>';
        if (locations.length === 0) {
            html += '<p>Локации не найдены</p>';
        } else {
            html += '<table><thead><tr><th>X</th><th>Y</th><th>Название</th></tr></thead><tbody>';
            locations.forEach(loc => {
                html += '<tr>';
                html += `<td>${loc.x}</td>`;
                html += `<td>${loc.y}</td>`;
                html += `<td>${escapeXml(loc.name)}</td>`;
                html += '</tr>';
            });
            html += '</tbody></table>';
        }
        
        document.getElementById('additional-results').innerHTML = html;
    } catch (error) {
        showMessage(`Ошибка: ${error.message}`, 'error');
    }
}

async function getCountByNationality() {
    const nationality = document.getElementById('demo-nationality').value;
    try {
        const { text } = await makeRequest(`${baseUrl}/demography/nationality/${nationality}/hair-color`);
        const parser = new DOMParser();
        const xml = parser.parseFromString(text, 'application/xml');
        const count = xml.querySelector('count')?.textContent || '0';
        
        document.getElementById('demography-results').innerHTML = 
            `<h3>Результат:</h3><p>Количество людей с национальностью ${nationality}: <strong>${count}</strong></p>`;
    } catch (error) {
        showMessage(`Ошибка: ${error.message}`, 'error');
    }
}

async function getCountByNationalityAndEyeColor() {
    const nationality = document.getElementById('demo-nationality').value;
    const eyeColor = document.getElementById('demo-eye-color').value;
    try {
        const { text } = await makeRequest(`${baseUrl}/demography/nationality/${nationality}/eye-color/${eyeColor}`);
        const parser = new DOMParser();
        const xml = parser.parseFromString(text, 'application/xml');
        const count = xml.querySelector('count')?.textContent || '0';
        
        document.getElementById('demography-results').innerHTML = 
            `<h3>Результат:</h3><p>Количество людей с национальностью ${nationality} и цветом глаз ${eyeColor}: <strong>${count}</strong></p>`;
    } catch (error) {
        showMessage(`Ошибка: ${error.message}`, 'error');
    }
}

