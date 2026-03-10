async function fetchAndShow(url, resultId) {
    const resultDiv = document.getElementById(resultId);
    try {
        const response = await fetch(url);
        const text = await response.text();
        resultDiv.textContent = 'Respuesta: ' + text;
        resultDiv.classList.add('visible');
    } catch (error) {
        resultDiv.textContent = 'Error: ' + error.message;
        resultDiv.classList.add('visible');
    }
}

async function testHello() {
    const name = document.getElementById('nameInput').value || 'World';
    await fetchAndShow('/hello?name=' + encodeURIComponent(name), 'helloResult');
}

async function testPi() {
    await fetchAndShow('/pi', 'piResult');
}

async function testGreeting() {
    const name = document.getElementById('greetingInput').value.trim();
    const url = name ? '/greeting?name=' + encodeURIComponent(name) : '/greeting';
    await fetchAndShow(url, 'greetingResult');
}

async function testCounter() {
    await fetchAndShow('/counter', 'counterResult');
}
