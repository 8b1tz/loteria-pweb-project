package br.edu.ifpb.loteriapweb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpb.loteriapweb.enums.StatusAposta;
import br.edu.ifpb.loteriapweb.model.Aposta;
import br.edu.ifpb.loteriapweb.model.Usuario;

public interface ApostaRepository extends JpaRepository<Aposta, Integer> {

	List<Aposta> findByStatus(StatusAposta status);

	List<Aposta> findByUsuario(Usuario usuario);
}
