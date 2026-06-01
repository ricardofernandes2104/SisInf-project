/*
 * ISEL-DEI-SisInf
 * ND 2022-2026
 *
 *   
 * Information Systems Project - Active Databases
 * Didactic material to support 
 * the Information Systems course
 * 
 *  * */

/* ### DO NOT CHANGE OR REMOVE THE MARKERS BELOW 
 * ### ONLY WRITE to THE TODO ZONE
 * ### */


-- region Question 1.a 
CREATE OR REPLACE FUNCTION validar_nif() RETURNS TRIGGER AS $$
DECLARE
sum_value INT := 0;
	check_digit INT;
	i INT;
	nif_text TEXT;
BEGIN
	nif_text := NEW.nif;
	IF nif_text !~ '^[0-9]+$' OR length(nif_text) != 9 THEN
		RAISE EXCEPTION 'NIF inválido';
END IF;
FOR i IN 1..8 LOOP
		sum_value := sum_value + (CAST(SUBSTRING(nif_text, i, 1) AS INT) * (10 - i));
END LOOP;
	check_digit := 11 - (sum_value % 11);
	IF check_digit >= 10 THEN
		check_digit := 0;
END IF;
	IF check_digit != CAST(SUBSTRING(nif_text, 9, 1) AS INT) THEN
		RAISE EXCEPTION 'NIF inválido';
END IF;

RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE TRIGGER validar_nif_trigger
BEFORE INSERT OR UPDATE ON cliente
                               FOR EACH ROW EXECUTE FUNCTION validar_nif();

INSERT INTO cliente (nif, cartao_cidadao, nome) values ('100000002', '13523688 5 ZX4', 'Tiago Sequeira da Silva');
INSERT INTO cliente (nif, cartao_cidadao, nome) values ('123456788', '13323677 6 ZX7', 'Ricardo Simão');

-- endregion

-- region Question 1.b
CREATE OR REPLACE FUNCTION validar_email_duplicado() RETURNS TRIGGER AS $$
BEGIN
	IF EXISTS(
	SELECT 1 FROM contacto_email WHERE cliente_nif = NEW.cliente_nif
	AND email = NEW.email) THEN
		RAISE EXCEPTION 'Email Duplicado';
END IF;

RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION validar_telefone_duplicado() RETURNS TRIGGER AS $$
BEGIN
	IF EXISTS(
	SELECT 1 FROM contacto_telefone WHERE cliente_nif = NEW.cliente_nif
	AND telefone = NEW.telefone) THEN
		RAISE EXCEPTION 'Telefone Duplicado';
END IF;

RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE TRIGGER validar_email_duplicado_trigger
BEFORE INSERT OR UPDATE ON contacto_email
                               FOR EACH ROW EXECUTE FUNCTION validar_email_duplicado();

CREATE OR REPLACE TRIGGER validar_telefone_duplicado_trigger
BEFORE INSERT OR UPDATE ON contacto_telefone
                            FOR EACH ROW EXECUTE FUNCTION validar_telefone_duplicado();

INSERT INTO contacto_email (cliente_nif, descricao, email) VALUES ('214587963', 'Email pessoal', 'joao.ferreira@pessoal.pt');
INSERT INTO contacto_email (cliente_nif, descricao, email) VALUES ('214587963', 'Email pessoal', 'tago.silva@pessoal.ru');

INSERT INTO contacto_telefone (cliente_nif, descricao, telefone) VALUES ('214587963', 'Telemóvel', '+351912345678');
INSERT INTO contacto_telefone (cliente_nif, descricao, telefone) VALUES ('214587963', 'Telemóvel', '+351937306666');

-- endregion

-- region Question 2
CREATE OR REPLACE FUNCTION fx_media_movel(days integer, p_instrumento_isin VARCHAR(12)) RETURNS NUMERIC AS $$
DECLARE
media NUMERIC(15,2);
BEGIN
SELECT ROUND(AVG(valor_fecho), 2) INTO media FROM (
                                                      SELECT valor_fecho FROM valor_instrumento_diario
                                                      WHERE instrumento_isin = p_instrumento_isin
                                                      ORDER BY valor_instrumento_diario."data" DESC LIMIT days
                                                  ) sub;

RETURN media;
END;
$$ LANGUAGE plpgsql;

SELECT * FROM fx_media_movel(3, 'PTGAL0AM0009');



-- endregion

-- region Question 3
CREATE OR REPLACE FUNCTION fx_portefolio_info(portfolio_id BIGINT)
RETURNS TABLE (
	isin VARCHAR(12),
	quantidade NUMERIC(15, 2),
	valor_actual NUMERIC(15, 2),
	percentagem_variacao NUMERIC(7,2)
)
AS $$
BEGIN
RETURN QUERY
SELECT p.instrumento_isin, p.quantidade, df.valor_actual, df.percentagem_variacao_diaria
FROM posicao p JOIN dados_fundamentais df ON p.instrumento_isin = df.instrumento_isin
WHERE p.portefolio = portfolio_id;
END;
$$ LANGUAGE plpgsql;

SELECT * FROM fx_portefolio_info(1);


-- endregion

-- region Question 4
CREATE OR REPLACE PROCEDURE p_actualizaValorDiario() AS $$
BEGIN
WITH dados_diarios AS (
    SELECT
        identificador AS instrumento_isin,
        data_tempo::DATE AS "data",
            MIN(valor) AS valor_minimo,
        MAX(valor) AS valor_maximo,
        (ARRAY_AGG(valor ORDER BY data_tempo ASC))[1] AS valor_abertura,
    (ARRAY_AGG(valor ORDER BY data_tempo DESC))[1] AS valor_fecho
FROM triplo_externo te INNER JOIN instrumento i ON i.instrumento_id = te.identificador
GROUP BY identificador, data_tempo::DATE
    )
