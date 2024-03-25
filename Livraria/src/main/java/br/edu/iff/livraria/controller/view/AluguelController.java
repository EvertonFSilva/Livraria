package br.edu.iff.livraria.controller.view;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.edu.iff.livraria.entities.Aluguel;
import br.edu.iff.livraria.entities.Usuario;
import br.edu.iff.livraria.entities.Cliente;
import br.edu.iff.livraria.service.ClienteService;
import br.edu.iff.livraria.service.LivroService;
import br.edu.iff.livraria.service.AluguelService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/aluguel")
public class AluguelController {

	@Autowired
	private AluguelService aluguelService;

	@Autowired
	public ClienteService clienteService;

	@Autowired
	public LivroService livroService;

	@GetMapping("/listar")
	public String listarAlugueis(Model model, HttpSession session, @RequestParam(required = false) String resultado)
			throws UnsupportedEncodingException {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
		if (usuario != null && usuario.getPermissao() == 2) {
			model.addAttribute("alugueis", aluguelService.listarAlugueis());
			model.addAttribute("clientes", clienteService.listarClientes());
			model.addAttribute("livros", livroService.listarLivros());
			if (resultado != null) {
				model.addAttribute("mensagem", URLDecoder.decode(resultado, "UTF-8"));
			}
			return "admin/alugueis";
		}
		return "redirect:/";
	}

	@GetMapping("/carrinho")
	public String exibirCarrinho(Model model, HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
		Cliente cliente = clienteService.buscarPorLogin(usuario.getLogin());
		if (cliente != null) {
			List<Aluguel> alugueis = aluguelService.buscarAlugueisEmProgresso(cliente.getId());
			if (!alugueis.isEmpty()) {
				model.addAttribute("aluguel", alugueis.get(0));
			} else {
				model.addAttribute("aluguel", null);
			}
			return "carrinho";
		}
		return "redirect:/";
	}

	@PostMapping("/adicionarItem")
	public String adicionarItemAoAluguel(@RequestParam("livroId") Long livroId,
			@RequestParam("quantidade") int quantidade, HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
		Cliente cliente = clienteService.buscarPorLogin(usuario.getLogin());
		if (cliente != null) {
			List<Aluguel> alugueis = aluguelService.buscarAlugueisEmProgresso(cliente.getId());
			Aluguel aluguel;
			if (alugueis.isEmpty()) {
				aluguel = aluguelService.adicionarAluguel(cliente.getId(), "PIX");
			} else {
				aluguel = alugueis.get(0);
			}
			aluguelService.adicionarItemAoAluguel(aluguel.getId(), livroId, quantidade);
			return "redirect:/aluguel/carrinho";
		}
		return "redirect:/";
	}

	@PostMapping("/alterar/pagamento")
	public String alterarFormaPagamento(@RequestParam("tipo") String formaPagamento, HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
		Cliente cliente = clienteService.buscarPorLogin(usuario.getLogin());
		if (cliente != null) {
			aluguelService.atualizarAluguel(aluguelService.buscarAlugueisEmProgresso(cliente.getId()).get(0).getId(),
					cliente.getId(), 0.0f, null, null, null, formaPagamento, false);
			return "redirect:/aluguel/carrinho";
		}
		return "redirect:/";
	}

	@PostMapping("/alterar/quantidade")
	public String alterarQuantidadeItem(@RequestParam("itemId") Long itemId,
			@RequestParam("novaQuantidade") int novaQuantidade, HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
		Cliente cliente = clienteService.buscarPorLogin(usuario.getLogin());
		if (cliente != null) {
			aluguelService.atualizarItemDoAluguel(
					aluguelService.buscarAlugueisEmProgresso(cliente.getId()).get(0).getId(), itemId, null,
					novaQuantidade);
			return "redirect:/aluguel/carrinho";
		}
		return "redirect:/";
	}

	@PostMapping("/deletarItem")
	public String deletarItemDoAluguel(@RequestParam("itemId") Long itemId, HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
		Cliente cliente = clienteService.buscarPorLogin(usuario.getLogin());
		if (cliente != null) {
			aluguelService.deletarItemDoAluguel(
					aluguelService.buscarAlugueisEmProgresso(cliente.getId()).get(0).getId(), itemId);
			return "redirect:/aluguel/carrinho";
		}
		return "redirect:/";
	}

	@PostMapping("/finalizar")
	public String finalizarAluguel(HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
		Cliente cliente = clienteService.buscarPorLogin(usuario.getLogin());
		if (cliente != null) {
			aluguelService.finalizarAluguel(aluguelService.buscarAlugueisEmProgresso(cliente.getId()).get(0).getId());
			return "redirect:/";
		}
		return "redirect:/";
	}

