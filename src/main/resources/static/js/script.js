// Function to validate file size and type
        function validateFile() {
            const fileInput = document.getElementById('exampleInputphoto');
            const filePath = fileInput.value;
            const allowedExtensions = /(\.jpg|\.jpeg|\.png)$/i;
            const maxSize = 10 * 1024 * 1024; // 10MB

            if (!allowedExtensions.exec(filePath)) {
                alert('Invalid file type. Please upload a file with .jpg, .jpeg, .png, or .gif extension.');
                fileInput.value = '';
                return false;
            }

            if (fileInput.files[0].size > maxSize) {
                alert('File size exceeds 10MB. Please upload a smaller file.');
                fileInput.value = '';
                return false;
            }

            return true;
        }