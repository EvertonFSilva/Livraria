package br.edu.iff.livraria.controller.view;

import br.edu.iff.livraria.entities.Funcionario;
import br.edu.iff.livraria.service.FuncionarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("funcionario")
public class FuncionarioController {

    @Autowired
    private FuncionarioService funcionarioService;

    @GetMapping
    public String listarFuncionarios(Model model) {
        List<Funcionario> funcionarios = funcionarioService.listarFuncionarios();
        model.addAttribute("funcionarios", funcionarios);
        return "listarFuncionarios";
    }

    @GetMapping("/adicionar")
    public String exibirFormularioAdicionar() {
        return "adicionarFuncionario";
    }

    @PostMapping("/adicionar")
    public String adicionarFuncionario(@Valid @RequestParam String login, @Valid @RequestParam String senha,
                                       @Valid @RequestParam String cpf, @Valid @RequestParam String nome, @Valid @RequestParam String email,
                                       @Valid @RequestParam String telefone, @Valid @RequestParam String endereco,
                                       @Valid @RequestParam String cargo, @Valid @RequestParam float salario, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "adicionarFuncionario";
        }

        String mensagem = funcionarioService.adicionarFuncionario(login, senha, cpf, nome, email, telefone, endereco,
                cargo, salario);
        model.addAttribute("mensagem", mensagem);
        return "redirect:/funcionario/adicionar";
    }

    @GetMapping("/editar/{id}")
    public String exibirFormularioEditar(@PathVariable Long id, Model model) {
        Funcionario funcionario = funcionarioService.buscarPorId(id);
        model.addAttribute("funcionario", funcionario);
        return "editarFuncionario";
    }

    @PostMapping("/editar/{id}")
    public String editarFuncionario(@PathVariable Long id, @Valid @RequestParam String cpf,
                                    @Valid @RequestParam String nome, @Valid @RequestParam String email, @Valid @RequestParam String endereco,
                                    @Valid @RequestParam String cargo, @Valid @RequestParam float salario, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "editarFuncionario";
        }

        String mensagem = funcionarioService.atualizarFuncionario(id, cpf, nome, email, endereco, cargo, salario);
        model.addAttribute("mensagem", mensagem);
        return "redirect:/funcionario/editar/" + id;
    }

    @GetMapping("/excluir/{id}")
    public String excluirFuncionario(@PathVariable Long id, Model model) {
        String mensagem = funcionarioService.deletarFuncionario(id);
        model.addAttribute("mensagem", mensagem);
		return "redirect:/funcionario/excluir/" + id;
    }

    @GetMapping("/{id}/telefones")
    public String listarTelefones(@PathVariable Long id, Model model) {
        List<String> telefones = funcionarioService.listarTelefones(id);
        model.addAttribute("telefones", telefones);
        return "listarTelefones";
    }

    @GetMapping("/{id}/adicionarTelefone")
    public String exibirFormularioAdicionarTelefone(@PathVariable Long id) {
        return "adicionarTelefone";
    }

    @PostMapping("/{id}/adicionarTelefone")
    public String adicionarTelefone(@PathVariable Long id, @RequestParam String telefone, Model model) {
        String mensagem = funcionarioService.adicionarTelefone(id, telefone);
        model.addAttribute("mensagem", mensagem);
        return "redirect:/funcionario/editar/" + id;
    }

    @GetMapping("/{id}/deletarTelefone")
    public String exibirFormularioDeletarTelefone(@PathVariable Long id) {
        return "deletarTelefone";
    }

    @PostMapping("/{id}/deletarTelefone")
    public String deletarTelefone(@PathVariable Long id, @RequestParam String telefone, Model model) {
        String mensagem = funcionarioService.deletarTelefone(id, telefone);
        model.addAttribute("mensagem", mensagem);
        return "redirect:/funcionario/editar/" + id;
    }
}
