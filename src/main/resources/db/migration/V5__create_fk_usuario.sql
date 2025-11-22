alter table transacao
    add column fk_id_usuario integer;

alter table transacao
    add constraint transacao_fk_id_usuario_fkey
    foreign key (fk_id_usuario) references usuario(id);

alter table conta
    add column fk_id_usuario integer;

alter table conta
    add constraint conta_fk_id_usuario_fkey
    foreign key (fk_id_usuario) references usuario(id);

alter table categoria
    add column fk_id_usuario integer;

alter table categoria
    add constraint categoria_fk_id_usuario_fkey
    foreign key (fk_id_usuario) references usuario(id);