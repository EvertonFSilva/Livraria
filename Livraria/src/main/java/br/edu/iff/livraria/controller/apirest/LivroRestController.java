package br.edu.iff.livraria.controller.apirest;

import br.edu.iff.livraria.entities.Livro;
import br.edu.iff.livraria.service.LivroService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/livro")
public class LivroRestController {

    @Autowired
    private LivroService livroService;

    @PostMapping("")
    @ResponseBody
    @Operation(summary = "Adicionar um livro em específico")
    public String adicionarLivro(@RequestParam String titulo,
                                 @RequestParam String autor,
                                 @RequestParam String genero,
                                 @RequestParam int qtdPaginas,
                                 @RequestParam float precoVenda,
                                 @RequestParam float precoAluguel) {
        return livroService.adicionarLivro(titulo, autor, genero, qtdPaginas, precoVenda, precoAluguel);
    }

    @PutMapping("/{id}")
    @ResponseBody
    @Operation(summary = "Atualizar um livro em específico")
    public String atualizarLivro(@PathVariable("id") Long id,
                                 @RequestParam String titulo,
                                 @RequestParam String autor,
                                 @RequestParam String genero,
                                 @RequestParam int qtdPaginas,
                                 @RequestParam float precoVenda,
                                 @RequestParam float precoAluguel) {
        return livroService.atualizarLivro(id, titulo, autor, genero, qtdPaginas, precoVenda, precoAluguel);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    @Operation(summary = "Deletar um livro em específico")
    public String deletarLivro(@PathVariable("id") Long id) {
        return livroService.deletarLivro(id);
    }

    @GetMapping("/{id}")
    @ResponseBody
    @Operation(summary = "Retornar um livro em específico")
    public Livro buscarLivro(@PathVariable("id") Long id) {
        return livroService.buscarPorId(id);
    }

    @GetMapping("")
    @ResponseBody
    @Operation(summary = "Listar todos os livros")
    public List<Livro> listarLivros() {
        return livroService.listarLivros();
    }
}