INSERT INTO valor_instrumento_diario (instrumento_isin, "data", valor_minimo, valor_maximo, valor_abertura, valor_fecho)
SELECT instrumento_isin, "data", valor_minimo, valor_maximo, valor_abertura, valor_fecho FROM dados_diarios
    ON CONFLICT (instrumento_isin, "data")
	DO UPDATE SET
    valor_minimo = LEAST(valor_instrumento_diario.valor_minimo, EXCLUDED.valor_minimo),
               valor_maximo = GREATEST(valor_instrumento_diario.valor_maximo, EXCLUDED.valor_maximo),
               valor_fecho = EXCLUDED.valor_fecho;
END;
$$ LANGUAGE plpgsql;

CALL p_actualizaValorDiario();

SELECT * FROM valor_instrumento_diario;

-- endregion

-- region Question 5
CREATE OR REPLACE VIEW contacto_cliente(nif,cartao_cidadao,nome,tipo_contacto,contacto,descricao) AS
SELECT c.nif, c.cartao_cidadao, c.nome, 'EMAIL', ce.email, ce.descricao
FROM cliente c JOIN contacto_email ce ON ce.cliente_nif = c.nif
UNION ALL
SELECT c.nif, c.cartao_cidadao, c.nome, 'TELEFONE', ct.telefone, ct.descricao
FROM cliente c JOIN contacto_telefone ct ON ct.cliente_nif = c.nif;

CREATE OR REPLACE FUNCTION contacto_cliente_insert() RETURNS TRIGGER AS $$
BEGIN
INSERT INTO cliente VALUES (NEW.nif, NEW.cartao_cidadao, NEW.nome) ON CONFLICT (nif) DO NOTHING;
IF NEW.tipo_contacto = 'EMAIL'
		THEN INSERT INTO contacto_email (cliente_nif, descricao, email) VALUES (NEW.nif, NEW.descricao, NEW.contacto);
	ELSIF NEW.tipo_contacto = 'TELEFONE'
		THEN INSERT INTO contacto_telefone (cliente_nif, descricao, telefone) VALUES (NEW.nif, NEW.descricao, NEW.contacto);
ELSE
		RAISE EXCEPTION 'tipo_contacto inválido.';
END IF;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER trg_contacto_cliente_insert
INSTEAD OF INSERT ON contacto_cliente FOR EACH ROW EXECUTE FUNCTION contacto_cliente_insert();

CREATE OR REPLACE FUNCTION contacto_cliente_update() RETURNS TRIGGER AS $$
BEGIN
UPDATE cliente SET cartao_cidadao = NEW.cartao_cidadao, nome = NEW.nome WHERE nif = OLD.nif;
IF OLD.tipo_contacto = 'EMAIL'
		THEN UPDATE contacto_email SET email = NEW.contacto, descricao = NEW.descricao WHERE cliente_nif = OLD.nif AND email = OLD.contacto;
ELSE
UPDATE contacto_telefone SET telefone = NEW.contacto, descricao = NEW.descricao WHERE cliente_nif = OLD.nif AND telefone = OLD.contacto;
END IF;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER trg_contacto_cliente_update
INSTEAD OF UPDATE ON contacto_cliente FOR EACH ROW EXECUTE FUNCTION contacto_cliente_update();

CREATE OR REPLACE FUNCTION contacto_cliente_delete() RETURNS TRIGGER AS $$
BEGIN
	IF OLD.tipo_contacto = 'EMAIL'
		THEN DELETE FROM contacto_email WHERE cliente_nif = OLD.nif AND email = OLD.contacto;
ELSE
DELETE FROM contacto_telefone WHERE cliente_nif = OLD.nif AND telefone = OLD.contacto;
END IF;
RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER trg_contacto_cliente_delete
INSTEAD OF DELETE ON contacto_cliente FOR EACH ROW EXECUTE FUNCTION contacto_cliente_delete();

INSERT INTO contacto_cliente (
    nif,
    cartao_cidadao,
    nome,
    tipo_contacto,
    contacto,
    descricao
)
VALUES (
           '501964843',
           '19876543 2 ZX1',
           'Bernardo Ferreira',
           'EMAIL',
           'brnrd@email.pt',
           'Pessoal'
       );

SELECT * FROM cliente WHERE nif = '501964843';
SELECT * FROM contacto_email WHERE cliente_nif = '501964843';

INSERT INTO contacto_cliente (
    nif,
    cartao_cidadao,
    nome,
    tipo_contacto,
    contacto,
    descricao
)
VALUES (
           '501964843',
           '19876543 2 ZX1',
           'Bernardo Ferreira',
           'TELEFONE',
           '912345678',
           'Telemóvel'
       );

SELECT * FROM contacto_telefone WHERE cliente_nif = '501964843';

UPDATE contacto_cliente SET contacto = 'novo@email.pt', descricao = 'Email principal'
WHERE nif = '501964843' AND tipo_contacto = 'EMAIL';

SELECT * FROM contacto_email WHERE cliente_nif = '501964843';

DELETE FROM contacto_cliente WHERE nif = '501964843' AND tipo_contacto = 'EMAIL';
DELETE FROM contacto_cliente WHERE nif = '501964843' AND tipo_contacto = 'TELEFONE';

SELECT * FROM contacto_email WHERE cliente_nif = '501964843';
-- endregion

-- region Other changes
--TODO
-- endregion
