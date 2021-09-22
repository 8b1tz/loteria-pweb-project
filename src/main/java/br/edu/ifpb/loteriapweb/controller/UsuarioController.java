package br.edu.ifpb.loteriapweb.controller;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import br.edu.ifpb.loteriapweb.enums.StatusAposta;
import br.edu.ifpb.loteriapweb.model.Aposta;
import br.edu.ifpb.loteriapweb.model.Funcao;
import br.edu.ifpb.loteriapweb.model.Sorteio;
import br.edu.ifpb.loteriapweb.model.Usuario;
import br.edu.ifpb.loteriapweb.repository.ApostaRepository;
import br.edu.ifpb.loteriapweb.repository.FuncaoRepository;
import br.edu.ifpb.loteriapweb.repository.SorteioRepository;
import br.edu.ifpb.loteriapweb.repository.UsuarioRepository;

@Controller
public class UsuarioController {

	@Autowired
	private FuncaoRepository funcaoRepository;
	@Autowired
	private SorteioRepository sorteioRepository;
	@Autowired
	private ApostaRepository apostaRepository;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private UsuarioRepository usuarioRepository;

	@GetMapping("registro")
	public ModelAndView show(ModelAndView mv, Usuario usuario) {
		mv.setViewName("registro");
		return mv;
	}

	@PostMapping("registro")
	public ModelAndView save(@Valid Usuario user, BindingResult resultadoValidacao, ModelAndView mv) {
		if (resultadoValidacao.hasErrors()) {
			mv.setViewName("registro");
			return mv;
		}
		Usuario usuario;
		Collection<Funcao> funcoes = funcaoRepository.findByName("ROLE_USER");
		if (funcoes.isEmpty()) {
			usuario = new Usuario(100.0, user.getUsername(), passwordEncoder.encode(user.getPassword()),
					user.getEmail(), Arrays.asList(new Funcao("ROLE_USER")));
		} else {
			usuario = new Usuario(100.0, user.getUsername(), passwordEncoder.encode(user.getPassword()),
					user.getEmail(), funcoes);
		}
		usuarioRepository.save(usuario);
		mv.setViewName("redirect:/home");
		return mv;
	}

	@GetMapping("favoritos")
	public ModelAndView showFavoritos(ModelAndView mv) {

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Usuario usuario = usuarioRepository.findByUsername(username);
		mv.addObject(usuario);
		List<Aposta> apostas = usuarioRepository.findByUsername(username).getApostasFavoritas();
		System.out.println(apostas);
		mv.addObject("apostas", apostas);
		mv.setViewName("usuario/favoritos");
		return mv;

	}

	@GetMapping("sorteio/{idsorteio}/favoritos")
	public ModelAndView showFavoritos(ModelAndView mv, @PathVariable Integer idsorteio) {

		Sorteio sorteio = sorteioRepository.findById(idsorteio).get();
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		List<Aposta> apostas = usuarioRepository.findByUsername(username).getApostasFavoritas();
		mv.addObject("apostas", apostas);
		mv.addObject("sorteio", sorteio);
		mv.addObject("usuario", usuarioRepository.findByUsername(username));
		mv.setViewName("usuario/favoritos");
		return mv;

	}

	@PostMapping("sorteio/{idsorteio}/favoritos/{idaposta}")
	public String sorteioFavoritoCriar(@PathVariable Integer idaposta, ModelAndView mv,
			@PathVariable Integer idsorteio) {

		Sorteio sorteio = sorteioRepository.findById(idsorteio).get();
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Usuario usuario = usuarioRepository.findByUsername(username);
		Aposta aposta = apostaRepository.findById(idaposta).get();
		List<Integer> numeros = aposta.getNumeros();
		Aposta apostinha = new Aposta();
		numeros.forEach(n -> apostinha.adicionarNumero(n));
		
		Integer quantidade = numeros.size();
		
		if (quantidade == 6) {
			usuario.setDinheiro(usuario.getDinheiro() - 3.00);
		}

		if (quantidade == 7) {
			usuario.setDinheiro(usuario.getDinheiro() - 15.00);

		}

		if (quantidade == 8) {
			usuario.setDinheiro(usuario.getDinheiro() - 90.00);

		}

		if (quantidade == 9) {
			usuario.setDinheiro(usuario.getDinheiro() - 300.00);

		}

		if (quantidade == 10) {
			usuario.setDinheiro(usuario.getDinheiro() - 1500.00);
		}

		apostinha.setIsFavorito(false);
		apostinha.setSorteio(sorteio);
		apostinha.setStatus(StatusAposta.PARTICIPANDO);
		apostinha.setUsuario(usuario);

		sorteio.adicionarAposta(apostinha);
		apostaRepository.save(apostinha);

		return "redirect:/sorteio/" + idsorteio + "/apostas/";

	}

	@PostMapping("sorteio/{idsorteio}/aposta/{idaposta}/favoritar")
	public String saveFavoritos(ModelAndView mv, @PathVariable Integer idsorteio, @PathVariable Integer idaposta) {

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Usuario usuario = usuarioRepository.findByUsername(username);
		Aposta apostinha = apostaRepository.getById(idaposta);

		if (usuario.getApostasFavoritas().contains(apostinha)) {
			usuario.removerApostasFavoritas(apostinha);
			apostinha.setIsFavorito(false);
			usuarioRepository.save(usuario);
		} else {
			apostinha.setIsFavorito(true);
			usuario.adicionarApostasFavoritas(apostinha);
			usuarioRepository.save(usuario);
		}
		return "redirect:/sorteio/" + idsorteio + "/apostas";
	}
}
