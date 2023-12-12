package br.edu.iff.livraria.controller.apirest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping(path = "/api/v1")
public class MainRestController {

	@GetMapping
	@ResponseBody
	@Operation(summary = "Mensagem principal da API")
	public String inicial() {
		return "Olá, bem vindo a API do meu APP.";
	}
}