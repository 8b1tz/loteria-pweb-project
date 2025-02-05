package br.edu.ifpb.loteriapweb.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.edu.ifpb.loteriapweb.enums.StatusAposta;
import br.edu.ifpb.loteriapweb.enums.StatusSorteio;
import br.edu.ifpb.loteriapweb.model.Aposta;
import br.edu.ifpb.loteriapweb.model.Sorteio;
import br.edu.ifpb.loteriapweb.model.Usuario;
import br.edu.ifpb.loteriapweb.repository.ApostaRepository;
import br.edu.ifpb.loteriapweb.repository.SorteioRepository;
import br.edu.ifpb.loteriapweb.repository.UsuarioRepository;

@Controller
public class ApostaController {

	private static Integer quantidade = 0;

	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private SorteioRepository sorteioRepository;

	@Autowired
	private ApostaRepository apostaRepository;

	@RequestMapping(value = "sorteio/{idsorteio}/apostas", method = RequestMethod.GET)
	public ModelAndView findById(@PathVariable Integer idsorteio, ModelAndView mv, Principal principal) {

		mv.setViewName("aposta/apostas");
		Sorteio sorteio = sorteioRepository.findById(idsorteio).get();
		Usuario usuario = usuarioRepository.findByUsername(principal.getName());
		List<Aposta> apostas = sorteio.getApostas();
		mv.addObject("apostas", apostas);
		mv.addObject("sorteio", sorteio);
		mv.addObject("usuario", usuario);
		return mv;
	}

	@RequestMapping("sorteio/{idsorteio}/apostas/{status}")
	public String aberto(@PathVariable Integer idsorteio, @PathVariable String status, Model m, Principal principal) {

		Sorteio sorteio = sorteioRepository.findById(idsorteio).get();
		List<Aposta> apostas = sorteio.getApostas();
		List<Aposta> apostasFiltradas = apostas.stream().filter(a -> a.getStatus().name().toLowerCase().equals(status))
				.toList();
		Usuario usuario = usuarioRepository.findByUsername(principal.getName());
		m.addAttribute("usuario", usuario);
		m.addAttribute("sorteio", sorteio);
		m.addAttribute("apostas", apostasFiltradas);
		m.addAttribute("status", status);

		return "aposta/apostas";
	}

	@RequestMapping(value = "sorteio/{idsorteio}/formularioaposta", method = RequestMethod.GET)
	public ModelAndView formaposta(@PathVariable Integer idsorteio, ModelAndView mv, Principal principal) {
		mv.setViewName("aposta/criacaoaposta");
		Sorteio sorteio = sorteioRepository.findById(idsorteio).get();
		Usuario usuario = usuarioRepository.findByUsername(principal.getName());
		mv.addObject("usuario", usuario);
		mv.addObject("sorteio", sorteio);

		return mv;
	}

	@RequestMapping(value = "sorteio/{idsorteio}/numerossorteio", method = RequestMethod.POST)
	public ModelAndView quantidade(@PathVariable Integer idsorteio, ModelAndView mv, Integer quantidadeform,
			Principal principal) {
		Usuario usuario = usuarioRepository.findByUsername(principal.getName());
		mv.addObject("usuario", usuario);
		Sorteio sorteio = sorteioRepository.findById(idsorteio).get();
		mv.addObject("sorteio", sorteio);
		quantidade = quantidadeform;
		mv.addObject("quantidade", quantidade);
		mv.setViewName("aposta/formcriar");
		return mv;

	}

