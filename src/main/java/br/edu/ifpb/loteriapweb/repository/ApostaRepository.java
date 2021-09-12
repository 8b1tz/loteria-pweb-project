package br.edu.ifpb.loteriapweb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpb.loteriapweb.enums.StatusAposta;
import br.edu.ifpb.loteriapweb.model.Aposta;

public interface ApostaRepository extends JpaRepository<Aposta, Long> {

	List<Aposta> findByStatus(StatusAposta status);

}
