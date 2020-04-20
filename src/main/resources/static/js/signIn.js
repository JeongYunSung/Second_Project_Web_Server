function signIn() {
    const req = new XMLHttpRequest();
    const username = document.querySelector('#username');
    const password = document.querySelector('#password');
    req.open("POST", encodeURI("http://localhost/signin"), true);
    // req.setRequestHeader(document.querySelector('meta[name=_csrf]').getAttribute("content")
    //     , document.querySelector('meta[name=_csrf_header]').getAttribute("content"));
    req.onreadystatechange = () => {
        if(req.readyState == 4 && req.status == 200) {
            alert("GOOD");
        }
    };
    req.send(`username=${username}&password=${password}`);
}