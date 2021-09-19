package br.edu.ifpb.loteriapweb.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import br.edu.ifpb.loteriapweb.model.Aposta;
import br.edu.ifpb.loteriapweb.model.Usuario;
import br.edu.ifpb.loteriapweb.repository.ApostaRepository;
import br.edu.ifpb.loteriapweb.repository.UsuarioRepository;

@Controller
public class UsuarioController {

	@Autowired
	private ApostaRepository apostaRepository;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@GetMapping("registro")
	public String show(Model model, Usuario usuario) {
		return "registro";
	}
	
	@PostMapping("registro")
	public String save(@Valid Usuario usuario, BindingResult resultadoValidacao) {
		if (resultadoValidacao.hasErrors()) {
			return "registro";
		}
		usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
		usuarioRepository.save(usuario);
		return "redirect:/home";
	}
	
	@GetMapping("favoritos")
	public String showFavoritos(Model model) {

			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			List<Aposta> apostas = usuarioRepository.findByUsername(username).getApostasFavoritas();
			System.out.println(apostas);	
			model.addAttribute("apostas", apostas);
			
			return "usuario/favoritos";
			
		}	
	
	@PostMapping("sorteio/{idsorteio}/aposta/{idaposta}/favoritar")
	public String saveFavoritos(ModelAndView mv,@PathVariable Integer idsorteio, @PathVariable Integer idaposta) {
			System.out.print(idsorteio + " x  " + idaposta);
			List<Aposta> apostas = apostaRepository.findAll();
			apostas.forEach(p -> System.out.println(p));
			
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			Usuario usuario = usuarioRepository.findByUsername(username);
			Aposta apostinha = apostaRepository.getById(idaposta);
			
			if(usuario.getApostasFavoritas().contains(apostinha)) {
				usuario.removerApostasFavoritas(apostinha);
				apostinha.setIsFavorito(false);
				usuarioRepository.save(usuario);
			}else {
			apostinha.setIsFavorito(true);
			usuario.adicionarApostasFavoritas(apostinha);
			usuarioRepository.save(usuario);
			}
			return "redirect:/sorteio/"+idsorteio+"/apostas";
		}	
}
