// Example script.js for client-side interactions

// Function to validate the form data before submission
function validateForm() {
    var firstName = document.getElementById("firstName").value;
    var lastName = document.getElementById("lastName").value;
    var id = document.getElementById("id").value;
    var password = document.getElementById("password").value;

    // Perform validation checks here
    if (firstName === "" || lastName === "" || id === "" || password === "") {
        alert("Please fill in all fields.");
        return false;
    }

    return true;
}
