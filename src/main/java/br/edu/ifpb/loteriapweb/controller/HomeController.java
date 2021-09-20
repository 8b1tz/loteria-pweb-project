package br.edu.ifpb.loteriapweb.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import br.edu.ifpb.loteriapweb.enums.StatusSorteio;
import br.edu.ifpb.loteriapweb.model.Usuario;
import br.edu.ifpb.loteriapweb.repository.SorteioRepository;
import br.edu.ifpb.loteriapweb.repository.UsuarioRepository;

@RequestMapping("/home")
@Controller
public class HomeController {

	@Autowired
	private SorteioRepository sorteioRepository;
	@Autowired
	private UsuarioRepository usuarioRepository;
	@GetMapping
	public String home(Model m, Principal principal) {
		m.addAttribute("sorteios", sorteioRepository.findAll());
		Usuario usuario = usuarioRepository.findByUsername(principal.getName());
		m.addAttribute("usuario", usuario);
		return "home";
	}

	@GetMapping("/{status}")
	public String aberto(@PathVariable String status, Model m, Principal principal) {
		Usuario usuario = usuarioRepository.findByUsername(principal.getName());
		m.addAttribute("usuario", usuario);
		m.addAttribute("sorteios", sorteioRepository.findByStatus(StatusSorteio.valueOf(status.toUpperCase())));
		m.addAttribute("status", status);
		return "home";
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public String onError() {
		return "redirect:/home";
	}
}
