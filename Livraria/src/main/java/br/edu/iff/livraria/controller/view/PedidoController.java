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

import br.edu.iff.livraria.entities.Pedido;
import br.edu.iff.livraria.entities.Usuario;
import br.edu.iff.livraria.entities.Cliente;
import br.edu.iff.livraria.service.ClienteService;
import br.edu.iff.livraria.service.LivroService;
import br.edu.iff.livraria.service.PedidoService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/pedido")
public class PedidoController {

	@Autowired
	private PedidoService pedidoService;

	@Autowired
	public ClienteService clienteService;

	@Autowired
	private LivroService livroService;

	@GetMapping("/listar")
	public String listarPedidos(Model model, HttpSession session, @RequestParam(required = false) String resultado)
			throws UnsupportedEncodingException {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
		if (usuario != null && usuario.getPermissao() == 2) {
			model.addAttribute("pedidos", pedidoService.listarPedidos());
			model.addAttribute("clientes", clienteService.listarClientes());
			model.addAttribute("livros", livroService.listarLivros());
			if (resultado != null) {
				model.addAttribute("mensagem", URLDecoder.decode(resultado, "UTF-8"));
			}
			return "admin/pedidos";
		}
		return "redirect:/";
	}

	@GetMapping("/carrinho")
	public String exibirCarrinho(Model model, HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
		Cliente cliente = clienteService.buscarPorLogin(usuario.getLogin());
		if (cliente != null) {
			List<Pedido> pedidos = pedidoService.buscarPedidosEmProgresso(cliente.getId());
			if (!pedidos.isEmpty()) {
				model.addAttribute("pedido", pedidos.get(0));
			} else {
				model.addAttribute("pedido", null);
			}
			return "carrinho";
		}
		return "redirect:/";
	}

	@PostMapping("/adicionarItem")
	public String adicionarItemAoPedido(@RequestParam("livroId") Long livroId,
			@RequestParam("quantidade") int quantidade, HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
		Cliente cliente = clienteService.buscarPorLogin(usuario.getLogin());
		if (cliente != null) {
			List<Pedido> pedidos = pedidoService.buscarPedidosEmProgresso(cliente.getId());
			Pedido pedido;
			if (pedidos.isEmpty()) {
				pedido = pedidoService.adicionarPedido(cliente.getId(), "PIX");
			} else {
				pedido = pedidos.get(0);
			}
			pedidoService.adicionarItemAoPedido(pedido.getId(), livroId, quantidade);
			return "redirect:/pedido/carrinho";
		}
		return "redirect:/";
	}

	@PostMapping("/alterar/pagamento")
	public String alterarFormaPagamento(@RequestParam("tipo") String formaPagamento, HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
		Cliente cliente = clienteService.buscarPorLogin(usuario.getLogin());
		if (cliente != null) {
			pedidoService.atualizarPedido(pedidoService.buscarPedidosEmProgresso(cliente.getId()).get(0).getId(),
					cliente.getId(), 0, null, null, formaPagamento, false);
			return "redirect:/pedido/carrinho";
		}
		return "redirect:/";
	}

	@PostMapping("/alterar/quantidade")
	public String alterarQuantidadeItem(@RequestParam("itemId") Long itemId,
			@RequestParam("novaQuantidade") int novaQuantidade, HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
		Cliente cliente = clienteService.buscarPorLogin(usuario.getLogin());
		if (cliente != null) {
			pedidoService.atualizarItemDoPedido(pedidoService.buscarPedidosEmProgresso(cliente.getId()).get(0).getId(),
					itemId, null, novaQuantidade);
			return "redirect:/pedido/carrinho";
		}
		return "redirect:/";
	}

	@PostMapping("/deletarItem")
	public String deletarItemDoPedido(@RequestParam("itemId") Long itemId, HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
		Cliente cliente = clienteService.buscarPorLogin(usuario.getLogin());
		if (cliente != null) {
			pedidoService.deletarItemDoPedido(pedidoService.buscarPedidosEmProgresso(cliente.getId()).get(0).getId(),
					itemId);
			return "redirect:/pedido/carrinho";
		}
		return "redirect:/";
	}

	@PostMapping("/finalizar")
	public String finalizarPedido(HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
		Cliente cliente = clienteService.buscarPorLogin(usuario.getLogin());
		if (cliente != null) {
			pedidoService.finalizarPedido(pedidoService.buscarPedidosEmProgresso(cliente.getId()).get(0).getId());
			return "redirect:/";
		}
		return "redirect:/";
	}

