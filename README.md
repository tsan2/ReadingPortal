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

4.Посмотреть профиль
- URL: /user/me
- Метод: GET

5.Список пользователей
- URL: /user
- Метод: GET
- Query params: size, page

6.Информация о пользователе по id
- URL: /user/{id}
- Метод: GET

7.Информация о пользователе по нику
- URL: /user
- Метод: GET
- Query params: nickname

8.Сменить пароль по старому паролю
- URL: /user/me/change-password
- Метод: PATCH
- JSON (request body):
json
{
   "oldPassword": "my_password",
   "newPassword" : "my_secret_password"
}

9.Сменить никнейм
- URL: /user/me/change-nickname
- Метод: PATCH
- JSON (request body):
json
{
   "nickname" : "oleg123"
}

10.сменить пароль по коду
1) - URL: /auth/forgot-password
- Метод: POST
- JSON (request body):
json
{
  "email" : "oleg@gmail.com"
}

2) - URL: /auth/reset-password
- Метод: POST
- JSON (request body):
json
{
  "email" : "oleg@gmail.com",
  "code" : "68350",
  "newPassword" : "my_password"
}