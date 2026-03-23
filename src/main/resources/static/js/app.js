// Lấy phần tử DOM
const form = document.getElementById('importForm');
const resultDiv = document.getElementById('result');
const listBtn = document.getElementById('listBtn');
const booksTableWrapper = document.getElementById('booksTableWrapper');

// Helper: format JSON cho phần debug
function formatJson(obj) {
    return JSON.stringify(obj, null, 2);
}

// Gọi API import book khi submit form
if (form) {
    form.addEventListener('submit', async (event) => {
        event.preventDefault();

        const formData = new FormData(form);

        // Payload phải khớp với ManageBookRequest
        const payload = {
            title: formData.get('title'),
            author: formData.get('author'),
            price: Number(formData.get('price')),
            categoryId: Number(formData.get('categoryId'))
        };

        resultDiv.className = '';
        resultDiv.textContent =
            'Calling API POST /api/books/import ...\n\nPayload:\n' +
            formatJson(payload);

        try {
            const response = await fetch('/api/books/import', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(payload)
            });

            const data = await response.json();

            const statusLine = `HTTP ${response.status} ${response.statusText}`;
            resultDiv.className = response.ok ? 'success' : 'error';
            resultDiv.textContent =
                statusLine + '\n\n' +
                'Response body:\n' + formatJson(data);
        } catch (error) {
            resultDiv.className = 'error';
            resultDiv.textContent = 'Error calling API:\n' + error;
        }
    });
}

// Hàm render bảng books từ mảng BookResponse
function renderBooksTable(books) {
    // books là mảng BookResponse từ APIResponse.data
    // Cấu trúc BookResponse trong backend của bạn thường có:
    // id, title, author, price, categoryId (hoặc categoryName)
    // Mình sẽ cố gắng hiển thị các field cơ bản; nếu tên field khác,
    // bạn chỉnh lại cho khớp.

    if (!Array.isArray(books) || books.length === 0) {
        booksTableWrapper.innerHTML =
            '<div class="no-data">No books found.</div>';
        return;
    }

    // Lấy danh sách cột cơ bản, cố gắng đọc theo key phổ biến
    // Nếu bạn biết chắc cấu trúc BookResponse, có thể fix cột cụ thể.
    const headers = ['id', 'title', 'author', 'price', 'categoryId'];

    let thead = '<thead><tr>';
    headers.forEach(h => {
        thead += `<th>${h}</th>`;
    });
    thead += '</tr></thead>';

    let tbody = '<tbody>';
    books.forEach(book => {
        tbody += '<tr>';
        headers.forEach(h => {
            const value = book[h] != null ? book[h] : '';
            tbody += `<td>${value}</td>`;
        });
        tbody += '</tr>';
    });
    tbody += '</tbody>';

    const tableHtml = `<table>${thead}${tbody}</table>`;
    booksTableWrapper.innerHTML = tableHtml;
}

// Gọi API list books khi click "List Books"
if (listBtn) {
    listBtn.addEventListener('click', async () => {
        resultDiv.className = '';
        resultDiv.textContent = 'Calling API GET /api/books/list ...';

        try {
            const response = await fetch('/api/books/list');
            const data = await response.json();

            const statusLine = `HTTP ${response.status} ${response.statusText}`;
            resultDiv.className = response.ok ? 'success' : 'error';
            resultDiv.textContent =
                statusLine + '\n\n' +
                'Response body:\n' + formatJson(data);

            // data theo APIResponse<List<BookResponse>>:
            // {
            //   status: "...",
            //   code: "...",
            //   message: "...",
            //   data: [ { ...BookResponse }, ... ]
            // }
            if (data && Array.isArray(data.data)) {
                renderBooksTable(data.data);
            } else {
                booksTableWrapper.innerHTML =
                    '<div class="no-data">No books data in response.</div>';
            }
        } catch (error) {
            resultDiv.className = 'error';
            resultDiv.textContent = 'Error calling API:\n' + error;
            booksTableWrapper.innerHTML =
                '<div class="no-data">Error loading books.</div>';
        }
    });
}
