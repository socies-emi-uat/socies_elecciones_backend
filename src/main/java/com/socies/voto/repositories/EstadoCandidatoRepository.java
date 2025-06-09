package com.socies.voto.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.socies.voto.models.EstadoCandidato;

@Repository
public interface EstadoCandidatoRepository extends JpaRepository<EstadoCandidato, Long> {

}
