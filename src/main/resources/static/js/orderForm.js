document.addEventListener('DOMContentLoaded', function () {
    if (document.querySelector('.product')) {
        const productDivs = document.querySelectorAll('.product');
        productDivs.forEach(function(productDiv) {
            const quantityInput = productDiv.querySelector('input[name$=".quantity"]');
            const unitPriceInput = productDiv.querySelector('input[name$=".unitPrice"]');
            
            quantityInput.addEventListener('input', updateTotalAmount);
            unitPriceInput.addEventListener('input', updateTotalAmount);
        });
        
        updateTotalAmount();
    }
	const customerSelect = document.getElementById('customer');
	    if (customerSelect) {
	        customerSelect.addEventListener('change', updateDueAmount);
	    }
});

function addProduct() {
    if (document.querySelector('.product')) {
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
            if (input.type === 'number') {
                input.addEventListener('input', updateTotalAmount);
            }
        });
        document.getElementById('products').appendChild(productDiv);
        updateProductIndexes();
    }
}

function removeProduct(button) {
    var productDiv = button.parentElement;
    if (document.querySelectorAll('.product').length > 1) {
        productDiv.remove();
        updateProductIndexes();
        updateTotalAmount();
    } else {
        alert('You must have at least one product.');
    }
}

function updateProductIndexes() {
    const productDivs = document.querySelectorAll('.product');
    productDivs.forEach(function(productDiv, index) {
        const inputs = productDiv.querySelectorAll('input, select');
        inputs.forEach(function(input) {
            var name = input.name;
            var match = name.match(/\[(\d+)\]/);
            if (match) {
                input.name = name.replace(/\[\d+\]/, '[' + index + ']');
            }
        });
    });
}

function updateDueAmount() {
    const customerSelect = document.getElementById('customer');
    const selectedOption = customerSelect.options[customerSelect.selectedIndex];
    const customerDue = selectedOption.getAttribute('data-due');
    const dueInput = document.getElementById('customerDue');
    // dueInput.value = selectedOption.value ? customerDue : '';
	// Update the value of the hidden input field with the customerDue value
	 if (dueInput) {
	     dueInput.value = customerDue || ''; // Empty string if no due value
	 }

	 // Update the total amount with due (if applicable)
	 updateTotalAmount();
}

function updateTotalAmount() {
    if (document.querySelector('.product')) {
        const productDivs = document.querySelectorAll('.product');
        let totalAmount = 0;
        
        productDivs.forEach(function(productDiv) {
            const quantityInput = productDiv.querySelector('input[name$=".quantity"]');
            const unitPriceInput = productDiv.querySelector('input[name$=".unitPrice"]');
            
            const quantity = parseFloat(quantityInput.value) || 0;
            const unitPrice = parseFloat(unitPriceInput.value) || 0;
            
            totalAmount += Math.floor(quantity * unitPrice);
        });
		if(document.getElementById('totalAmountWithDue')){
			let totalAmountWithDue = 0;
			const dueInput = document.getElementById('customerDue');
			const dueValue = parseFloat(dueInput.value) || 0;
			totalAmountWithDue = totalAmount + dueValue;
			document.getElementById('totalAmountWithDue').innerText = totalAmountWithDue;
		}
		
		document.getElementById('totalAmount').innerText = totalAmount;
    }
}


//order list maximum date check
function setMaxDate() {
    const today = new Date().toISOString().split('T')[0];
    document.getElementById('fromDate').setAttribute('max', today);
    document.getElementById('toDate').setAttribute('max', today);
}
if(document.getElementById('fromDate') || document.getElementById('toDate')){
	window.onload = setMaxDate;
}