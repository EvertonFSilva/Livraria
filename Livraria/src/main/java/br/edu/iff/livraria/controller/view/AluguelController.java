package br.edu.iff.livraria.controller.view;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.edu.iff.livraria.entities.Aluguel;
import br.edu.iff.livraria.entities.Cliente;
import br.edu.iff.livraria.entities.Livro;
import br.edu.iff.livraria.service.AluguelService;
import br.edu.iff.livraria.service.ClienteService;
import br.edu.iff.livraria.service.LivroService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("aluguel")
public class AluguelController {

	@Autowired
	private AluguelService aluguelService;

	@Autowired
	private LivroService livroService;

	@Autowired
	public ClienteService clienteService;

	@GetMapping("/CRUD")
	public String page() throws Exception {
		return "redirect:/aluguel/CRUD/listarAlugueis";
	}

	@GetMapping("/CRUD/addForm")
	public String addAluguelForm(Model model, HttpServletRequest request) throws Exception {
		model.addAttribute("aluguel_add", new Aluguel());
		model.addAttribute("cliente_lista", clienteService.listarClientes());
		String resposta = request.getParameter("resposta");
		if (resposta != null) {
			model.addAttribute("respostaAdd", URLDecoder.decode(resposta, "UTF-8"));
		}
		return "CRUD_Aluguel";
	}

	@PostMapping("/CRUD/add")
	public String addAluguel(@Valid @ModelAttribute Aluguel aluguel, BindingResult resultado, Model model) {
		Cliente cliente = aluguel.getCliente();
		if (resultado.hasErrors()) {
			model.addAttribute("mensagemErro", resultado.getAllErrors());
			return "";
		} else {
			String resposta = aluguelService.adicionarAluguel(cliente.getId(), aluguel.getFormaPagamento());
			try {
				return "redirect:/aluguel/CRUD/addForm?resposta=" + URLEncoder.encode(resposta, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return "";
			}
		}
	}

	@GetMapping("/CRUD/listarAlugueis")
	public String listarAlugueis(Model model, HttpServletRequest request) throws Exception {
		String cpf = request.getParameter("cpf");
		String resposta = request.getParameter("resposta");
		if (cpf == null) {
			model.addAttribute("aluguel_lista", aluguelService.listarAlugueis());
		} else {
			List<Aluguel> alugueis = clienteService.buscarPorCPF(URLDecoder.decode(cpf, "UTF-8")).getAluguel();
			if (alugueis == null) {
				model.addAttribute("aluguel_lista", new Aluguel());
			} else {
				model.addAttribute("aluguel_lista", alugueis);
			}
		}
		if (resposta != null) {
			model.addAttribute("respostaFinalizar", URLDecoder.decode(resposta, "UTF-8"));
		}
		return "CRUD_Aluguel";
	}

	@PostMapping("/CRUD/buscaAluguel")
	public String buscarAluguel(String cpf) throws Exception {
		return "redirect:/aluguel/CRUD/listarAlugueis?cpf=" + URLEncoder.encode(cpf, "UTF-8");
	}

	@GetMapping("/CRUD/editar")
	public String formEditar(Long id, Model model) throws Exception {
		model.addAttribute("aluguel_edit", aluguelService.buscarPorId(id));
		model.addAttribute("cliente_lista", clienteService.listarClientes());
		model.addAttribute("livro_lista", aluguelService.listarLivrosDoAluguel(id));

		return "CRUD_Aluguel";
	}

	@PostMapping("/CRUD/atualizar")
	public String atualizarAluguel(Long id, @Valid @ModelAttribute Aluguel aluguel, BindingResult resultado,
			Model model) {
		if (resultado.hasErrors()) {
			model.addAttribute("mensagemErro", resultado.getAllErrors());
			return "";
		} else {
			aluguelService.atualizarAluguel(id, aluguel.getCliente().getId(), aluguel.getValorTotal(),
					aluguel.getDataInicio(), aluguel.getDataFim(), aluguel.getDataEntrega(),
					aluguel.getFormaPagamento());
			return "redirect:/aluguel/CRUD/editar?id=" + id;
		}
	}

	@GetMapping("/CRUD/deletar")
	public String deletarAluguel(@RequestParam Long id) throws Exception {
		aluguelService.deletarAluguel(id);
		return "redirect:/aluguel/CRUD/listarAlugueis";
	}

	@PostMapping("/CRUD/addItem")
	public String addLivro(@RequestParam Long id, String titulo) throws Exception {
		aluguelService.adicionarItemAoAluguel(id, livroService.buscarPorTitulo(titulo).getId(), 1);
		return "redirect:/aluguel/CRUD/editar?id=" + id;
	}

	@GetMapping("/CRUD/removeItem")
	public String removeLivro(@RequestParam Long id, Long itemId) throws Exception {
		aluguelService.deletarItemDoAluguel(id, itemId);
		return "redirect:/aluguel/CRUD/editar?id=" + id;
	}

	@GetMapping("/CRUD/finalizar")
	public String finalizarAluguel(Long id) throws Exception {
		String resposta = aluguelService.finalizarAluguel(id);
		return "redirect:/aluguel/CRUD/listarAlugueis?resposta=" + URLEncoder.encode(resposta, "UTF-8");
	}

	@GetMapping("/carrinho/{id}")
	public String carrinho(@PathVariable("id") Long id, Model model, HttpServletRequest request) throws Exception {
		List<Livro> listaLivros = aluguelService.listarLivrosDoAluguel(id);
		model.addAttribute("aluguel", aluguelService.buscarPorId(id));
		model.addAttribute("lista_lista", listaLivros);
		String resposta = request.getParameter("resposta");
		if (resposta != null) {
			model.addAttribute("respostaFinalizar", URLDecoder.decode(resposta, "UTF-8"));
		}
		return "carrinho";
	}

	@GetMapping("/carrinho/addItem")
	public String addLivroCarrinho(Long id, String titulo) throws Exception {
		aluguelService.adicionarItemAoAluguel(id, livroService.buscarPorTitulo(titulo).getId(), 1);
		return "redirect:/";
	}

	@GetMapping("/carrinho/removeItem")
	public String removeItem(@RequestParam Long id, Long itemId) throws Exception {
		aluguelService.deletarItemDoAluguel(id, itemId);
		return "redirect:/aluguel/carrinho/" + aluguelService.buscarPorId(id).getCliente().getId();
	}

	@GetMapping("/carrinho/finalizar")
	public String finalizarAluguelCarrinho(Long id) throws Exception {
		String resposta = aluguelService.finalizarAluguel(id);
		if (resposta.compareTo("Aluguel finalizada com sucesso") == 0) {
			return "redirect:/";
		}

		return "redirect:/aluguel/carrinho/" + aluguelService.buscarPorId(id).getCliente().getId() + "?resposta="
				+ URLEncoder.encode(resposta, "UTF-8");
	}

}