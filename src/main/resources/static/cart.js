function Get(yourUrl) {
    var Httpreq = new XMLHttpRequest(); // a new request
    Httpreq.open("GET", yourUrl, false);
    Httpreq.send(null);
    return Httpreq.responseText;
}

// Function to remove item from cart
function removeItem(productId) {
    cartItems.splice(cartItems.findIndex(item => item.first.id === productId), 1);

    var xhr = new XMLHttpRequest();

    xhr.onload = function () {
        displayCartItems();
    };

    xhr.open("POST", "/removeFromCart", true);
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
    xhr.send("id=" + productId);
}

// Function to increase quantity
function increaseQuantity(productId) {
    var itemIndex = cartItems.findIndex(item => item.first.id === productId);
    if (cartItems[itemIndex].first.stock > cartItems[itemIndex].second) {
        cartItems[itemIndex].second++;
        displayCartItems();

        var xhr = new XMLHttpRequest();
        xhr.open("POST", "/editCart", true);
        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        xhr.send("id=" + productId + "&change=1");
    }
}

// Function to decrease quantity
function decreaseQuantity(productId) {
    var itemIndex = cartItems.findIndex(item => item.first.id === productId);
    if (0 < cartItems[itemIndex].second) {
        cartItems[itemIndex].second--;
        displayCartItems();

        var xhr = new XMLHttpRequest();
        xhr.open("POST", "/editCart", false);
        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        xhr.send("id=" + productId + "&change=-1");
    }
}

// Function to add to cart
function addToLocalCart(productId) {
    var cartItems = JSON.parse(localStorage.getItem('cart')) || [];
    var existingItem = cartItems.find(item => item.id === productId);
    if (existingItem) {
        var product = JSON.parse(Get("/getProduct?id=" + item.id))
        if (product.stock > existingItem.quantity)
            existingItem.quantity++;
    } else {
        cartItems.push({id: productId, quantity: 1});
    }
    localStorage.setItem('cart', JSON.stringify(cartItems));
}

// Function to remove item from cart
function removeLocalItem(productId) {
    var cartItems = JSON.parse(localStorage.getItem('cart')) || [];
    var existingItem = cartItems.find(item => item.id === productId);
    if (existingItem)
        cartItems.splice(existingItem, 1);

    localStorage.setItem('cart', JSON.stringify(cartItems));
}


// Function to increase quantity
function increaseLocalQuantity(productId, maxQuantity) {
    var cartItems = JSON.parse(localStorage.getItem('cart')) || [];
    var existingItem = cartItems.find(item => item.id === productId);
    if (existingItem && existingItem.quantity < maxQuantity) {
        existingItem.quantity++;
    }
    localStorage.setItem('cart', JSON.stringify(cartItems));
}

// Function to decrease quantity
function decreaseLocalQuantity(productId) {
    var cartItems = JSON.parse(localStorage.getItem('cart')) || [];
    var existingItem = cartItems.find(item => item.id === productId);
    if (existingItem) {
        existingItem.quantity--;
        if (existingItem.quantity === 0) {
            removeLocalItem(productId);
            return;
        }
    }
    localStorage.setItem('cart', JSON.stringify(cartItems));
}





