function addProduct() {
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
}