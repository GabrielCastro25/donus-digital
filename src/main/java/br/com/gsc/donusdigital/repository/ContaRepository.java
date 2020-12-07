package br.com.gsc.donusdigital.repository;

import br.com.gsc.donusdigital.model.Conta;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ContaRepository extends CrudRepository<Conta, Long> {

    @Query(value = "select ct from Conta ct inner join ct.cliente cli where cli.cpf = :cpf")
    Conta getPorCpfCliente(@Param("cpf") String cpfParam);

    @Query(value = "select ct from Conta ct inner join ct.cliente cli where ct.numeroConta = :nrConta AND cli.cpf = :cpf")
    Optional<Conta> getPorNumeroContaECpfCliente(@Param("nrConta") Long numeroConta, @Param("cpf") String cpfParam);
}
