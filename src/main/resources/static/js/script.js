// $(document).ready(function() {
//  *
//  */
//
// $(document).ready(function() {
//     $('.delete-book').on('click', function (){
//         /*<![CDATA[*/
//         var path = /*[[@{/}]]*/'remove';
//         /*]]>*/
//
//         var id=$(this).attr('id');
//
//         bootbox.confirm({
//             message: "Are you sure to remove this book? It can't be undone.",
//             buttons: {
//                 cancel: {
//                     label:'<i class="fa fa-times"></i> Cancel'
//                 },
//                 confirm: {
//                     label:'<i class="fa fa-check"></i> Confirm'
//                 }
//             },
//             callback: function(confirmed) {
//                 if(confirmed) {
//                     $.post(path, {'id':id}, function(res) {
//                         location.reload();
//                     });
//                 }
//             }
//         });
//     });
//
//     $('.checkboxBook').click(function () {
//         var id = $(this).attr('id');
//         if(this.checked){
//             bookIdList.push(id);
//         }
//         else {
//             bookIdList.splice(bookIdList.indexOf(id), 1);
//         }
//     })
//
//     $('#deleteSelected').click(function() {
//         var idList= $('.checkboxBook');
//         var bookIdList=[];
//         for (var i = 0; i < idList.length; i++) {
//             if(idList[i].checked==true) {
//                 bookIdList.push(idList[i]['id'])
//             }
//         }
//
//         console.log(bookIdList);
//
//         /*<![CDATA[*/
//         var path = /*[[@{/}]]*/'removeList';
//         /*]]>*/
//
//         bootbox.confirm({
//             message: "Are you sure to remove all selected books? It can't be undone.",
//             buttons: {
//                 cancel: {
//                     label:'<i class="fa fa-times"></i> Cancel'
//                 },
//                 confirm: {
//                     label:'<i class="fa fa-check"></i> Confirm'
//                 }
//             },
//             callback: function(confirmed) {
//                 if(confirmed) {
//                     $.ajax({
//                         type: 'POST',
//                         url: path,
//                         data: JSON.stringify(bookIdList),
//                         contentType: "application/json",
//                         success: function(res) {
//                             console.log(res);
//                             location.reload()
//                         },
//                         error: function(res){
//                             console.log(res);
//                             location.reload();
//                         }
//                     });
//                 }
//             }
//         });
//     });
//
//     $("#selectAllBooks").click(function() {
//         if($(this).prop("checked")===true) {
//             $(".checkboxBook").prop("checked",true);
//         } else if ($(this).prop("checked")===false) {
//             $(".checkboxBook").prop("checked",false);
//         }
//     })
// });}
let reloadButton = document.getElementById("btn-fetch");
reloadButton.addEventListener("click", reloadBooks());
function reloadBooks() {
    let bookContainer = document.getElementById("body");
    bookContainer.innerHTML = "";
    fetch('http://localhost:8080/api/books')
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
            deleteButton.textContent = "Delete";
            deleteButton.addEventListener("click", function () {
                fetch('http://localhost:8080/api/books/' + book.id, {
                    method: 'DELETE',
                })
                    .then(response => response.json())
                    .then(json => {
                        console.log(json);
                        reloadBooks();
                    })
            });
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
