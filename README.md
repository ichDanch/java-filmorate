# Filmorate

![](QuickDBD-Filmorate.png)

1. Запрос фильма по id:

```sql
    SELECT * 
    FROM film 
    WHERE film_id=?;
```
2. Топ 10 фильмов:

```sql
    SELECT f.film_id, 
           COUNT(l.user_id)
    FROM film AS f
    INNER JOIN likes AS l ON f.film_id=l.film_id
    ORDER BY COUNT(l.user_id) DESC
    LIMIT 10;
```