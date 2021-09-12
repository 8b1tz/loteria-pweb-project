package br.edu.ifpb.loteriapweb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ifpb.loteriapweb.enums.StatusSorteio;
import br.edu.ifpb.loteriapweb.model.Sorteio;

@Repository
public interface SorteioRepository extends JpaRepository<Sorteio, Integer>{

	List<Sorteio> findByStatus(StatusSorteio status);



}
