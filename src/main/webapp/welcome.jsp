<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Welcome</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f0f0f0;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }
        .welcome-container {
            background-color: #ffffff;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            text-align: center;
        }
        .welcome-container h1 {
            font-size: 24px;
            margin-bottom: 10px;
        }
        .welcome-container p {
            font-size: 18px;
            color: #555;
        }
    </style>
</head>
<body>
    <div class="welcome-container">
        <h1>Welcome, <%= request.getAttribute("firstName") %> <%= request.getAttribute("lastName") %>!</h1>
        <p>Your ID: <%= request.getAttribute("id") %></p>
    </div>
</body>
</html>
