alter table transacao
alter column tipo type varchar
using tipo::text;

drop type tipo_transacao;
