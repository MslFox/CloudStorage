### Здравствуйте, посмотрите работу пожалуйста, на предмет замечаний, дополнений и прочих ошибок.

Сейчас логика работы программы такова:
1. Пользователи хранятся в postgres в виде реализации UserDetails
2. POST login/ получаю логин пароль -> создаю пользователя в базе -> генерирую токен -> возращаю токен. В токен шифрую дату окончания, имя юзера, авторити(по умолчанию "ROLE_USER"). Имя юзера -> шифрую Base64. Для отладки сделал авто регистрацию при отсутствии пользователя в базе (настраивается в application.prooerties)
3. POST logout/ обрабатывает logoutHandler из filterChain -> в случае если токен еще "живой" , кладет токен в blackList -> чистит контекст -> посылает 200 OK. Также в отдельном потоке пробегает по blackList и чистит его от исживших токенов.
4. Файлы хранятся в файловой системе в корневой папке проекта
5. Получаю токен -> валидирую, расшифровываю токен -> кладу в контекст advice
6. POST, GET, DELETE, PUT работают с файловой системой. Получают имя юзера из контекста, по ним находят папку юзера. -> в случае c новым юзером, создается папка
7. Имя папок = имя юзера(зашифрованное Base64)
8. Исключения создаю сам свои DetectedException -> далее обрабатываю свои и  встроенные
9. По сути это два сервиса, которые взаимодействуют посредством зашифрованной в JWT информации. Моя идея была в том, что (сервис,БД авторизации) и (сервис,хранилище файлов) никаким образом не связаны между собой (за исключением общего секретного ключа токена)

### Вот, как то так. Прошу посмотреть код. Лежал в больнице -> Отстал от сроков.Теперь время очень поджимает. 30 марта срок сдачи. Если все норм, то добавляю Swagger, Тесты, Dockerfiles, liquibase(сейчас для отладки стоит "update") 
### Буду очень благодарен быстрому ответу
### Вопросы
1. При отправлке тела в том виде, который указан ниже, файл сохраняется в виде json. Приходится с сервера отправлять byte[], для "нормалього" сохранения файл, это противоречит указаной схеме. Я чего то не понимаю, не правильно делаю?
```
components:
    File:
      type: object
      properties:
        hash:
          type: string
        file:
          type: string
          format: binary
``` 
2. Также при POST file/ на серврер приходит мультипарт без значения "hash". Я чего то не понимаю, не правильно делаю?
 
 
  