const API_KEY = "demo-key-123";

const headers = {
    "X-API-KEY": API_KEY,
    "Content-Type": "application/json"
};

const fromCurrency = document.getElementById("fromCurrency");
const toCurrency = document.getElementById("toCurrency");
const amount = document.getElementById("amount");
const result = document.getElementById("result");

async function loadCurrencies() {
    try {
        const response = await fetch("/api/currencies", {
            headers: headers
        });

        if (!response.ok) {
            throw new Error("Unable to load currencies");
        }

        const currencies = await response.json();

        Object.entries(currencies).forEach(([code, name]) => {
            const option1 = new Option(`${code} - ${name}`, code);
            const option2 = new Option(`${code} - ${name}`, code);

            fromCurrency.add(option1);
            toCurrency.add(option2);
        });

        fromCurrency.value = "USD";
        toCurrency.value = "INR";

    } catch (error) {
        result.textContent = error.message;
    }
}

async function convertCurrency() {

    const value = Number(amount.value);

    if (!Number.isFinite(value) || value <= 0) {
        result.textContent = "Please enter a valid positive amount.";
        return;
    }

    const button = document.getElementById("convertButton");
    button.disabled = true;
    button.textContent = "Converting...";

    try {
        const response = await fetch("/api/convert", {
            method: "POST",
            headers: headers,
            body: JSON.stringify({
                from: fromCurrency.value,
                to: toCurrency.value,
                amount: value
            })
        });

        const data = await response.json();

        if (!response.ok) {
            throw new Error(data.error || "Conversion failed");
        }

        result.innerHTML = `
            ${data.amount} ${data.fromCurrency}
            = ${data.convertedAmount} ${data.toCurrency}
            <br><br>
            Exchange Rate: ${data.exchangeRate}
        `;

        loadHistory();

    } catch (error) {
        result.textContent = error.message;
    } finally {
        button.disabled = false;
        button.textContent = "Convert Now";
    }
}

async function loadHistory() {

    const historyBody = document.getElementById("historyBody");

    try {
        const response = await fetch("/api/history", {
            headers: headers
        });

        const history = await response.json();

        if (!response.ok) {
            throw new Error("Unable to load history");
        }

        historyBody.innerHTML = "";

        if (history.length === 0) {
            historyBody.innerHTML =
                `<tr><td colspan="5">No conversion history found.</td></tr>`;
            return;
        }

        history.forEach(item => {

            const row = document.createElement("tr");

            const values = [
                item.fromCurrency,
                item.toCurrency,
                `${item.amount}`,
                `${item.convertedAmount}`
            ];

            values.forEach(value => {
                const cell = document.createElement("td");
                cell.textContent = value;
                row.appendChild(cell);
            });

            const actionCell = document.createElement("td");
            const deleteButton = document.createElement("button");

            deleteButton.textContent = "Delete";
            deleteButton.className = "delete-button";
            deleteButton.onclick = () => deleteHistory(item.id);

            actionCell.appendChild(deleteButton);
            row.appendChild(actionCell);

            historyBody.appendChild(row);
        });

    } catch (error) {
        historyBody.innerHTML =
            `<tr><td colspan="5">${error.message}</td></tr>`;
    }
}

async function deleteHistory(id) {

    if (!confirm("Do you want to delete this record?")) {
        return;
    }

    try {
        const response = await fetch(`/api/history/${id}`, {
            method: "DELETE",
            headers: headers
        });

        if (!response.ok) {
            throw new Error("Unable to delete record");
        }

        loadHistory();

    } catch (error) {
        alert(error.message);
    }
}

document.getElementById("convertButton")
    .addEventListener("click", convertCurrency);

document.getElementById("refreshButton")
    .addEventListener("click", loadHistory);

document.getElementById("swapButton")
    .addEventListener("click", () => {
        const temp = fromCurrency.value;
        fromCurrency.value = toCurrency.value;
        toCurrency.value = temp;
    });

loadCurrencies().then(loadHistory);