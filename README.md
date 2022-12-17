# Filmorate. Кинопоиск для своих.
Социальная сеть, которая поможет выбрать кино на основе того, какие фильмы вы и ваши друзья смотрите и какие оценки им ставите.  

#### Api приложенния: 
 - *PUT /users/{id}/friends/{friendId} — добавление в друзья.*
 - *DELETE /users/{id}/friends/{friendId} — удаление из друзей.*
 - *GET /users/{id}/friends — возвращаем список пользователей, являющихся его друзьями.*
 - *GET /users/{id}/friends/common/{otherId} — список друзей, общих с другим пользователем.*
 - *PUT /films/{id}/like/{userId} — пользователь ставит лайк фильму.*
 - *DELETE /films/{id}/like/{userId} — пользователь удаляет лайк.*
 - *GET /films/popular?count={count} — возвращаем список из первых count фильмов по количеству лайков. Если значение параметра count не задано, вернется первые 10.*

![](QuickDBD-Filmorate.png)

#### Примеры SQL запросов:
1. Запрос фильма по id:

```sql
    SELECT * 
    FROM film 
    WHERE film_id=?;
```
2. Топ 10 фильмов
```sql
SELECT FILMS.FILM_ID, NAME, DESCRIPTION, RELEASEDATE, DURATION, MPA_ID, count(LIKES.user_id)
FROM films
LEFT JOIN likes on FILMS.FILM_ID = LIKES.FILM_ID
GROUP BY FILMS.FILM_ID
ORDER BY count(LIKES.USER_ID) desc
LIMIT ?;
```   
3. Запрос жанра фильма по id фильма
```sql
SELECT GENRES.GENRE_ID, NAME
FROM FILMS_GENRE
JOIN GENRES ON FILMS_GENRE.GENRE_ID = GENRES.GENRE_ID
WHERE FILMS_GENRE.FILM_ID=?;
``` 
4. Запрос всех друзей
```sql
SELECT FRIEND_ID, EMAIL, LOGIN, NAME, BIRTHDAY
FROM FRIENDS
JOIN USERS ON FRIENDS.FRIEND_ID = USERS.USER_ID
WHERE FRIENDS.USER_ID=?;
``` 



