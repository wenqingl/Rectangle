function addCheck() {
    if (document.addForm.name.value == "" ||
        document.addForm.width.value == "" ||
        document.addForm.height.value == "" ||
        document.addForm.color.value == "" ||
        document.addForm.borderWidth.value == "") {
        document.getElementById("showError1").innerHTML = "Please enter complete information.";
        return false;
    }

    if (!isNaN(document.addForm.color.value) ||
        isNaN(document.addForm.width.value) ||
        isNaN(document.addForm.height.value) ||
        isNaN(document.addForm.borderWidth.value) ||
        document.addForm.width.value < 0 ||
        document.addForm.height.value < 0 ||
        document.addForm.borderWidth.value < 0) {
        document.getElementById("showError1").innerHTML = "Please check the information you entered.";
        return false;
    }
    return true;
}

function deleteCheck() {
    if (isNaN(document.deleteForm.id.value) ||
        document.deleteForm.id.value < 0) {
        document.getElementById("showError2").innerHTML = "Please check the information you entered.";
        return false;
    }
    return true;
}

