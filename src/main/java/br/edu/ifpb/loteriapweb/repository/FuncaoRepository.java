package br.edu.ifpb.loteriapweb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ifpb.loteriapweb.model.Funcao;

@Repository
public interface FuncaoRepository extends JpaRepository<Funcao, Integer>  {

	List<Funcao> findByName(String name);
}