	@RequestMapping(value = "sorteio/{idsorteio}/criaraposta", method = RequestMethod.POST)
	public String saveaposta(@PathVariable Integer idsorteio, Model model, Integer num1, Integer num2, Integer num3,
			Integer num4, Integer num5, Integer num6, Integer num7, Integer num8, Integer num9, Integer num10)
			throws Exception {

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Usuario usuario = usuarioRepository.findByUsername(username);
		model.addAttribute("usuario", usuario);

		Sorteio sorteio = sorteioRepository.findById(idsorteio).get();
		model.addAttribute("sorteio", sorteio);
		model.addAttribute("quantidade", quantidade);

		ArrayList<Integer> numeros = new ArrayList();
		ArrayList<Integer> numerosValidos = new ArrayList();

		Aposta aposta = new Aposta();
		aposta.setSorteio(sorteio);

		numeros.add(num1);
		numeros.add(num2);
		numeros.add(num3);
		numeros.add(num4);
		numeros.add(num5);
		numeros.add(num6);

		if (quantidade == 6) {
			if (usuario.getDinheiro() < 3.00) {
				model.addAttribute("errorMessage", "Você não tem esse dinheiro!");
				return "aposta/formcriar";
			}
			usuario.setDinheiro(usuario.getDinheiro() - 3.00);
		}
		if (quantidade >= 7) {
			if (usuario.getDinheiro() < 15.00) {
				model.addAttribute("errorMessage", "Você não tem esse dinheiro!");
				return "aposta/formcriar";
			}
			numeros.add(num7);
			if (quantidade == 7) {
				usuario.setDinheiro(usuario.getDinheiro() - 15.00);
			}
		}
		if (quantidade >= 8) {
			if (usuario.getDinheiro() < 90.00) {
				model.addAttribute("errorMessage", "Você não tem esse dinheiro!");
				return "aposta/formcriar";
			}
			numeros.add(num8);
			if (quantidade == 8) {
				usuario.setDinheiro(usuario.getDinheiro() - 90.00);
			}
		}
		if (quantidade >= 9) {
			if (usuario.getDinheiro() < 300.00) {
				model.addAttribute("errorMessage", "Você não tem esse dinheiro!");
				return "aposta/formcriar";
			}
			numeros.add(num9);
			if (quantidade == 9) {
				usuario.setDinheiro(usuario.getDinheiro() - 300.00);
			}
		}
		if (quantidade == 10) {
			if (usuario.getDinheiro() < 1500.00) {
				model.addAttribute("errorMessage", "Você não tem esse dinheiro!");
				return "aposta/formcriar";
			}
			numeros.add(num10);
			if (quantidade == 10) {
				usuario.setDinheiro(usuario.getDinheiro() - 1500.00);
			}
		}
		for (int i = 0; i < numeros.size(); i++) {

			for (int j = 0; j < numeros.size(); j++) {

				if ((numeros.get(i) <= 60) && (numeros.get(i) >= 1)) {
					if ((numeros.get(i) == numeros.get(j)) && (i != j)) {
						model.addAttribute("errorMessage", "Não é permitido números repetidos");
						return "aposta/formcriar";

					}
				}

			}
			if (numeros.get(i) <= 60 && numeros.get(i) >= 1) {
				numerosValidos.add(numeros.get(i));
			} else {
				model.addAttribute("errorMessage", "Não é permitido números menores que 1 ou maiores que 60");
				return "aposta/formcriar";
			}
		}

		for (int i = 0; i < numerosValidos.size(); i++) {

			if (numerosValidos.size() >= 6 && numerosValidos.size() <= 10) {
				aposta.adicionarNumero(numerosValidos.get(i));
			} else {
				model.addAttribute("errorMessage", "Verifique se suas apostas estão seguindo as regras");
				return "aposta/formcriar";
			}
		}

		if (numerosValidos.size() >= 6 && numerosValidos.size() <= 10) {
			model.addAttribute(usuario);

			aposta.setUsuario(usuario);
			aposta.setStatus(StatusAposta.PARTICIPANDO);
			sorteio.adicionarAposta(aposta);

			usuarioRepository.save(usuario);
			apostaRepository.save(aposta);
			sorteioRepository.save(sorteio);

		}

		return "redirect:/sorteio/" + idsorteio + "/apostas";

	}

	@RequestMapping(value = "sorteio/{idsorteio}/sortearaposta", method = RequestMethod.POST)
	public String sortear(ModelAndView mv, @PathVariable Integer idsorteio) {

		Sorteio sorteio = sorteioRepository.findById(idsorteio).get();
		List<Integer> resultado = new ArrayList<>();
		List<Aposta> apostas = sorteio.getApostas();
		Integer acertos = 0;

		for (int i = 0; i < 6; i++) {
			int numero = aleatoriar(1, 60);
			sorteio.adicionarDezenasSorteadas(numero);
			resultado.add(numero);
		}

		for (int i = 0; i < apostas.size(); i++) {

			List<Integer> apostaNumero = apostas.get(i).getNumeros();

			for (int j = 0; j < apostaNumero.size(); j++) {

				if (resultado.contains(apostaNumero.get(j))) {
					acertos++;
				}
			}

			if (acertos >= 6) {
				apostas.get(i).setStatus(StatusAposta.GANHOU);
				apostas.get(i).getUsuario().setDinheiro(
						apostas.get(i).getUsuario().getDinheiro() + apostas.get(i).getSorteio().getValorDoPremio());
			} else {
				apostas.get(i).setStatus(StatusAposta.PERDEU);
			}
			apostaRepository.save(apostas.get(i));
			acertos = 0;
		}

		sorteio.setFoiSorteado(true);
		sorteio.setStatus(StatusSorteio.FECHADO);
		sorteioRepository.save(sorteio);

		mv.addObject("sorteio", sorteio);
		mv.addObject("resultado", resultado);

		return "redirect:/home";
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public String onError() {
		return "redirect:/apostas/aposta";
	}

	@ExceptionHandler(NullPointerException.class)
	public String onError2() {
		return "redirect:/apostas/aposta";
	}

	public static int aleatoriar(int minimo, int maximo) {
		Random random = new Random();
		return random.nextInt((maximo - minimo) + 1) + minimo;
	}
}
