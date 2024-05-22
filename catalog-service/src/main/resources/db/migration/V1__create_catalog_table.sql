create table catalogs
(
    deleted boolean NOT NULL,
    created_at timestamp(6),
    username varchar(255),
    id bigint NOT NULL,
    updated_at timestamp(6)
);

CREATE SEQUENCE catalogs_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE catalogs_id_seq OWNED BY catalogs.id;

ALTER TABLE ONLY catalogs ALTER COLUMN id SET DEFAULT nextval('catalogs_id_seq'::regclass);

ALTER TABLE ONLY catalogs
    ADD CONSTRAINT catalogs_pkey PRIMARY KEY (id);
