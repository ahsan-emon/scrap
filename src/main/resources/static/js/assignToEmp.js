 function updateTotalAssignAmount() {
	const employeeSelect = document.getElementById('employee');
    const amountInput = document.getElementById('exampleInputHasAmount');
    
    employeeSelect.addEventListener('change', updateTotalAssignAmount);
    amountInput.addEventListener('input', updateTotalAssignAmount);
    const selectedEmployee = employeeSelect.options[employeeSelect.selectedIndex];
    const previousAmount = parseInt(selectedEmployee.getAttribute('data-amount')) || 0;
    const newAmount = parseInt(amountInput.value) || 0;
    
    const totalAssignAmount = previousAmount + newAmount;
    document.getElementById('totalAssignAmount').innerText = totalAssignAmount.toString();
}
