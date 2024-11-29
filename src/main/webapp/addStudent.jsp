<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Information Form</title>
    <link rel="stylesheet" href="form.css">

</head>
<body>
<div class="form-container">
    <a href="./" class="submit-btn">home page</a>
    <h2>Student Information Form</h2>
    <form action="./${requestScope['action']}" method="post">
        <label for="student-id">Student ID</label>
        <input type="number" id="student-id" name="student-id" value="${requestScope['student-id']}"
        ${requestScope['action'] == 'update-student' ? 'readonly' : ''} required>

        <label for="first-name">First Name</label>
        <input type="text" id="first-name" pattern="[A-Za-z]*" name="first-name" value="${requestScope['first-name']}" required>

        <label for="last-name">Last Name</label>
        <input type="text" id="last-name" pattern="[A-Za-z]*" name="last-name" value="${requestScope['last-name']}" required>


        <label for="gender">Gender</label>
        <select id="gender" name="gender" required>
            <option value="" >Select gender</option>
            <option value="male" ${requestScope['gender'] == 'male' ? 'selected' : ''}>Male</option>
            <option value="female" ${requestScope['gender'] == 'female' ? 'selected' : ''}>Female</option>
        </select>

        <label for="level">Level</label>
        <input type="number" id="level" name="level" value="${requestScope['level']}" required  min="1" max="7">

        <label for="gpa">GPA</label>
        <input type="number" step="0.01" id="gpa" name="gpa" value="${requestScope['gpa']}" required min="0" max="4.0">

        <label for="address">Address</label>
        <input type="text" id="address" pattern="[A-Za-z]*" name="address" value="${requestScope['address']}" required>
        <button type="submit" class="submit-btn">Submit</button>
    </form>
</div>
</body>
</html>