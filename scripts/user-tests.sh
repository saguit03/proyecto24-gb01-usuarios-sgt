echo -ne "\n Running user tests"

echo -ne "\n\n Creating a test user \n\n"

curl -X POST http://localhost:8081/Medifli/users \
-H "Content-Type: application/json" \
-H "Accept: application/json" \
-d '{
    "username": "testuser",
    "password": "password123",
    "email": "test@example.com",
    "name": "John",
    "surname": "Doe"
}'


echo -ne "\n\n Getting user \n\n"

curl -X GET http://localhost:8081/Medifli/users/1 \
-H "Accept: application/json"


echo -ne "\n\n Creating other test user \n\n"

curl -X POST http://localhost:8081/Medifli/users \
-H "Content-Type: application/json" \
-H "Accept: application/json" \
-d '{
    "username": "testuser2",
    "password": "password123",
    "email": "test2@example.com",
    "name": "John2",
    "surname": "Doe2"
}'


echo -ne "\n\n Getting all users \n\n"

curl -X GET http://localhost:8081/Medifli/users \
-H "Accept: application/json"

echo -ne "\n\n Updating user \n\n"

curl -X PUT http://localhost:8081/Medifli/users/1 \
-H "Content-Type: application/json" \
-H "Accept: application/json" \
-d '{
    "idUser": 1,
    "username": "updateduser",
    "name": "John",
    "surname": "Doe",
    "password": "newpassword123",
    "email": "updated@example.com"
}'

echo -ne "\n\n Getting updated user \n\n"

curl -X GET http://localhost:8081/Medifli/users/1 \
-H "Accept: application/json"

echo -ne "\n\n Deleting FIRST test user \n\n"

curl -X DELETE http://localhost:8081/Medifli/users/1 \
-H "Accept: application/json"

echo -ne "\n\n Getting all users \n\n"

curl -X GET http://localhost:8081/Medifli/users \
-H "Accept: application/json"
