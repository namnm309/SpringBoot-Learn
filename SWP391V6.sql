PGDMP                      }            SWP391V4    17.2    17.2 v    j           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                           false            k           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                           false            l           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                           false            m           1262    25709    SWP391V4    DATABASE     �   CREATE DATABASE "SWP391V4" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'English_United States.1252';
    DROP DATABASE "SWP391V4";
                     postgres    false            }           1247    25802    appointment_payment_status    TYPE     T   CREATE TYPE public.appointment_payment_status AS ENUM (
    'unpaid',
    'paid'
);
 -   DROP TYPE public.appointment_payment_status;
       public               postgres    false            z           1247    25795    appointment_status    TYPE     f   CREATE TYPE public.appointment_status AS ENUM (
    'in process',
    'completed',
    'cancelles'
);
 %   DROP TYPE public.appointment_status;
       public               postgres    false            h           1247    25711    gender_user    TYPE     E   CREATE TYPE public.gender_user AS ENUM (
    'male',
    'female'
);
    DROP TYPE public.gender_user;
       public               postgres    false            �           1247    25893    notification_status_enum    TYPE     R   CREATE TYPE public.notification_status_enum AS ENUM (
    'read',
    'unread'
);
 +   DROP TYPE public.notification_status_enum;
       public               postgres    false            �           1247    25846    payment_method    TYPE     Y   CREATE TYPE public.payment_method AS ENUM (
    'cash',
    'credit_card',
    'momo'
);
 !   DROP TYPE public.payment_method;
       public               postgres    false            k           1247    25716 	   role_user    TYPE     `   CREATE TYPE public.role_user AS ENUM (
    'admin',
    'staff',
    'customer',
    'child'
);
    DROP TYPE public.role_user;
       public               postgres    false            t           1247    25779    service_type    TYPE     U   CREATE TYPE public.service_type AS ENUM (
    'single',
    'combo',
    'modify'
);
    DROP TYPE public.service_type;
       public               postgres    false            �            1259    25974 	   demo1_seq    SEQUENCE     r   CREATE SEQUENCE public.demo1_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
     DROP SEQUENCE public.demo1_seq;
       public               postgres    false            �            1259    25808    tbl_appointments    TABLE     v  CREATE TABLE public.tbl_appointments (
    appointment_id integer NOT NULL,
    user_id integer,
    vaccine_id integer,
    appointment_date timestamp without time zone NOT NULL,
    status public.appointment_status DEFAULT 'in process'::public.appointment_status,
    payment_status public.appointment_payment_status DEFAULT 'unpaid'::public.appointment_payment_status
);
 $   DROP TABLE public.tbl_appointments;
       public         heap r       postgres    false    890    893    893    890            �            1259    25807 #   tbl_appointments_appointment_id_seq    SEQUENCE     �   CREATE SEQUENCE public.tbl_appointments_appointment_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 :   DROP SEQUENCE public.tbl_appointments_appointment_id_seq;
       public               postgres    false    224            n           0    0 #   tbl_appointments_appointment_id_seq    SEQUENCE OWNED BY     k   ALTER SEQUENCE public.tbl_appointments_appointment_id_seq OWNED BY public.tbl_appointments.appointment_id;
          public               postgres    false    223            �            1259    25872    tbl_feedback    TABLE     .  CREATE TABLE public.tbl_feedback (
    feedback_id integer NOT NULL,
    user_id integer,
    appointment_id integer,
    rating integer,
    comment text,
    time_created timestamp without time zone DEFAULT now(),
    CONSTRAINT tbl_feedback_rating_check CHECK (((rating >= 1) AND (rating <= 5)))
);
     DROP TABLE public.tbl_feedback;
       public         heap r       postgres    false            �            1259    25871    tbl_feedback_feedback_id_seq    SEQUENCE     �   CREATE SEQUENCE public.tbl_feedback_feedback_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 3   DROP SEQUENCE public.tbl_feedback_feedback_id_seq;
       public               postgres    false    230            o           0    0    tbl_feedback_feedback_id_seq    SEQUENCE OWNED BY     ]   ALTER SEQUENCE public.tbl_feedback_feedback_id_seq OWNED BY public.tbl_feedback.feedback_id;
          public               postgres    false    229            �            1259    25898    tbl_notifications    TABLE       CREATE TABLE public.tbl_notifications (
    notification_id integer NOT NULL,
    user_id integer,
    content text NOT NULL,
    sent_at timestamp without time zone DEFAULT now(),
    status public.notification_status_enum DEFAULT 'unread'::public.notification_status_enum
);
 %   DROP TABLE public.tbl_notifications;
       public         heap r       postgres    false    911    911            �            1259    25897 %   tbl_notifications_notification_id_seq    SEQUENCE     �   CREATE SEQUENCE public.tbl_notifications_notification_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 <   DROP SEQUENCE public.tbl_notifications_notification_id_seq;
       public               postgres    false    232            p           0    0 %   tbl_notifications_notification_id_seq    SEQUENCE OWNED BY     o   ALTER SEQUENCE public.tbl_notifications_notification_id_seq OWNED BY public.tbl_notifications.notification_id;
          public               postgres    false    231            �            1259    25914    tbl_otp    TABLE     (  CREATE TABLE public.tbl_otp (
    otp_id integer NOT NULL,
    phone character varying(15) NOT NULL,
    otp_code character varying(6) NOT NULL,
    created_at timestamp without time zone DEFAULT now(),
    expires_at timestamp without time zone NOT NULL,
    otp_status boolean DEFAULT false
);
    DROP TABLE public.tbl_otp;
       public         heap r       postgres    false            �            1259    25913    tbl_otp_otp_id_seq    SEQUENCE     �   CREATE SEQUENCE public.tbl_otp_otp_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 )   DROP SEQUENCE public.tbl_otp_otp_id_seq;
       public               postgres    false    234            q           0    0    tbl_otp_otp_id_seq    SEQUENCE OWNED BY     I   ALTER SEQUENCE public.tbl_otp_otp_id_seq OWNED BY public.tbl_otp.otp_id;
          public               postgres    false    233            �            1259    25854    tbl_payments    TABLE     	  CREATE TABLE public.tbl_payments (
    payment_id integer NOT NULL,
    user_id integer,
    appointment_id integer,
    amount numeric(10,2) NOT NULL,
    payment_date timestamp without time zone DEFAULT now(),
    method_payment public.payment_method NOT NULL
);
     DROP TABLE public.tbl_payments;
       public         heap r       postgres    false    902            �            1259    25853    tbl_payments_payment_id_seq    SEQUENCE     �   CREATE SEQUENCE public.tbl_payments_payment_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 2   DROP SEQUENCE public.tbl_payments_payment_id_seq;
       public               postgres    false    228            r           0    0    tbl_payments_payment_id_seq    SEQUENCE OWNED BY     [   ALTER SEQUENCE public.tbl_payments_payment_id_seq OWNED BY public.tbl_payments.payment_id;
          public               postgres    false    227            �            1259    26640    tbl_permission    TABLE     �   CREATE TABLE public.tbl_permission (
    permission_name character varying(255) NOT NULL,
    permission_description character varying(255)
);
 "   DROP TABLE public.tbl_permission;
       public         heap r       postgres    false            �            1259    26647 	   tbl_roles    TABLE     ~   CREATE TABLE public.tbl_roles (
    role_name character varying(255) NOT NULL,
    role_description character varying(255)
);
    DROP TABLE public.tbl_roles;
       public         heap r       postgres    false            �            1259    26654    tbl_roles_permissions    TABLE     �   CREATE TABLE public.tbl_roles_permissions (
    role_role_name character varying(255) NOT NULL,
    permissions_permission_name character varying(255) NOT NULL
);
 )   DROP TABLE public.tbl_roles_permissions;
       public         heap r       postgres    false            �            1259    25923    tbl_service_vaccines    TABLE     v   CREATE TABLE public.tbl_service_vaccines (
    id integer NOT NULL,
    service_id integer,
    vaccine_id integer
);
 (   DROP TABLE public.tbl_service_vaccines;
       public         heap r       postgres    false            �            1259    25922    tbl_service_vaccines_id_seq    SEQUENCE     �   CREATE SEQUENCE public.tbl_service_vaccines_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 2   DROP SEQUENCE public.tbl_service_vaccines_id_seq;
       public               postgres    false    236            s           0    0    tbl_service_vaccines_id_seq    SEQUENCE OWNED BY     [   ALTER SEQUENCE public.tbl_service_vaccines_id_seq OWNED BY public.tbl_service_vaccines.id;
          public               postgres    false    235            �            1259    25786    tbl_services    TABLE     �   CREATE TABLE public.tbl_services (
    service_id integer NOT NULL,
    service_name character varying(100) NOT NULL,
    description text,
    type public.service_type NOT NULL,
    price numeric(10,2) NOT NULL,
    date_create date
);
     DROP TABLE public.tbl_services;
       public         heap r       postgres    false    884            �            1259    25785    tbl_services_service_id_seq    SEQUENCE     �   CREATE SEQUENCE public.tbl_services_service_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 2   DROP SEQUENCE public.tbl_services_service_id_seq;
       public               postgres    false    222            t           0    0    tbl_services_service_id_seq    SEQUENCE OWNED BY     [   ALTER SEQUENCE public.tbl_services_service_id_seq OWNED BY public.tbl_services.service_id;
          public               postgres    false    221            �            1259    25726 	   tbl_users    TABLE     u  CREATE TABLE public.tbl_users (
    user_id bigint NOT NULL,
    parent_id bigint,
    username character varying(255) NOT NULL,
    fullname character varying(255),
    password character varying(255) NOT NULL,
    email character varying(255) NOT NULL,
    phone character varying(255) NOT NULL,
    birth_date timestamp(6) without time zone NOT NULL,
    gender character varying(255) NOT NULL,
    enabled boolean DEFAULT false NOT NULL,
    verification_cod character varying(255),
    verification_expired time(6) without time zone,
    verification_expiration time without time zone,
    roles character varying(255)[]
);
    DROP TABLE public.tbl_users;
       public         heap r       postgres    false            �            1259    26661    tbl_users_roles    TABLE        CREATE TABLE public.tbl_users_roles (
    user_user_id bigint NOT NULL,
    roles_role_name character varying(255) NOT NULL
);
 #   DROP TABLE public.tbl_users_roles;
       public         heap r       postgres    false            �            1259    25980    tbl_users_seq    SEQUENCE     w   CREATE SEQUENCE public.tbl_users_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 $   DROP SEQUENCE public.tbl_users_seq;
       public               postgres    false            �            1259    25725    tbl_users_user_id_seq    SEQUENCE     �   CREATE SEQUENCE public.tbl_users_user_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 ,   DROP SEQUENCE public.tbl_users_user_id_seq;
       public               postgres    false    218            u           0    0    tbl_users_user_id_seq    SEQUENCE OWNED BY     O   ALTER SEQUENCE public.tbl_users_user_id_seq OWNED BY public.tbl_users.user_id;
          public               postgres    false    217            �            1259    25827    tbl_vaccine_records    TABLE     �   CREATE TABLE public.tbl_vaccine_records (
    record_id integer NOT NULL,
    user_id integer,
    appointment_id integer,
    date_record date NOT NULL,
    reaction_record text
);
 '   DROP TABLE public.tbl_vaccine_records;
       public         heap r       postgres    false            �            1259    25826 !   tbl_vaccine_records_record_id_seq    SEQUENCE     �   CREATE SEQUENCE public.tbl_vaccine_records_record_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 8   DROP SEQUENCE public.tbl_vaccine_records_record_id_seq;
       public               postgres    false    226            v           0    0 !   tbl_vaccine_records_record_id_seq    SEQUENCE OWNED BY     g   ALTER SEQUENCE public.tbl_vaccine_records_record_id_seq OWNED BY public.tbl_vaccine_records.record_id;
          public               postgres    false    225            �            1259    25770    tbl_vaccines    TABLE     -  CREATE TABLE public.tbl_vaccines (
    vaccine_id integer NOT NULL,
    vaccine_name character varying(100) NOT NULL,
    description text,
    vaccine_age character varying(50),
    price numeric(10,2) NOT NULL,
    date_of_manufacture date NOT NULL,
    vaccine_expiry_date character varying(20)
);
     DROP TABLE public.tbl_vaccines;
       public         heap r       postgres    false            �            1259    25769    tbl_vaccines_vaccine_id_seq    SEQUENCE     �   CREATE SEQUENCE public.tbl_vaccines_vaccine_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 2   DROP SEQUENCE public.tbl_vaccines_vaccine_id_seq;
       public               postgres    false    220            w           0    0    tbl_vaccines_vaccine_id_seq    SEQUENCE OWNED BY     [   ALTER SEQUENCE public.tbl_vaccines_vaccine_id_seq OWNED BY public.tbl_vaccines.vaccine_id;
          public               postgres    false    219            �            1259    25975 	   users_seq    SEQUENCE     s   CREATE SEQUENCE public.users_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
     DROP SEQUENCE public.users_seq;
       public               postgres    false            z           2604    25994    tbl_appointments appointment_id    DEFAULT     �   ALTER TABLE ONLY public.tbl_appointments ALTER COLUMN appointment_id SET DEFAULT nextval('public.tbl_appointments_appointment_id_seq'::regclass);
 N   ALTER TABLE public.tbl_appointments ALTER COLUMN appointment_id DROP DEFAULT;
       public               postgres    false    224    223    224            �           2604    25995    tbl_feedback feedback_id    DEFAULT     �   ALTER TABLE ONLY public.tbl_feedback ALTER COLUMN feedback_id SET DEFAULT nextval('public.tbl_feedback_feedback_id_seq'::regclass);
 G   ALTER TABLE public.tbl_feedback ALTER COLUMN feedback_id DROP DEFAULT;
       public               postgres    false    230    229    230            �           2604    25996 !   tbl_notifications notification_id    DEFAULT     �   ALTER TABLE ONLY public.tbl_notifications ALTER COLUMN notification_id SET DEFAULT nextval('public.tbl_notifications_notification_id_seq'::regclass);
 P   ALTER TABLE public.tbl_notifications ALTER COLUMN notification_id DROP DEFAULT;
       public               postgres    false    231    232    232            �           2604    25997    tbl_otp otp_id    DEFAULT     p   ALTER TABLE ONLY public.tbl_otp ALTER COLUMN otp_id SET DEFAULT nextval('public.tbl_otp_otp_id_seq'::regclass);
 =   ALTER TABLE public.tbl_otp ALTER COLUMN otp_id DROP DEFAULT;
       public               postgres    false    233    234    234            ~           2604    25998    tbl_payments payment_id    DEFAULT     �   ALTER TABLE ONLY public.tbl_payments ALTER COLUMN payment_id SET DEFAULT nextval('public.tbl_payments_payment_id_seq'::regclass);
 F   ALTER TABLE public.tbl_payments ALTER COLUMN payment_id DROP DEFAULT;
       public               postgres    false    227    228    228            �           2604    26000    tbl_service_vaccines id    DEFAULT     �   ALTER TABLE ONLY public.tbl_service_vaccines ALTER COLUMN id SET DEFAULT nextval('public.tbl_service_vaccines_id_seq'::regclass);
 F   ALTER TABLE public.tbl_service_vaccines ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    236    235    236            y           2604    26001    tbl_services service_id    DEFAULT     �   ALTER TABLE ONLY public.tbl_services ALTER COLUMN service_id SET DEFAULT nextval('public.tbl_services_service_id_seq'::regclass);
 F   ALTER TABLE public.tbl_services ALTER COLUMN service_id DROP DEFAULT;
       public               postgres    false    222    221    222            v           2604    26006    tbl_users user_id    DEFAULT     v   ALTER TABLE ONLY public.tbl_users ALTER COLUMN user_id SET DEFAULT nextval('public.tbl_users_user_id_seq'::regclass);
 @   ALTER TABLE public.tbl_users ALTER COLUMN user_id DROP DEFAULT;
       public               postgres    false    218    217    218            }           2604    26003    tbl_vaccine_records record_id    DEFAULT     �   ALTER TABLE ONLY public.tbl_vaccine_records ALTER COLUMN record_id SET DEFAULT nextval('public.tbl_vaccine_records_record_id_seq'::regclass);
 L   ALTER TABLE public.tbl_vaccine_records ALTER COLUMN record_id DROP DEFAULT;
       public               postgres    false    226    225    226            x           2604    26004    tbl_vaccines vaccine_id    DEFAULT     �   ALTER TABLE ONLY public.tbl_vaccines ALTER COLUMN vaccine_id SET DEFAULT nextval('public.tbl_vaccines_vaccine_id_seq'::regclass);
 F   ALTER TABLE public.tbl_vaccines ALTER COLUMN vaccine_id DROP DEFAULT;
       public               postgres    false    220    219    220            T          0    25808    tbl_appointments 
   TABLE DATA           y   COPY public.tbl_appointments (appointment_id, user_id, vaccine_id, appointment_date, status, payment_status) FROM stdin;
    public               postgres    false    224   ��       Z          0    25872    tbl_feedback 
   TABLE DATA           k   COPY public.tbl_feedback (feedback_id, user_id, appointment_id, rating, comment, time_created) FROM stdin;
    public               postgres    false    230   �       \          0    25898    tbl_notifications 
   TABLE DATA           _   COPY public.tbl_notifications (notification_id, user_id, content, sent_at, status) FROM stdin;
    public               postgres    false    232   `�       ^          0    25914    tbl_otp 
   TABLE DATA           ^   COPY public.tbl_otp (otp_id, phone, otp_code, created_at, expires_at, otp_status) FROM stdin;
    public               postgres    false    234   ۜ       X          0    25854    tbl_payments 
   TABLE DATA           q   COPY public.tbl_payments (payment_id, user_id, appointment_id, amount, payment_date, method_payment) FROM stdin;
    public               postgres    false    228   +�       d          0    26640    tbl_permission 
   TABLE DATA           Q   COPY public.tbl_permission (permission_name, permission_description) FROM stdin;
    public               postgres    false    240   ��       e          0    26647 	   tbl_roles 
   TABLE DATA           @   COPY public.tbl_roles (role_name, role_description) FROM stdin;
    public               postgres    false    241   ��       f          0    26654    tbl_roles_permissions 
   TABLE DATA           \   COPY public.tbl_roles_permissions (role_role_name, permissions_permission_name) FROM stdin;
    public               postgres    false    242   ��       `          0    25923    tbl_service_vaccines 
   TABLE DATA           J   COPY public.tbl_service_vaccines (id, service_id, vaccine_id) FROM stdin;
    public               postgres    false    236   %�       R          0    25786    tbl_services 
   TABLE DATA           g   COPY public.tbl_services (service_id, service_name, description, type, price, date_create) FROM stdin;
    public               postgres    false    222   Q�       N          0    25726 	   tbl_users 
   TABLE DATA           �   COPY public.tbl_users (user_id, parent_id, username, fullname, password, email, phone, birth_date, gender, enabled, verification_cod, verification_expired, verification_expiration, roles) FROM stdin;
    public               postgres    false    218   ��       g          0    26661    tbl_users_roles 
   TABLE DATA           H   COPY public.tbl_users_roles (user_user_id, roles_role_name) FROM stdin;
    public               postgres    false    243   p�       V          0    25827    tbl_vaccine_records 
   TABLE DATA           o   COPY public.tbl_vaccine_records (record_id, user_id, appointment_id, date_record, reaction_record) FROM stdin;
    public               postgres    false    226   ��       P          0    25770    tbl_vaccines 
   TABLE DATA           �   COPY public.tbl_vaccines (vaccine_id, vaccine_name, description, vaccine_age, price, date_of_manufacture, vaccine_expiry_date) FROM stdin;
    public               postgres    false    220   �       x           0    0 	   demo1_seq    SEQUENCE SET     8   SELECT pg_catalog.setval('public.demo1_seq', 1, false);
          public               postgres    false    237            y           0    0 #   tbl_appointments_appointment_id_seq    SEQUENCE SET     Q   SELECT pg_catalog.setval('public.tbl_appointments_appointment_id_seq', 2, true);
          public               postgres    false    223            z           0    0    tbl_feedback_feedback_id_seq    SEQUENCE SET     J   SELECT pg_catalog.setval('public.tbl_feedback_feedback_id_seq', 2, true);
          public               postgres    false    229            {           0    0 %   tbl_notifications_notification_id_seq    SEQUENCE SET     S   SELECT pg_catalog.setval('public.tbl_notifications_notification_id_seq', 2, true);
          public               postgres    false    231            |           0    0    tbl_otp_otp_id_seq    SEQUENCE SET     @   SELECT pg_catalog.setval('public.tbl_otp_otp_id_seq', 1, true);
          public               postgres    false    233            }           0    0    tbl_payments_payment_id_seq    SEQUENCE SET     I   SELECT pg_catalog.setval('public.tbl_payments_payment_id_seq', 2, true);
          public               postgres    false    227            ~           0    0    tbl_service_vaccines_id_seq    SEQUENCE SET     I   SELECT pg_catalog.setval('public.tbl_service_vaccines_id_seq', 3, true);
          public               postgres    false    235                       0    0    tbl_services_service_id_seq    SEQUENCE SET     I   SELECT pg_catalog.setval('public.tbl_services_service_id_seq', 2, true);
          public               postgres    false    221            �           0    0    tbl_users_seq    SEQUENCE SET     =   SELECT pg_catalog.setval('public.tbl_users_seq', 101, true);
          public               postgres    false    239            �           0    0    tbl_users_user_id_seq    SEQUENCE SET     D   SELECT pg_catalog.setval('public.tbl_users_user_id_seq', 29, true);
          public               postgres    false    217            �           0    0 !   tbl_vaccine_records_record_id_seq    SEQUENCE SET     O   SELECT pg_catalog.setval('public.tbl_vaccine_records_record_id_seq', 2, true);
          public               postgres    false    225            �           0    0    tbl_vaccines_vaccine_id_seq    SEQUENCE SET     I   SELECT pg_catalog.setval('public.tbl_vaccines_vaccine_id_seq', 2, true);
          public               postgres    false    219            �           0    0 	   users_seq    SEQUENCE SET     9   SELECT pg_catalog.setval('public.users_seq', 151, true);
          public               postgres    false    238            �           2606    25815 &   tbl_appointments tbl_appointments_pkey 
   CONSTRAINT     p   ALTER TABLE ONLY public.tbl_appointments
    ADD CONSTRAINT tbl_appointments_pkey PRIMARY KEY (appointment_id);
 P   ALTER TABLE ONLY public.tbl_appointments DROP CONSTRAINT tbl_appointments_pkey;
       public                 postgres    false    224            �           2606    25881    tbl_feedback tbl_feedback_pkey 
   CONSTRAINT     e   ALTER TABLE ONLY public.tbl_feedback
    ADD CONSTRAINT tbl_feedback_pkey PRIMARY KEY (feedback_id);
 H   ALTER TABLE ONLY public.tbl_feedback DROP CONSTRAINT tbl_feedback_pkey;
       public                 postgres    false    230            �           2606    25907 (   tbl_notifications tbl_notifications_pkey 
   CONSTRAINT     s   ALTER TABLE ONLY public.tbl_notifications
    ADD CONSTRAINT tbl_notifications_pkey PRIMARY KEY (notification_id);
 R   ALTER TABLE ONLY public.tbl_notifications DROP CONSTRAINT tbl_notifications_pkey;
       public                 postgres    false    232            �           2606    25921    tbl_otp tbl_otp_pkey 
   CONSTRAINT     V   ALTER TABLE ONLY public.tbl_otp
    ADD CONSTRAINT tbl_otp_pkey PRIMARY KEY (otp_id);
 >   ALTER TABLE ONLY public.tbl_otp DROP CONSTRAINT tbl_otp_pkey;
       public                 postgres    false    234            �           2606    25860    tbl_payments tbl_payments_pkey 
   CONSTRAINT     d   ALTER TABLE ONLY public.tbl_payments
    ADD CONSTRAINT tbl_payments_pkey PRIMARY KEY (payment_id);
 H   ALTER TABLE ONLY public.tbl_payments DROP CONSTRAINT tbl_payments_pkey;
       public                 postgres    false    228            �           2606    26646 "   tbl_permission tbl_permission_pkey 
   CONSTRAINT     m   ALTER TABLE ONLY public.tbl_permission
    ADD CONSTRAINT tbl_permission_pkey PRIMARY KEY (permission_name);
 L   ALTER TABLE ONLY public.tbl_permission DROP CONSTRAINT tbl_permission_pkey;
       public                 postgres    false    240            �           2606    26660 0   tbl_roles_permissions tbl_roles_permissions_pkey 
   CONSTRAINT     �   ALTER TABLE ONLY public.tbl_roles_permissions
    ADD CONSTRAINT tbl_roles_permissions_pkey PRIMARY KEY (role_role_name, permissions_permission_name);
 Z   ALTER TABLE ONLY public.tbl_roles_permissions DROP CONSTRAINT tbl_roles_permissions_pkey;
       public                 postgres    false    242    242            �           2606    26653    tbl_roles tbl_roles_pkey 
   CONSTRAINT     ]   ALTER TABLE ONLY public.tbl_roles
    ADD CONSTRAINT tbl_roles_pkey PRIMARY KEY (role_name);
 B   ALTER TABLE ONLY public.tbl_roles DROP CONSTRAINT tbl_roles_pkey;
       public                 postgres    false    241            �           2606    25928 .   tbl_service_vaccines tbl_service_vaccines_pkey 
   CONSTRAINT     l   ALTER TABLE ONLY public.tbl_service_vaccines
    ADD CONSTRAINT tbl_service_vaccines_pkey PRIMARY KEY (id);
 X   ALTER TABLE ONLY public.tbl_service_vaccines DROP CONSTRAINT tbl_service_vaccines_pkey;
       public                 postgres    false    236            �           2606    25793    tbl_services tbl_services_pkey 
   CONSTRAINT     d   ALTER TABLE ONLY public.tbl_services
    ADD CONSTRAINT tbl_services_pkey PRIMARY KEY (service_id);
 H   ALTER TABLE ONLY public.tbl_services DROP CONSTRAINT tbl_services_pkey;
       public                 postgres    false    222            �           2606    26087    tbl_users tbl_users_email_key 
   CONSTRAINT     Y   ALTER TABLE ONLY public.tbl_users
    ADD CONSTRAINT tbl_users_email_key UNIQUE (email);
 G   ALTER TABLE ONLY public.tbl_users DROP CONSTRAINT tbl_users_email_key;
       public                 postgres    false    218            �           2606    26098    tbl_users tbl_users_phone_key 
   CONSTRAINT     Y   ALTER TABLE ONLY public.tbl_users
    ADD CONSTRAINT tbl_users_phone_key UNIQUE (phone);
 G   ALTER TABLE ONLY public.tbl_users DROP CONSTRAINT tbl_users_phone_key;
       public                 postgres    false    218            �           2606    26008    tbl_users tbl_users_pkey 
   CONSTRAINT     [   ALTER TABLE ONLY public.tbl_users
    ADD CONSTRAINT tbl_users_pkey PRIMARY KEY (user_id);
 B   ALTER TABLE ONLY public.tbl_users DROP CONSTRAINT tbl_users_pkey;
       public                 postgres    false    218            �           2606    26665 $   tbl_users_roles tbl_users_roles_pkey 
   CONSTRAINT     }   ALTER TABLE ONLY public.tbl_users_roles
    ADD CONSTRAINT tbl_users_roles_pkey PRIMARY KEY (user_user_id, roles_role_name);
 N   ALTER TABLE ONLY public.tbl_users_roles DROP CONSTRAINT tbl_users_roles_pkey;
       public                 postgres    false    243    243            �           2606    26100     tbl_users tbl_users_username_key 
   CONSTRAINT     _   ALTER TABLE ONLY public.tbl_users
    ADD CONSTRAINT tbl_users_username_key UNIQUE (username);
 J   ALTER TABLE ONLY public.tbl_users DROP CONSTRAINT tbl_users_username_key;
       public                 postgres    false    218            �           2606    25834 ,   tbl_vaccine_records tbl_vaccine_records_pkey 
   CONSTRAINT     q   ALTER TABLE ONLY public.tbl_vaccine_records
    ADD CONSTRAINT tbl_vaccine_records_pkey PRIMARY KEY (record_id);
 V   ALTER TABLE ONLY public.tbl_vaccine_records DROP CONSTRAINT tbl_vaccine_records_pkey;
       public                 postgres    false    226            �           2606    25777    tbl_vaccines tbl_vaccines_pkey 
   CONSTRAINT     d   ALTER TABLE ONLY public.tbl_vaccines
    ADD CONSTRAINT tbl_vaccines_pkey PRIMARY KEY (vaccine_id);
 H   ALTER TABLE ONLY public.tbl_vaccines DROP CONSTRAINT tbl_vaccines_pkey;
       public                 postgres    false    220            �           2606    26671 1   tbl_roles_permissions fk39se7p5bdvew7n2xy6ndoucqo    FK CONSTRAINT     �   ALTER TABLE ONLY public.tbl_roles_permissions
    ADD CONSTRAINT fk39se7p5bdvew7n2xy6ndoucqo FOREIGN KEY (role_role_name) REFERENCES public.tbl_roles(role_name);
 [   ALTER TABLE ONLY public.tbl_roles_permissions DROP CONSTRAINT fk39se7p5bdvew7n2xy6ndoucqo;
       public               postgres    false    241    242    4775            �           2606    26681 +   tbl_users_roles fkcvc82fmgkf6aplcv4tni833kw    FK CONSTRAINT     �   ALTER TABLE ONLY public.tbl_users_roles
    ADD CONSTRAINT fkcvc82fmgkf6aplcv4tni833kw FOREIGN KEY (user_user_id) REFERENCES public.tbl_users(user_id);
 U   ALTER TABLE ONLY public.tbl_users_roles DROP CONSTRAINT fkcvc82fmgkf6aplcv4tni833kw;
       public               postgres    false    4751    243    218            �           2606    26666 1   tbl_roles_permissions fkjajpgvwkdr5qc3okyf6m68cid    FK CONSTRAINT     �   ALTER TABLE ONLY public.tbl_roles_permissions
    ADD CONSTRAINT fkjajpgvwkdr5qc3okyf6m68cid FOREIGN KEY (permissions_permission_name) REFERENCES public.tbl_permission(permission_name);
 [   ALTER TABLE ONLY public.tbl_roles_permissions DROP CONSTRAINT fkjajpgvwkdr5qc3okyf6m68cid;
       public               postgres    false    242    240    4773            �           2606    26676 +   tbl_users_roles fkovijofsyuytw8xll2andf8e4b    FK CONSTRAINT     �   ALTER TABLE ONLY public.tbl_users_roles
    ADD CONSTRAINT fkovijofsyuytw8xll2andf8e4b FOREIGN KEY (roles_role_name) REFERENCES public.tbl_roles(role_name);
 U   ALTER TABLE ONLY public.tbl_users_roles DROP CONSTRAINT fkovijofsyuytw8xll2andf8e4b;
       public               postgres    false    241    243    4775            �           2606    26019 .   tbl_appointments tbl_appointments_user_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.tbl_appointments
    ADD CONSTRAINT tbl_appointments_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.tbl_users(user_id) ON DELETE CASCADE;
 X   ALTER TABLE ONLY public.tbl_appointments DROP CONSTRAINT tbl_appointments_user_id_fkey;
       public               postgres    false    224    4751    218            �           2606    25821 1   tbl_appointments tbl_appointments_vaccine_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.tbl_appointments
    ADD CONSTRAINT tbl_appointments_vaccine_id_fkey FOREIGN KEY (vaccine_id) REFERENCES public.tbl_vaccines(vaccine_id) ON DELETE CASCADE;
 [   ALTER TABLE ONLY public.tbl_appointments DROP CONSTRAINT tbl_appointments_vaccine_id_fkey;
       public               postgres    false    224    4755    220            �           2606    25887 -   tbl_feedback tbl_feedback_appointment_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.tbl_feedback
    ADD CONSTRAINT tbl_feedback_appointment_id_fkey FOREIGN KEY (appointment_id) REFERENCES public.tbl_appointments(appointment_id) ON DELETE CASCADE;
 W   ALTER TABLE ONLY public.tbl_feedback DROP CONSTRAINT tbl_feedback_appointment_id_fkey;
       public               postgres    false    230    4759    224            �           2606    26034 &   tbl_feedback tbl_feedback_user_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.tbl_feedback
    ADD CONSTRAINT tbl_feedback_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.tbl_users(user_id) ON DELETE CASCADE;
 P   ALTER TABLE ONLY public.tbl_feedback DROP CONSTRAINT tbl_feedback_user_id_fkey;
       public               postgres    false    230    218    4751            �           2606    26039 0   tbl_notifications tbl_notifications_user_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.tbl_notifications
    ADD CONSTRAINT tbl_notifications_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.tbl_users(user_id) ON DELETE CASCADE;
 Z   ALTER TABLE ONLY public.tbl_notifications DROP CONSTRAINT tbl_notifications_user_id_fkey;
       public               postgres    false    4751    232    218            �           2606    25866 -   tbl_payments tbl_payments_appointment_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.tbl_payments
    ADD CONSTRAINT tbl_payments_appointment_id_fkey FOREIGN KEY (appointment_id) REFERENCES public.tbl_appointments(appointment_id) ON DELETE CASCADE;
 W   ALTER TABLE ONLY public.tbl_payments DROP CONSTRAINT tbl_payments_appointment_id_fkey;
       public               postgres    false    224    4759    228            �           2606    26029 &   tbl_payments tbl_payments_user_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.tbl_payments
    ADD CONSTRAINT tbl_payments_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.tbl_users(user_id) ON DELETE CASCADE;
 P   ALTER TABLE ONLY public.tbl_payments DROP CONSTRAINT tbl_payments_user_id_fkey;
       public               postgres    false    228    4751    218            �           2606    25929 9   tbl_service_vaccines tbl_service_vaccines_service_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.tbl_service_vaccines
    ADD CONSTRAINT tbl_service_vaccines_service_id_fkey FOREIGN KEY (service_id) REFERENCES public.tbl_services(service_id) ON DELETE CASCADE;
 c   ALTER TABLE ONLY public.tbl_service_vaccines DROP CONSTRAINT tbl_service_vaccines_service_id_fkey;
       public               postgres    false    222    236    4757            �           2606    25934 9   tbl_service_vaccines tbl_service_vaccines_vaccine_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.tbl_service_vaccines
    ADD CONSTRAINT tbl_service_vaccines_vaccine_id_fkey FOREIGN KEY (vaccine_id) REFERENCES public.tbl_vaccines(vaccine_id) ON DELETE CASCADE;
 c   ALTER TABLE ONLY public.tbl_service_vaccines DROP CONSTRAINT tbl_service_vaccines_vaccine_id_fkey;
       public               postgres    false    220    236    4755            �           2606    26053 "   tbl_users tbl_users_parent_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.tbl_users
    ADD CONSTRAINT tbl_users_parent_id_fkey FOREIGN KEY (parent_id) REFERENCES public.tbl_users(user_id);
 L   ALTER TABLE ONLY public.tbl_users DROP CONSTRAINT tbl_users_parent_id_fkey;
       public               postgres    false    4751    218    218            �           2606    25840 ;   tbl_vaccine_records tbl_vaccine_records_appointment_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.tbl_vaccine_records
    ADD CONSTRAINT tbl_vaccine_records_appointment_id_fkey FOREIGN KEY (appointment_id) REFERENCES public.tbl_appointments(appointment_id) ON DELETE CASCADE;
 e   ALTER TABLE ONLY public.tbl_vaccine_records DROP CONSTRAINT tbl_vaccine_records_appointment_id_fkey;
       public               postgres    false    224    226    4759            �           2606    26024 4   tbl_vaccine_records tbl_vaccine_records_user_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.tbl_vaccine_records
    ADD CONSTRAINT tbl_vaccine_records_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.tbl_users(user_id) ON DELETE CASCADE;
 ^   ALTER TABLE ONLY public.tbl_vaccine_records DROP CONSTRAINT tbl_vaccine_records_user_id_fkey;
       public               postgres    false    218    4751    226            T   G   x�3�4�4�4202�50�54U04�2 !��<�������b�Ҽ���.#�Z#�Zc]CK+cjc���� ��x      Z   m   x�3�4�4�4�ty��;9C����
Ew--Q(y�{b�"��������������������������%�P��	�_��Ey
e��W�)�ed>���}xM^�>�1z\\\ R&      \   k   x�3�4�tz�ka�B���
9ww'g(d<ܵ3O�$��\������/�T0202�50�54�1L,���L-���,�-9K�RS��H6�X����c���� �!<�      ^   @   x�]ɱ� �ڞ"���Y���is���PS9(��4�j`C^�Ml�}kտ���Ow�j-      X   O   x�3�4�4�45 =N##S]#]K+#+S=sC#KsK��Ԕ̒��Ģ.#�6#Nsb�����s��qqq �X#      d   '   x�st���rut�JMLQH�THL���S������ ��`      e   )   x�v�-N-R(��I�rt����tL��̃��qqq ؀
�      f      x�st����t��A��.\1z\\\ @�	      `      x�3�4�4�2�4��@҈+F��� !��      R   _   x�3�N-*�LNU0�ty��;9C����
%��W�*�<ܵ��83/='��� ��,#]#]C.#�v#����I���M����)W� �%'�      N   �  x���[o�0��ͯ�w�vb'��i�KK�@(��$�3$���%+�#m%6�R>Y��<y��]'|>��gQ�Ll^#ǯ�wq�~�	�
} �J�"cb� )!R����s�sO�y���Ȁ�,���>����&qR�� ,+D�p�e �ܶ�p_4�N_��)���v���Ӑ���Q��9��w�-�u�é\/'\����rg��.�����ގ's�I\ѵ��8��;�_�e�7;*׎�G�P�1v&�y"�W"�&�3�::��HN��Ǿ#�ڶq���uu�w�2TY{����,��Άҧ�"�@�nWLG
����oah����90]�2d�am�$��&��_����|P(�1��8w���Q���1�Ľ���Y%�=ue�T(��
����mN��Cc�*	Dv%�n*��>�MiN!��r�f��6=�1�U���P�����q*�wzY����4ꃅ��h<�UsV6t&'��_��ln׵��Ӌ���7V���Zv�md�YO�+՟�c����v�7����nL⎡v�VuՉ׮�R�vYg�6�J����1��6���l��GH��7�A���u~Z>?�"x2Im)���)�0�8�PŒ��L]�p�W���q؏���j%/�~���0���KMH�B�7G��      g      x�3��tt����2��v����� 5LL      V   S   x�3�4�4�4202�50�54���8�%/]!��f������)<ܽ2/������X�������L�⇻'�(������� �o      P   g   x�3�KLN��KUp��=�E����
ep!#]S���ĢbNS�30�4202�50"N#������b.#�9N��8q�C�1G6�X��L�Ѐ��fN� UW*�     