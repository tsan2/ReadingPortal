API Endpoints

1.Регистрация пользователя
 - URL: /auth/register
 - Метод: POST
 - JSON (request body):
json
{
    "nickname" : "oleg",
    "email" : "oleg@gmail.com",
    "password" : "my_password"
}

2.Авторизация пользователя
- URL: /auth/login
- Метод: POST
- JSON (request body):
json
{
    "emailOrNickname" : "oleg",
    "password" : "my_password"
}

3.Выход из аккаунта
- URL: /auth/logout
- Метод: POST
