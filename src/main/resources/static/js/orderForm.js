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

// customer due
function updateDueAmount() {
        const customerSelect = document.getElementById('customer');
        const selectedOption = customerSelect.options[customerSelect.selectedIndex];
        const customerDue = selectedOption.getAttribute('data-due');
        const dueInput = document.getElementById('customerDue');
        dueInput.value = selectedOption.value ? customerDue : '';
}