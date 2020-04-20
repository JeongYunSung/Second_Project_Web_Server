function includeHtml(id, path) {
    fetch(path)
        .then(response => {
            return response.text()
        })
        .then(data => {
            document.querySelector(`#${id}`).innerHTML = data;
            if (id === "header") {
                isLogin();
            }
        })
}
function isLogin() {
    const value = document.querySelector('meta[name="login"]');
    if (value != null && value.getAttribute("content") != null) {
        document.querySelector("#signin").toggleAttribute("hidden");
        document.querySelector("#signup").toggleAttribute("hidden");
        document.querySelector("#myinfo").toggleAttribute("hidden");
        document.querySelector("#logout").toggleAttribute("hidden");
    }
}
includeHtml("header", "../fragments/header.html");
includeHtml("footer", "../fragments/footer.html");