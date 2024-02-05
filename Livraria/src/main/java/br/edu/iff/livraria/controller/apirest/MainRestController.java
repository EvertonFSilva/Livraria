package br.edu.iff.livraria.controller.apirest;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class MainRestController {

    @GetMapping
    @ResponseBody
    @Operation(summary = "Mensagem principal da API")
    public ResponseEntity<String> inicial() {
        try {
            String mensagem = "Olá, bem-vindo à API do meu APP.";
            return ResponseEntity.ok(mensagem);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao processar a requisição.");
        }
    }
}
