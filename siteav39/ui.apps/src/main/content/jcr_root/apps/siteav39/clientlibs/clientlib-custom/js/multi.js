var Employees = {
    'AEM-software': [
        'Developer',
        'Trainee',
        'Architect',
        'Engineer'
    ],
    Finance:[
        'Developer',
        'Trainee',
        'Architect',
        'Engineer'
    ],
    Enterprise:[
        'Developer',
        'Trainee',
        'Architect',
        'Engineer'
    ],
    Analysts:[
        'Developer',
        'Trainee',
        'Architect',
        'Engineer'
    ],
}



function showSubcategoryDropdown() {
    debugger
    const EmployeeCategories = Object.keys(Employees)
    var mainDropdown = document.getElementById("mainDropdown");
    console.log(mainDropdown.value)
    var subDropdownContainer = document.getElementById("subDropdownContainer");
    var subDropdown = document.getElementById("subDropdown")
  }