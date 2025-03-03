CREATE TABLE public.roles (
	id int8 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
	"name" varchar NOT NULL,
	CONSTRAINT roles_pk PRIMARY KEY (id)
);

INSERT INTO public.roles ("name") VALUES('ROLE_USER');
INSERT INTO public.roles ("name") VALUES('ROLE_ADMIN');
INSERT INTO public.roles ("name") VALUES('ROLE_APPROVED');

CREATE TABLE public.users (
	"name" varchar NOT NULL,
	birthday varchar NULL,
	email varchar NOT NULL,
	"password" varchar NOT NULL,
	id varchar NOT NULL,
	created_at varchar NOT NULL,
	updated_at varchar NULL,
	avatar_image varchar NULL,
	CONSTRAINT users_pk PRIMARY KEY (id),
	CONSTRAINT users_unique UNIQUE (email),
	CONSTRAINT users_unique_1 UNIQUE (name)
);

INSERT INTO public.users (name,email,"password",id)
VALUES ('admin','admin@admin.admin','$2a$10$PjzqckvISEBy1DGTdkmWa./IFUrTtbBB4i9IYTKDjV7l3CDDPQNY2','e7950adc-d438-41c2-aa19-4eb5470543d4')

CREATE TABLE public.users_roles (
	id int8 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
	user_id varchar NOT NULL,
	role_id int8 NOT NULL,
	CONSTRAINT users_roles_pk PRIMARY KEY (id),
	CONSTRAINT users_roles_roles_fk FOREIGN KEY (role_id) REFERENCES public.roles(id),
	CONSTRAINT users_roles_users_fk FOREIGN KEY (user_id) REFERENCES public.users(id)
);

INSERT INTO public.users_roles (user_id,role_id)
VALUES ('e7950adc-d438-41c2-aa19-4eb5470543d4', 2)

CREATE TABLE public.status (
	id int4 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1 NO CYCLE) NOT NULL,
	"name" varchar NOT NULL,
	CONSTRAINT status_pk PRIMARY KEY (id)
);

INSERT INTO public.status (name)
VALUES ('WAIT_APPROVE')
VALUES ('APPROVED')
VALUES ('CANCELED')

CREATE TABLE public.category (
	id varchar NOT NULL,
	"name" varchar NOT NULL,
	description varchar NULL,
	created_at varchar NOT NULL,
	updated_at varchar NULL,
	created_by varchar NOT NULL,
	CONSTRAINT categories_pk PRIMARY KEY (id),
	CONSTRAINT category_users_fk FOREIGN KEY (created_by) REFERENCES public.users(id)
);

CREATE TABLE public.item (
	id varchar NOT NULL,
	"name" varchar NOT NULL,
	description varchar NULL,
	status int4 NOT NULL,
	created_at varchar NOT NULL,
	updated_at varchar NULL,
	created_by varchar NOT NULL,
	CONSTRAINT item_pk PRIMARY KEY (id),
	CONSTRAINT item_unique UNIQUE (id),
	CONSTRAINT item_status_fk FOREIGN KEY (status) REFERENCES public.status(id),
	CONSTRAINT item_users_fk FOREIGN KEY (created_by) REFERENCES public.users(id)
);

CREATE TABLE public.item_category (
	id int8 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
	item_id varchar NOT NULL,
	category_id varchar NOT NULL,
	CONSTRAINT item_category_pk PRIMARY KEY (id),
	CONSTRAINT item_category_category_fk FOREIGN KEY (category_id) REFERENCES public.category(id) ON DELETE CASCADE,
	CONSTRAINT item_category_item_fk FOREIGN KEY (item_id) REFERENCES public.item(id)
);

CREATE TABLE public.rate (
	id varchar NOT NULL,
	"comment" varchar NOT NULL,
	item varchar NOT NULL,
	user_id varchar NOT NULL,
	ispositive bool NULL,
	status int4 NOT NULL,
	category_id varchar DEFAULT '1'::character varying NOT NULL,
	created_at varchar NOT NULL,
	updated_at varchar NULL,
	CONSTRAINT rate_pk PRIMARY KEY (id),
	CONSTRAINT rate_unique UNIQUE (id),
	CONSTRAINT rate_category_fk FOREIGN KEY (category_id) REFERENCES public.category(id) ON DELETE CASCADE,
	CONSTRAINT rate_item_fk FOREIGN KEY (item) REFERENCES public.item(id) ON DELETE CASCADE,
	CONSTRAINT rate_status_fk FOREIGN KEY (status) REFERENCES public.status(id),
	CONSTRAINT rate_users_fk FOREIGN KEY (user_id) REFERENCES public.users(id)
);

CREATE TABLE public.pages (
	id varchar NOT NULL,
	title varchar NOT NULL,
	description_pages varchar NULL,
	category varchar NOT NULL,
	status int4 NOT NULL,
	created_at varchar NOT NULL,
	updated_at varchar NULL,
	created_by varchar NOT NULL,
	CONSTRAINT pages_pk PRIMARY KEY (id),
	CONSTRAINT pages_unique UNIQUE (id),
	CONSTRAINT pages_unique_1 UNIQUE (title),
	CONSTRAINT pages_categories_fk FOREIGN KEY (category) REFERENCES public.category(id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT pages_status_fk FOREIGN KEY (status) REFERENCES public.status(id),
	CONSTRAINT pages_users_fk FOREIGN KEY (created_by) REFERENCES public.users(id)
);

CREATE TABLE public.pages_items (
	id int8 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
	pages_id varchar NOT NULL,
	items_id varchar NOT NULL,
	CONSTRAINT pages_items_pk PRIMARY KEY (id),
	CONSTRAINT pages_items_item_fk FOREIGN KEY (items_id) REFERENCES public.item(id) ON UPDATE CASCADE,
	CONSTRAINT pages_items_pages_fk FOREIGN KEY (pages_id) REFERENCES public.pages(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE public.action_history (
	user_id varchar NOT NULL,
	history varchar NOT NULL,
	admin_info varchar NULL,
	id int4 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1 NO CYCLE) NOT NULL,
	CONSTRAINT action_history_pk PRIMARY KEY (id),
	CONSTRAINT action_history_users_fk FOREIGN KEY (user_id) REFERENCES public.users(id)
);

CREATE TABLE public.settings (
	"name" varchar NOT NULL,
	value varchar NOT NULL
);
