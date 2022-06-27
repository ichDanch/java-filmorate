MERGE INTO genres key (GENRE_ID) values (1, 'Комедия');
MERGE INTO genres key (GENRE_ID) values (2, 'Драма');
MERGE INTO genres key (GENRE_ID) values (3, 'Мультфильм');
MERGE INTO genres key (GENRE_ID) values (4, 'Триллер');
MERGE INTO genres key (GENRE_ID) values (5, 'Документальный');
MERGE INTO genres key (GENRE_ID) values (6, 'Боевик');

MERGE INTO MPA_RATING key (MPA_ID) values (1, 'G');     --  у фильма нет возрастных ограничений
MERGE INTO MPA_RATING key (MPA_ID) values (2, 'PG');    -- детям рекомендуется смотреть фильм с родителями
MERGE INTO MPA_RATING key (MPA_ID) values (3, 'PG-13'); -- детям до 13 лет просмотр не желателен
MERGE INTO MPA_RATING key (MPA_ID) values (4, 'R');     --  лицам до 17 лет просматривать фильм можно только в присутствии взрослого
MERGE INTO MPA_RATING key (MPA_ID) values (5, 'NC-17'); -- лицам до 18 лет просмотр запрещён
