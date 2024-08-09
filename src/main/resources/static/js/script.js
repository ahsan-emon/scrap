// Function to validate file size and type
function validateFile() {
    const fileInput = document.getElementById('exampleInputphoto');
    
    // Check if the file input is empty (no file selected)
    if (fileInput.files.length === 0) {
        return true; // Allow form submission if no file is selected
    }

    const filePath = fileInput.value;
    const allowedExtensions = /(\.jpg|\.jpeg|\.png)$/i;
    const maxSize = 10 * 1024 * 1024; // 10MB

    // Validate file extension
    if (!allowedExtensions.exec(filePath)) {
        alert('Invalid file type. Please upload a file with .jpg, .jpeg or .png extension.');
        fileInput.value = ''; // Clear the file input
        return false;
    }

    // Validate file size
    if (fileInput.files[0].size > maxSize) {
        alert('File size exceeds 10MB. Please upload a smaller file.');
        fileInput.value = ''; // Clear the file input
        return false;
    }

    return true; // Validation passed, allow form submission
}
