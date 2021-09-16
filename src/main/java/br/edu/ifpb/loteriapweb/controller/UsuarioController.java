package br.edu.ifpb.loteriapweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.edu.ifpb.loteriapweb.model.Funcao;
import br.edu.ifpb.loteriapweb.model.Usuario;
import br.edu.ifpb.loteriapweb.repository.UsuarioRepository;

@Controller
@RequestMapping("registro")
public class UsuarioController {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@GetMapping
	public String show(Model model) {
		return "registro";
	}
	
	@PostMapping
	public String save(String username, String password, String email) {
		Usuario usuario = new Usuario();
		usuario.setUsername(username);
		usuario.setPassword(passwordEncoder.encode(password));
		usuario.setEmail(email);
		usuarioRepository.save(usuario);
		return "redirect:/home";
	}
	
}
