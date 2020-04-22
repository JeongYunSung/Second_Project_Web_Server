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
        })
}
function loading() {
    (() => {
        const value = document.querySelector('meta[name="login"]');
        if (value != null && value.getAttribute("content") != null) {
            document.querySelector("#signin").toggleAttribute("hidden");
            document.querySelector("#signup").toggleAttribute("hidden");
            document.querySelector("#myinfo").toggleAttribute("hidden");
            document.querySelector("#logout").toggleAttribute("hidden");
        }
        const details = document.querySelectorAll(".nav-list-item-detail");
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
        const items = document.querySelectorAll(".nav-list-item-detail");
        for (let i = 0; i < items.length; i++) {
            items[i].addEventListener("click", (event) => {
                location.href = `http://localhost/products/search?categoryName=${event.target.innerHTML}`;
            })
        }
    })();
}
includeHtml("header", "../fragments/header.html");
includeHtml("footer", "../fragments/footer.html");