CREATE TABLE tb_conta (
  nr_conta INT AUTO_INCREMENT  PRIMARY KEY,
  digito int(1) NOT NULL DEFAULT 1,
  saldo NUMBER(16,2) not null DEFAULT 0,
  dt_criacao TIMESTAMP NOT NULL,
  dt_ultima_atualizacao TIMESTAMP NULL
);

CREATE TABLE tb_cliente (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  nr_conta INT NULL,
  nome VARCHAR(300) NOT NULL,
  cpf VARCHAR(14) NOT NULL,
  dt_criacao TIMESTAMP NOT NULL,
  dt_ultima_atualizacao TIMESTAMP NULL
);

ALTER TABLE tb_cliente add constraint UN_CLIENT_CPF unique(cpf);

CREATE TABLE tb_conta_historico (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  nr_conta INT NOT NULL,
  valor NUMBER(16,2) not null,
  taxa NUMBER(16,2),
  descricao VARCHAR(500),
  dt_acao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE tb_cliente
    ADD FOREIGN KEY (nr_conta)
    REFERENCES tb_conta(nr_conta);

ALTER TABLE tb_conta_historico
    ADD FOREIGN KEY (nr_conta)
    REFERENCES tb_conta(nr_conta);