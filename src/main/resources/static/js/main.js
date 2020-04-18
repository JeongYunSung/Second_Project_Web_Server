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
    const value = document.cookie.match("(^|;) ?X-AUTH-TOKEN=([^;]*)(;|$)");
    if (value != null) {
        document.querySelector("#signin").toggleAttribute("hidden");
        document.querySelector("#signup").toggleAttribute("hidden");
        document.querySelector("#myinfo").toggleAttribute("hidden");
        document.querySelector("#logout").toggleAttribute("hidden");
    }
}
includeHtml("header", "fragments/header.html");
includeHtml("footer", "fragments/footer.html");