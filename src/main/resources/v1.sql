

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
	birthday date NULL,
	email varchar NOT NULL,
	"password" varchar NOT NULL,
	id varchar NOT NULL,
	CONSTRAINT users_pk PRIMARY KEY (id),
	CONSTRAINT users_unique UNIQUE (email)
);

INSERT INTO public.users (name,email,"password",id)
VALUES ('admin','admin@admin.admin','12345','e7950adc-d438-41c2-aa19-4eb5470543d4')

CREATE TABLE public.users_roles (
	id int8 GENERATED ALWAYS AS IDENTITY NOT NULL,
	user_id varchar NOT NULL,
	role_id int8 NOT NULL,
	CONSTRAINT users_roles_pk PRIMARY KEY (id),
	CONSTRAINT users_roles_roles_fk FOREIGN KEY (role_id) REFERENCES public.roles(id),
	CONSTRAINT users_roles_users_fk FOREIGN KEY (user_id) REFERENCES public.users(id)
);

INSERT INTO public.users_roles (user_id,role_id)
VALUES ('e7950adc-d438-41c2-aa19-4eb5470543d4', 2)

CREATE TABLE public.category (
	id varchar NOT NULL,
	"name" varchar NOT NULL,
	description varchar NULL,
	CONSTRAINT categories_pk PRIMARY KEY (id)
);

CREATE TABLE public.item (
	id varchar NOT NULL,
	"name" varchar NOT NULL,
	description varchar NULL,
	category varchar NOT NULL,
	CONSTRAINT item_unique UNIQUE (id),
	CONSTRAINT item_category_fk FOREIGN KEY (category) REFERENCES public.category(id)
);

CREATE TABLE public.rate (
	id varchar NOT NULL,
	"comment" varchar NOT NULL,
	item varchar NOT NULL,
	user_id varchar NOT NULL,
	ispositive bool NULL,
	isapproved bool NULL,
	CONSTRAINT rate_unique UNIQUE (id),
	CONSTRAINT rate_item_fk FOREIGN KEY (item) REFERENCES public.item(id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT rate_users_fk FOREIGN KEY (user_id) REFERENCES public.users(id)
);

CREATE TABLE public.pages (
	id varchar NOT NULL,
	"name" varchar NOT NULL,
	"desc" varchar NULL,
	category varchar NOT NULL,
	CONSTRAINT pages_unique UNIQUE (id),
	CONSTRAINT pages_unique_1 UNIQUE (name),
	CONSTRAINT pages_categories_fk FOREIGN KEY (category) REFERENCES public.category(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE public.pages_items (
	id int8 GENERATED ALWAYS AS IDENTITY NOT NULL,
	pages_id varchar NOT NULL,
	items_id varchar NOT NULL,
	CONSTRAINT pages_items_pk PRIMARY KEY (id),
	CONSTRAINT pages_items_item_fk FOREIGN KEY (items_id) REFERENCES public.item(id),
	CONSTRAINT pages_items_pages_fk FOREIGN KEY (pages_id) REFERENCES public.pages(id)
);
