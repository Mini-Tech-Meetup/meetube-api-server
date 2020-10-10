CREATE TABLE videos
(
    id              varchar(255)    not null,
    title           varchar(255)    not null,
    keywords        text[],
    caption         text,
    description     text,
    url             text,
    thumbnail_url   text,
    primary key (id)
);

CREATE index index_videos_keywords on videos using gin (keywords);

CREATE OR REPLACE FUNCTION fn_array_contains(left_array text[],
                                             right_array text[]) RETURNS boolean
AS
$$
BEGIN
    return left_array @> right_array;
END;
$$ LANGUAGE 'plpgsql'
