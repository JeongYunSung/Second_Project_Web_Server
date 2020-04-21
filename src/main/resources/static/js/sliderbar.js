class Page {

    constructor() {
        const list = document.querySelector("#best-product-list");
        this._size = Math.floor((list.clientWidth/206) + 0.03);
        this._maxPage = Math.floor(10/this._size + ((this._size-1)/this._size));
        this._page = 1;
    }

    set size(size) {
        this._size = size;
    }
    get size() {
        return this._size;
    }
    set page(page) {
        this._page = page;
    }
    get page() {
        return this._page;
    }
    set maxPage(maxPage) {
        this._maxPage = maxPage;
    }
    get maxPage() {
        return this._maxPage;
    }
}

const page = new Page();

function move(check) {
    const products = document.querySelectorAll(".best-product");
    if (check && page.page < page.maxPage) {
        if(page.page == page.maxPage - 1) {
            products[0].style.marginLeft = Number(products[0].style.marginLeft.replace("px", "")) - 200 + "px";
        }else {
            products[0].style.marginLeft = Number(products[0].style.marginLeft.replace("px", "")) - (206*page.size) + "px";
        }
        page.page += 1;
    }else if (!check && page.page > 1) {
        if(page.page == page.maxPage) {
            products[0].style.marginLeft = Number(products[0].style.marginLeft.replace("px", "")) + 200 + "px";
        }else {
            products[0].style.marginLeft = Number(products[0].style.marginLeft.replace("px", "")) + (206*page.size) + "px";
        }
        page.page -= 1;
    }
}

function right() {
    move(true);
}

function left() {
    move(false);
}

(() => {
    const list = document.querySelector("#best-product-list");
    window.addEventListener("resize", (event) => {
        const size = Math.floor((list.clientWidth/206) + 0.03);
        const maxPage = Math.floor(10/size + ((size-1)/size));
        if(isNaN(maxPage))
            return;
        page.maxPage = maxPage;
        page.size = size;
        page.page = 1;
    });
})();