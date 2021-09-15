package br.edu.ifpb.loteriapweb.service;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.edu.ifpb.loteriapweb.model.Funcao;
import br.edu.ifpb.loteriapweb.model.Usuario;
import br.edu.ifpb.loteriapweb.repository.UsuarioRepository;

@Service

public class UsuarioServiceImp implements UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepository.findByUsername(username);
		if (usuario == null) {
			throw new UsernameNotFoundException("invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(usuario.getUsername(), usuario.getPassword(),
				 mapRolesToAuthorities(usuario.getFuncao()));
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Funcao> funcoes) {
		return funcoes.stream().map(f -> new SimpleGrantedAuthority(f.getName())).collect(Collectors.toList());

	}
}
