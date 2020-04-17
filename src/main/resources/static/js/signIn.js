function signIn(id) {
    const form = document.querySelector(`#${id}`);
    const req = new XMLHttpRequest();
    req.open("POST", encodeURI("http://localhost/signIn"), true);
    req.setRequestHeader('${_csrf.headerName}', '${_csrf.token}');
    req.onreadystatechange = () => {
        if(req.readyState == 4 && req.status == 200) {
            alert("GOOD");
        }
    };
    req.send(form.getFormData());
}