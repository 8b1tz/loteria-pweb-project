package br.edu.ifpb.loteriapweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import br.edu.ifpb.loteriapweb.enums.StatusSorteio;
import br.edu.ifpb.loteriapweb.repository.SorteioRepository;

@RequestMapping("/home")
@Controller
public class HomeController {

	@Autowired
	private SorteioRepository sorteioRepository;

	@GetMapping
	public String home(Model m) {
		m.addAttribute("sorteios", sorteioRepository.findAll());
		return "home";
	}

	@GetMapping("/{status}")
	public String aberto(@PathVariable String status, Model m) {
		m.addAttribute("sorteios", sorteioRepository.findByStatus(StatusSorteio.valueOf(status.toUpperCase())));
		m.addAttribute("status", status);
		return "home";
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public String onError() {
		return "redirect:/home";
	}
}
