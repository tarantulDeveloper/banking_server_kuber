CREATE TABLE client_accounts
(
    balance    numeric(38, 2),
    deleted    boolean NOT NULL,
    created_at timestamp(6) without time zone,
    id         bigint  NOT NULL,
    updated_at timestamp(6) without time zone,
    username    varchar(255)  NOT NULL,
    CONSTRAINT client_accounts_balance_check CHECK ((balance >= (0)::numeric))
);


CREATE SEQUENCE client_accounts_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE client_accounts_id_seq OWNED BY client_accounts.id;


ALTER TABLE ONLY client_accounts ALTER COLUMN id SET DEFAULT nextval('client_accounts_id_seq'::regclass);


ALTER TABLE ONLY client_accounts
    ADD CONSTRAINT client_accounts_pkey PRIMARY KEY (id);