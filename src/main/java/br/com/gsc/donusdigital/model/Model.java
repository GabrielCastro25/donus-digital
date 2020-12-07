package br.com.gsc.donusdigital.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public abstract class Model implements Serializable {

    @Column(name = "dt_criacao", nullable = false)
    @CreationTimestamp
    private LocalDateTime dataCriacao;

    @Column(name = "dt_ultima_atualizacao")
    @UpdateTimestamp
    private LocalDateTime dataUltimaAtualizacao;
}
