function includeHtml(id, path) {
    fetch(path)
        .then(response => {
            return response.text()
        })
        .then(data => {
            document.querySelector(`#${id}`).innerHTML = data;
            if (id === "header") {
                loading();
            }
            if (path.includes("searchBar")) {
                loadSearch();
            }
        })
}
function loading() {
    (() => {
        const value = document.querySelector('meta[name="login"]');
        const details = document.querySelectorAll(".nav-list-item-detail");
        const items = document.querySelectorAll(".nav-list-item-detail");
        if (value != null && value.getAttribute("content") != null) {
            document.querySelector("#signin").toggleAttribute("hidden");
            document.querySelector("#signup").toggleAttribute("hidden");
            document.querySelector("#myinfo").toggleAttribute("hidden");
            document.querySelector("#logout").toggleAttribute("hidden");
        }
        for (let i = 0; i < details.length; i++) {
            const detail = details[i];
            detail.addEventListener("mouseover", (event) => {
                const children = detail.children;
                for (let j = 0; j < children.length; j++) {
                    if(children[j].classList.contains("nav-list-item-sub")) {
                        children[j].removeAttribute("hidden");
                    }
                }
            });
            detail.addEventListener("mouseout", (event) => {
                const children = detail.children;
                for (let j = 0; j < children.length; j++) {
                    if(children[j].classList.contains("nav-list-item-sub")) {
                        children[j].setAttribute("hidden", "");
                    }
                }
            })
        }
        for (let i = 0; i < items.length; i++) {
            items[i].addEventListener("click", (event) => {
                if(event.target.nodeName === "A") {
                    location.href = `http://localhost/products?category=${event.target.innerHTML}`;
                }
            })
        }
    })();
}
function loadSearch() {
    const searchBar = document.querySelector("#search-button");
    searchBar.addEventListener("click", (event) => {
        const select = document.querySelector("#search-type");
        const text = document.querySelector("#search-text").value;
        const type = select.options[select.selectedIndex].value;

        if (text === null || text.length === 0) {
            alert("검색어를 입력해주세요.");
            return;
        }

        let query = "";

        if(type === "전체") {
            query += `category=${text}&product=${text}`;
        } else if(type === "상품") {
            query += `product=${text}`;
        } else if(type === "카테고리") {
            query += `category=${text}`;
        } else if(type === "최소가격") {
            query += `min=${text}`;
        } else{
            query += `max=${text}`;
        }

        location.href = `http://localhost/products?${query}`;
    })
}
includeHtml("header", "../fragments/header.html");
includeHtml("footer", "../fragments/footer.html");