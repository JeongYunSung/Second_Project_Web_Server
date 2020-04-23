class Page {

    constructor(id) {
        const list = document.querySelector(`#${id}-product-list`);
        if(list != null) {
            const count = document.querySelectorAll(`.${id}-product`)
            this._size = Math.floor((list.clientWidth/206) + 0.03);
            this._maxPage = Math.floor(count.length/this._size + ((this._size-1)/this._size));
        }
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

const best = new Page("best");
const view = new Page("view");

function move(check, id, page) {
    const products = document.querySelectorAll(`.${id}-product`);
    const remain = (products.length%page.size) === 0 ? 1 : (products.length%page.size);
    if (check && page.page < page.maxPage) {
        if(page.page == page.maxPage - 1) {
            products[0].style.marginLeft = Number(products[0].style.marginLeft.replace("px", "")) - 206*remain + "px";
        }else {
            products[0].style.marginLeft = Number(products[0].style.marginLeft.replace("px", "")) - (206*page.size) + "px";
        }
        page.page += 1;
    }else if (!check && page.page > 1) {
        if(page.page == page.maxPage) {
            products[0].style.marginLeft = Number(products[0].style.marginLeft.replace("px", "")) + 206*remain + "px";
        }else {
            products[0].style.marginLeft = Number(products[0].style.marginLeft.replace("px", "")) + (206*page.size) + "px";
        }
        page.page -= 1;
    }
}

function right_best() {
    move(true, "best", best);
}

function left_best() {
    move(false, "best", best);
}

function right_view() {
    move(true, "view", view);
}

function left_view() {
    move(false, "view", view);
}

function event(id, page) {
    const list = document.querySelector(`#${id}-product-list`);
    window.addEventListener("resize", (event) => {
        if(list != null) {
            const count = document.querySelectorAll(`.${id}-product`)
            const size = Math.floor((list.clientWidth/206) + 0.03);
            const maxPage = Math.floor(count.length/size + ((size-1)/size));
            if(isNaN(maxPage))
                return;
            page.maxPage = maxPage;
            page.size = size;
            page.page = 1;
            count[0].style.marginLeft = "0px";
        }
    });
}
event("best", best);
event("view", view);