	@GetMapping("/finalizar")
	public String exibirPaginaFinalizarPedido(Model model, HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
		Cliente cliente = clienteService.buscarPorLogin(usuario.getLogin());
		if (cliente != null) {
			List<Pedido> pedidos = pedidoService.buscarPedidosEmProgresso(cliente.getId());
			if (!pedidos.isEmpty()) {
				Pedido pedido = pedidos.get(0);
				pedido.setDataPedido(LocalDateTime.now());
				model.addAttribute("pedido", pedido);
				return "finalizar";
			}
		}
		return "redirect:/";
	}

	@PostMapping("/admin/adicionar")
	public String adicionarPedido(@RequestParam("clienteId") Long clienteId,
			@RequestParam("formaPagamento") String formaPagamento, Model model, HttpSession session)
			throws UnsupportedEncodingException {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
		if (usuario != null && usuario.getPermissao() == 2) {
			Pedido pedido = pedidoService.adicionarPedido(clienteId, formaPagamento);
			if (pedido == null) {
				String mensagem = "Esse cliente já tem um pedido em progresso.";
				return "redirect:/pedido/listar?resultado=" + URLEncoder.encode(mensagem, "UTF-8");
			} else {
				return "redirect:/pedido/listar";
			}
		}
		return "redirect:/";
	}

	@PostMapping("/admin/deletar")
	public String deletePedido(@RequestParam("pedidoId") Long pedidoId, Model model, HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
		if (usuario != null && usuario.getPermissao() == 2) {
			pedidoService.deletarPedido(pedidoId);
			return "redirect:/pedido/listar";
		}
		return "redirect:/";
	}

	@PostMapping("/admin/editar")
	public String editarPedido(@RequestParam("pedidoId") Long pedidoId, @RequestParam("clienteId") Long clienteId,
			@RequestParam("valorTotal") float valorTotal,
			@RequestParam("dataPedido") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataPedido,
			@RequestParam("dataEntrega") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataEntrega,
			@RequestParam("formaPagamento") String formaPagamento, @RequestParam("status") boolean status, Model model,
			HttpSession session) throws UnsupportedEncodingException {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
		if (usuario != null && usuario.getPermissao() == 2) {
			List<Pedido> pedidosEmAberto = pedidoService.buscarPedidosEmProgresso(clienteId);
			if (!pedidosEmAberto.isEmpty()) {
				if (pedidosEmAberto.size() == 1 && pedidosEmAberto.get(0).getId() != pedidoId) {
					if (pedidoService.buscarPorId(pedidoId).isFinalizado()) {
						String mensagem = "Esse cliente já tem um aluguel em progresso.";
						return "redirect:/aluguel/listar?resultado=" + URLEncoder.encode(mensagem, "UTF-8");
					}
				}
			}
			pedidoService.atualizarPedido(pedidoId, clienteId, valorTotal, dataPedido, dataEntrega, formaPagamento,
					status);
			return "redirect:/pedido/listar";
		}
		return "redirect:/";
	}

	@PostMapping("/admin/adicionarItem")
	public String adicionarItemAoPedido(@RequestParam("pedidoId") Long pedidoId, @RequestParam("livroId") Long livroId,
			@RequestParam("quantidade") int quantidade, Model model, HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
		if (usuario != null && usuario.getPermissao() == 2) {
			pedidoService.adicionarItemAoPedido(pedidoId, livroId, quantidade);
			return "redirect:/pedido/listar";
		}
		return "redirect:/";
	}

	@PostMapping("/admin/deletarItem")
	public String deletarItemDoPedido(@RequestParam("pedidoId") Long pedidoId, @RequestParam("itemId") Long itemId,
			Model model, HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
		if (usuario != null && usuario.getPermissao() == 2) {
			pedidoService.deletarItemDoPedido(pedidoId, itemId);
			return "redirect:/pedido/listar";
		}
		return "redirect:/";
	}

	@PostMapping("/admin/editarItem")
	public String editarItemDoPedido(@RequestParam("pedidoId") Long pedidoId, @RequestParam("itemId") Long itemId,
			@RequestParam("livroId") Long livroId, @RequestParam("novaQuantidade") int novaQuantidade, Model model,
			HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
		if (usuario != null && usuario.getPermissao() == 2) {
			pedidoService.atualizarItemDoPedido(pedidoId, itemId, livroId, novaQuantidade);
			return "redirect:/pedido/listar";
		}
		return "redirect:/";
	}
}