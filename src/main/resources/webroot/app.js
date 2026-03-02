async function testHello() {
    const name = document.getElementById('nameInput').value || 'World';
    const resultDiv = document.getElementById('helloResult');
    try {
        const response = await fetch('/hello?name=' + encodeURIComponent(name));
        const text = await response.text();
        resultDiv.textContent = 'Response: ' + text;
        resultDiv.classList.add('visible');
    } catch (error) {
        resultDiv.textContent = 'Error: ' + error.message;
        resultDiv.classList.add('visible');
    }
}

async function testPi() {
    const resultDiv = document.getElementById('piResult');
    try {
        const response = await fetch('/pi');
        const text = await response.text();
        resultDiv.textContent = 'Response: ' + text;
        resultDiv.classList.add('visible');
    } catch (error) {
        resultDiv.textContent = 'Error: ' + error.message;
        resultDiv.classList.add('visible');
    }
}
