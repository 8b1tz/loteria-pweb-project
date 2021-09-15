package br.edu.ifpb.loteriapweb.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import br.edu.ifpb.loteriapweb.enums.StatusSorteio;
import br.edu.ifpb.loteriapweb.model.Sorteio;
import br.edu.ifpb.loteriapweb.repository.SorteioRepository;

@Controller

public class SorteioController {

	@Autowired
	private SorteioRepository sorteioRepository;


	@GetMapping("sorteio/criacao")
	public ModelAndView formulario(Sorteio sorteio, ModelAndView mv) {
		
		return mv;
	}

	@PostMapping("sorteio/criar")
	public ModelAndView criar(@Valid Sorteio sorteio, BindingResult resultadoValidacao, ModelAndView mv) {
		if (resultadoValidacao.hasErrors()) {
			mv.addObject("sorteio", sorteio);
			mv.setViewName("sorteio/criacao");			
		} else {
			sorteio.setStatus(StatusSorteio.ABERTO);
			sorteioRepository.save(sorteio);
			mv.setViewName("redirect:/home");
		}
		return mv;
	}	
	
	
}
