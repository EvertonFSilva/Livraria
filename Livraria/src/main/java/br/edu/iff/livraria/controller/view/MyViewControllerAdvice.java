package br.edu.iff.livraria.controller.view;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class MyViewControllerAdvice {

	@ExceptionHandler(Exception.class)
	public String handleException(Exception ex, Model model) {
		model.addAttribute("msgErros", ex.getMessage());
		return "error";
	}
}