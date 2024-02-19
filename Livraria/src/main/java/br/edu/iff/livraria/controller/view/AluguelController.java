package br.edu.iff.livraria.controller.view;

import br.edu.iff.livraria.entities.Aluguel;
import br.edu.iff.livraria.service.AluguelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("aluguel")
public class AluguelController {

	@Autowired
	private AluguelService aluguelService;

	@GetMapping
	public String listarAlugueis(Model model) {
		List<Aluguel> alugueis = aluguelService.listarAlugueis();
		model.addAttribute("alugueis", alugueis);
		return "listarAlugueis";
	}

	@GetMapping("/adicionar")
	public String exibirFormularioAdicionar() {
		return "adicionarAluguel";
	}

	@PostMapping("/adicionar")
	public String adicionarAluguel(@Valid @RequestParam Long clienteId, @Valid @RequestParam String formaPagamento,
			BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "adicionarAluguel";
		}

		String mensagem = aluguelService.adicionarAluguel(clienteId, formaPagamento);
		model.addAttribute("mensagem", mensagem);
		return "redirect:/aluguel/adicionar";
	}

	@GetMapping("/editar/{id}")
	public String exibirFormularioEditar(@PathVariable Long id, Model model) {
		Aluguel aluguel = aluguelService.buscarPorId(id);
		model.addAttribute("aluguel", aluguel);
		return "editarAluguel";
	}

	@PostMapping("/editar/{id}")
	public String editarAluguel(@PathVariable Long id, @Valid @RequestParam Long clienteId,
			@Valid @RequestParam float valorTotal, @Valid @RequestParam LocalDateTime dataInicio,
			@Valid @RequestParam LocalDateTime dataFim, @Valid @RequestParam LocalDateTime dataEntrega,
			@Valid @RequestParam String formaPagamento, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "editarAluguel";
		}

		String mensagem = aluguelService.atualizarAluguel(id, clienteId, valorTotal, dataInicio, dataFim, dataEntrega,
				formaPagamento);
		model.addAttribute("mensagem", mensagem);
		return "redirect:/aluguel/editar/" + id;
	}

	@GetMapping("/excluir/{id}")
	public String excluirAluguel(@PathVariable Long id, Model model) {
		String mensagem = aluguelService.deletarAluguel(id);
		model.addAttribute("mensagem", mensagem);
		return "redirect:/aluguel/excluir/" + id;
	}
}