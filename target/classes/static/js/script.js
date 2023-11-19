document.addEventListener("DOMContentLoaded", function () {
        reloadBooks();
});

function reloadBooks() {
        let bookContainer = document.getElementById("body");
        bookContainer.innerHTML = "";
        fetch('http://localhost:8090/api/books')
            .then(response => response.json())
            .then(json => json.forEach( book=> {

                    let bookRow = document.createElement("tr");
                    let bookTitle = document.createElement("td");
                    let titleLink = document.createElement("a");
                    titleLink.setAttribute("href", "/books/bookInfo/" + book.id);
                    titleLink.textContent = book.title;
                    let bookAuthor = document.createElement("td");
                    let bookCategory = document.createElement("td");
                    let bookListPrice = document.createElement("td");
                    let bookOurPrice = document.createElement("td");
                    let bookImage = document.createElement("td");
                    let operation = document.createElement("td");

                    let image = document.createElement("img");
                    image.setAttribute("src", book.imageUrl);
                    image.setAttribute("width", "50");
                    image.setAttribute("height", "80");
                    image.setAttribute("alt", "image");
                    let bookActive = document.createElement("td");
                    let bookOperation = document.createElement("div");
                    bookOperation.setAttribute("class", "btn-group");

                    let editButton = document.createElement("button");
                    editButton.setAttribute("class", "btn btn-success fa fa-pencil btn-xs update-book");
                    editButton.setAttribute("type", "submit");
                    editButton.setAttribute("value", "edit");
                    editButton.textContent = "Edit";
                    editButton.addEventListener("click", function () {
                            window.location.href = "/books/updateBook/" + book.id;
                    });
                    bookOperation.appendChild(editButton);


                    let deleteButton = document.createElement("button");
                    deleteButton.setAttribute("class", "btn btn-danger btn-xs delete-book");
                    deleteButton.setAttribute("type", "submit");
                    deleteButton.setAttribute("value", "delete");
                    deleteButton.dataset.id = book.id;
                    deleteButton.textContent = "Delete";
                    deleteButton.setAttribute("id", book.id);
                    deleteButton.addEventListener("click", deleteBtnClicked);
                    bookOperation.appendChild(deleteButton);

                    bookAuthor.textContent = book.author;
                    bookCategory.textContent = book.category;
                    bookListPrice.textContent = book.listPrice + " $";
                    bookOurPrice.textContent = book.ourPrice + " $";
                    bookActive.textContent = book.active;

                    bookTitle.appendChild(titleLink);
                    bookRow.appendChild(bookTitle);
                    bookRow.appendChild(bookAuthor);
                    bookRow.appendChild(bookCategory);
                    bookRow.appendChild(bookListPrice);
                    bookRow.appendChild(bookOurPrice);
                    bookImage.appendChild(image);
                    bookRow.appendChild(bookImage);
                    bookRow.appendChild(bookActive);
                    operation.appendChild(bookOperation);
                    bookRow.appendChild(operation);

                    bookContainer.appendChild(bookRow);
            }))
}
function deleteBtnClicked(event) {

    let bookId = event.target.dataset.id;
    let requestOptions = {
        method: 'DELETE'
    }

    fetch(`http://localhost:8090/api/books/remove/${bookId}`, requestOptions)
        .then(_ => reloadBooks())
        .catch(error => console.log('error', error))
}