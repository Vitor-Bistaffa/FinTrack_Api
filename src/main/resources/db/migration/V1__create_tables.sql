create table categoria(
    id serial primary key
    ,nome varchar(50)
);

create table conta(
    id serial primary key
    ,nome varchar(50)
);

create type tipo_transacao as enum (
    'Despesa'
    ,'Receita'
);

create table transacao(
    id serial primary key
    ,fk_id_categoria integer
    ,foreign key (fk_id_categoria) references categoria(id)
    ,fk_id_conta integer
    ,foreign key (fk_id_conta) references conta(id)
    ,valor decimal(19,4)
    ,nome varchar(50)
    ,descricao text
    ,tipo tipo_transacao
    ,data date
    ,parcela integer
);

create table saldo(
    id serial primary key
    ,saldo_atual decimal(19,4)
    ,data date
);
