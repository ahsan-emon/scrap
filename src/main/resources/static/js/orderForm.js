/*function addProduct() {
    var productsDiv = document.getElementById('products');
    var productTemplate = document.querySelector('.product-template').cloneNode(true);
    productTemplate.classList.remove('product-template');
    productTemplate.style.display = '';
    var index = document.querySelectorAll('.product').length;
    productTemplate.innerHTML = productTemplate.innerHTML.replace(/__index__/g, index);
    productsDiv.appendChild(productTemplate);
}

function removeProduct(button) {
    var productDiv = button.closest('.product');
    productDiv.remove();
    updateIndexes();
}

function updateIndexes() {
    var products = document.querySelectorAll('.product');
    products.forEach((product, index) => {
        product.innerHTML = product.innerHTML.replace(/\[orderItems\]\[\d+\]/g, `[orderItems][${index}]`);
        product.innerHTML = product.innerHTML.replace(/orderItems\.\d+\./g, `orderItems.${index}.`);
    });
}*/



/*function addProduct() {
    var productDiv = document.querySelector('.product').cloneNode(true);
    var inputs = productDiv.querySelectorAll('input, select');
    inputs.forEach(function(input) {
        input.value = '';
    });
    document.getElementById('products').appendChild(productDiv);
}

function removeProduct(button) {
    var productDiv = button.parentElement;
    if (document.querySelectorAll('.product').length > 1) {
        productDiv.remove();
    } else {
        alert('You must have at least one product.');
    }
}*/



function addProduct() {
    var productDiv = document.querySelector('.product').cloneNode(true);
    var inputs = productDiv.querySelectorAll('input, select');
    inputs.forEach(function(input) {
        input.value = '';
        var name = input.name;
        var match = name.match(/\[(\d+)\]/);
        if (match) {
            var index = parseInt(match[1]) + document.querySelectorAll('.product').length;
            input.name = name.replace(/\[\d+\]/, '[' + index + ']');
        }
    });
    document.getElementById('products').appendChild(productDiv);
}

function removeProduct(button) {
    var productDiv = button.parentElement;
    if (document.querySelectorAll('.product').length > 1) {
        productDiv.remove();
    } else {
        alert('You must have at least one product.');
    }
}
