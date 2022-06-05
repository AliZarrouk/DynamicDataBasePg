## About
Using db "_cluster_" as storage. Sharding is done on an external key.
In this example, 3 postgres instances are used.

Inspiration: https://medium.com/innomizetech/dynamic-multi-database-application-with-spring-boot-7c61a743e914

## How to run
### App
```docker-compose up```

For each db :

```sql
-- Table: public.post

-- DROP TABLE IF EXISTS public.post;

CREATE TABLE IF NOT EXISTS public.post
(
    id bigint NOT NULL,
    extid character varying(50) COLLATE pg_catalog."default",
    name character varying(50) COLLATE pg_catalog."default",
    CONSTRAINT post_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.post
    OWNER to postgres;
```

```sql
-- SEQUENCE: public.hibernate_sequence

-- DROP SEQUENCE IF EXISTS public.hibernate_sequence;

CREATE SEQUENCE IF NOT EXISTS public.hibernate_sequence
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE public.hibernate_sequence
    OWNER TO postgres;
```

then run the app ```dmd.Application```

### post stuff
```shell
curl --location --request POST 'http://localhost:8080/post/?name=test'
```

### get stuff
```shell
curl --location --request GET 'http://localhost:8080/post/?id=SOME_UUID'
```