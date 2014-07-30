-- Para criar a base
-- $ sudo -u postgres psql -f create_db.sql postgres

--
-- PostgreSQL database dump
--

-- Started on 2014-07-30 12:05:20 BRT

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

--
-- TOC entry 1776 (class 1262 OID 11564)
-- Name: postgres; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE postgres WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'pt_BR.UTF-8' LC_CTYPE = 'pt_BR.UTF-8';


ALTER DATABASE postgres OWNER TO postgres;

\connect postgres

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 140 (class 1259 OID 16393)
-- Dependencies: 6
-- Name: sms; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE sms (
    id bigint NOT NULL,
    message character varying(160) NOT NULL,
    phone_from character varying(20),
    name_from character varying(20),
    phone_to character varying(20) NOT NULL,
    name_to character varying(20)
);


ALTER TABLE public.sms OWNER TO postgres;

--
-- TOC entry 141 (class 1259 OID 16396)
-- Dependencies: 6 140
-- Name: sms_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE sms_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.sms_id_seq OWNER TO postgres;

--
-- TOC entry 1779 (class 0 OID 0)
-- Dependencies: 141
-- Name: sms_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE sms_id_seq OWNED BY sms.id;


--
-- TOC entry 1771 (class 2604 OID 16398)
-- Dependencies: 141 140
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sms ALTER COLUMN id SET DEFAULT nextval('sms_id_seq'::regclass);


--
-- TOC entry 1773 (class 2606 OID 16400)
-- Dependencies: 140 140
-- Name: pk_id_sms; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sms
    ADD CONSTRAINT pk_id_sms PRIMARY KEY (id);


--
-- TOC entry 1778 (class 0 OID 0)
-- Dependencies: 6
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2014-07-30 12:05:20 BRT

--
-- PostgreSQL database dump complete
--

