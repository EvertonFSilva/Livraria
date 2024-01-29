package br.edu.iff.livraria.controller.apirest;

import br.edu.iff.livraria.entities.Funcionario;
import br.edu.iff.livraria.service.FuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/funcionario")
public class FuncionarioRestController {

    @Autowired
    private FuncionarioService funcionarioService;

    @PostMapping("")
    @ResponseBody
    public String adicionarFuncionario(String login, String senha, String cpf, String nome, String email, String telefone, String endereco, String cargo, float salario) {
        return funcionarioService.adicionarFuncionario(login, senha, cpf, nome, email, telefone, endereco, cargo, salario);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public String atualizarFuncionario(@PathVariable("id") Long id, @RequestParam String cpf, String nome, String email, String endereco, String cargo, float salario) {
        return funcionarioService.atualizarFuncionario(id, cpf, nome, email, endereco, cargo, salario);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public String deletarFuncionario(@PathVariable("id") Long id) {
        String resultado = funcionarioService.deletarFuncionario(id);
        return resultado;
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Funcionario buscarFuncionario(@PathVariable("id") Long id) {
        return funcionarioService.buscarFuncionario(id);
    }

    @GetMapping("")
    @ResponseBody
    public List<Funcionario> listarFuncionarios() {
        return funcionarioService.listarFuncionarios();
    }

    @PostMapping("/{id}/telefone")
    @ResponseBody
    public String adicionarTelefone(@PathVariable("id") Long id, @RequestParam String telefone) {
        String resultado = funcionarioService.adicionarTelefone(id, telefone);
        return resultado;
    }

    @DeleteMapping("/{id}/telefone")
    @ResponseBody
    public String deletarTelefone(@PathVariable("id") Long id, @RequestParam String telefone) {
        String resultado = funcionarioService.deletarTelefone(id, telefone);
        return resultado;
    }

    @GetMapping("/{id}/telefones")
    @ResponseBody
    public String listarTelefones(@PathVariable("id") Long id) {
        String resultado = funcionarioService.listarTelefones(id);
        return resultado;
    }
}
