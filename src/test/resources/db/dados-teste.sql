insert into tb_conta values (1,1,0, current_timestamp , current_timestamp );
insert into tb_conta values (2,1,500, current_timestamp , current_timestamp );

insert into tb_cliente values (1,1, 'Cliente teste 1', '960.269.278-24' , current_timestamp, current_timestamp );
insert into tb_cliente values (2,2, 'Cliente teste 2', '515.101.391-25' , current_timestamp, current_timestamp );

insert into tb_conta_historico values(1, 2, 500, null, 'Teste carga', current_timestamp );
COMMIT;