	@GetMapping("/finalizar")
	public String exibirPaginaFinalizarAluguel(Model model, HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
		Cliente cliente = clienteService.buscarPorLogin(usuario.getLogin());
		if (cliente != null) {
			List<Aluguel> alugueis = aluguelService.buscarAlugueisEmProgresso(cliente.getId());
			if (!alugueis.isEmpty()) {
				Aluguel aluguel = alugueis.get(0);
				aluguel.setDataInicio(LocalDateTime.now());
				model.addAttribute("aluguel", aluguel);
				return "finalizar";
			}
		}
		return "redirect:/";
	}

	@PostMapping("/admin/adicionar")
	public String adicionarAluguel(@RequestParam("clienteId") Long clienteId,
			@RequestParam("formaPagamento") String formaPagamento, Model model, HttpSession session)
			throws UnsupportedEncodingException {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
		if (usuario != null && usuario.getPermissao() == 2) {
			Aluguel aluguel = aluguelService.adicionarAluguel(clienteId, formaPagamento);
			if (aluguel == null) {
				String mensagem = "Esse cliente já tem um aluguel em progresso.";
				return "redirect:/aluguel/listar?resultado=" + URLEncoder.encode(mensagem, "UTF-8");
			} else {
				return "redirect:/aluguel/listar";
			}
		}
		return "redirect:/";
	}

	@PostMapping("/admin/deletar")
	public String deleteAluguel(@RequestParam("aluguelId") Long aluguelId, Model model, HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
		if (usuario != null && usuario.getPermissao() == 2) {
			aluguelService.deletarAluguel(aluguelId);
			return "redirect:/aluguel/listar";
		}
		return "redirect:/";
	}

	@PostMapping("/admin/editar")
	public String editarAluguel(@RequestParam("aluguelId") Long aluguelId, @RequestParam("clienteId") Long clienteId,
			@RequestParam("valorTotal") float valorTotal,
			@RequestParam("dataInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
			@RequestParam("dataFim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim,
			@RequestParam("dataEntrega") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataEntrega,
			@RequestParam("formaPagamento") String formaPagamento, @RequestParam("status") boolean status, Model model,
			HttpSession session) throws UnsupportedEncodingException {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
		if (usuario != null && usuario.getPermissao() == 2) {
			List<Aluguel> alugueisEmAberto = aluguelService.buscarAlugueisEmProgresso(clienteId);
			if (!alugueisEmAberto.isEmpty()) {
				if (alugueisEmAberto.size() == 1 && alugueisEmAberto.get(0).getId() != aluguelId) {
					if (aluguelService.buscarPorId(aluguelId).isFinalizado()) {
						String mensagem = "Esse cliente já tem um aluguel em progresso.";
						return "redirect:/aluguel/listar?resultado=" + URLEncoder.encode(mensagem, "UTF-8");
					}
				}
			}
			aluguelService.atualizarAluguel(aluguelId, clienteId, valorTotal, dataInicio, dataFim, dataEntrega,
					formaPagamento, status);
			return "redirect:/aluguel/listar";
		}
		return "redirect:/";
	}

	@PostMapping("/admin/adicionarItem")
	public String adicionarItemAoAluguel(@RequestParam("aluguelId") Long aluguelId,
			@RequestParam("livroId") Long livroId, @RequestParam("quantidade") int quantidade, Model model,
			HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
		if (usuario != null && usuario.getPermissao() == 2) {
			aluguelService.adicionarItemAoAluguel(aluguelId, livroId, quantidade);
			return "redirect:/aluguel/listar";
		}
		return "redirect:/";
	}

	@PostMapping("/admin/deletarItem")
	public String deletarItemDoAluguel(@RequestParam("aluguelId") Long aluguelId, @RequestParam("itemId") Long itemId,
			Model model, HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
		if (usuario != null && usuario.getPermissao() == 2) {
			aluguelService.deletarItemDoAluguel(aluguelId, itemId);
			return "redirect:/aluguel/listar";
		}
		return "redirect:/";
	}

	@PostMapping("/admin/editarItem")
	public String editarItemDoAluguel(@RequestParam("aluguelId") Long aluguelId, @RequestParam("itemId") Long itemId,
			@RequestParam("livroId") Long livroId, @RequestParam("novaQuantidade") int novaQuantidade, Model model,
			HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
		if (usuario != null && usuario.getPermissao() == 2) {
			aluguelService.atualizarItemDoAluguel(aluguelId, itemId, livroId, novaQuantidade);
			return "redirect:/aluguel/listar";
		}
		return "redirect:/";
	}

}
