package br.edu.iff.livraria.controller.apirest;

import br.edu.iff.livraria.entities.Aluguel;
import br.edu.iff.livraria.entities.Item;
import br.edu.iff.livraria.entities.Pedido;
import br.edu.iff.livraria.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/pedido")
public class PedidoRestController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping("")
    @ResponseBody
    @Operation(summary = "Adicionar um pedido em específico")
    public String adicionarPedido(@RequestParam Long clienteId) {
        return pedidoService.adicionarPedido(clienteId);
    }

    @PutMapping("/{id}")
    @ResponseBody
    @Operation(summary = "Atualizar um pedido em específico")
    public String atualizarPedido(@PathVariable("id") Long id,
                                  @RequestParam Long clienteId,
                                  @RequestParam float valorTotal,
                                  @RequestParam Date dataPedido,
                                  @RequestParam Date dataEntrega,
                                  @RequestParam String formaPagamento) {
        return pedidoService.atualizarPedido(id, clienteId, valorTotal, dataPedido, dataEntrega, formaPagamento);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    @Operation(summary = "Deletar um pedido em específico")
    public String deletarPedido(@PathVariable("id") Long id) {
        return pedidoService.deletarPedido(id);
    }

    @GetMapping("/{id}")
    @ResponseBody
    @Operation(summary = "Retornar um pedido em específico")
    public Pedido buscarPedido(@PathVariable("id") Long id) {
        return pedidoService.buscarPorId(id);
    }

    @PatchMapping("/{id}")
    @ResponseBody
    @Operation(summary = "Finalizar um pedido em específico")
    public String finalizarPedido(@PathVariable("id") Long id) {
        return pedidoService.finalizarPedido(id);
    }

    @GetMapping("")
    @ResponseBody
    @Operation(summary = "Listar todos os pedidos")
    public List<Pedido> listarPedidos() {
        return pedidoService.listarPedidos();
    }

    @PostMapping("/{id}/item")
    @ResponseBody
    @Operation(summary = "Adicionar um item ao pedido em um cliente em específico")
    public String adicionarItem(@PathVariable("id") Long id, String titulo, int quantidade, int tipo) {    	    	
        return pedidoService.adicionarItemAoPedido(id, titulo, quantidade, tipo);
    }

    @DeleteMapping("/{id}/item")
    @ResponseBody
    @Operation(summary = "Deletar um livro do pedido em um cliente em específico")
    public String deletarItem(@PathVariable("id") Long id, @RequestParam Long itemId) {
        return pedidoService.deletarItemDoPedido(id, itemId);
    }

    @GetMapping("/{id}/itens")
    @ResponseBody
    @Operation(summary = "Listar os itens do pedido de um cliente em específico")
    public List<Item> listarItens(@PathVariable("id") Long id) {
        return pedidoService.listarItensDoPedido(id);
    }

    @PostMapping("/{id}/item/{itemId}/aluguel")
    @ResponseBody
    @Operation(summary = "Adicionar um aluguel ao item de um pedido em um cliente em específico")
    public String adicionarAluguel(@PathVariable("id") Long id, @PathVariable("itemId") Long itemId) {
        return pedidoService.adicionarAluguelAoItem(id, itemId);
    }

    @DeleteMapping("/{id}/item/{itemId}/aluguel")
    @ResponseBody
    @Operation(summary = "Deletar um aluguel do item de um pedido em um cliente em específico")
    public String deletarAluguel(@PathVariable("id") Long id, @PathVariable("itemId") Long itemId) {
        return pedidoService.deletarAluguelDoItem(id, itemId);
    }

    @GetMapping("/{id}/item/{itemId}/aluguel/{aluguelId}")
    @ResponseBody
    @Operation(summary = "Buscar um aluguel específico do item de um pedido de um cliente")
    public String buscarAluguel(@PathVariable("id") Long id, @PathVariable("itemId") Long itemId, @PathVariable("aluguelId") Long aluguelId) {
        return pedidoService.buscarAluguel(id, itemId, aluguelId);
    }

    @GetMapping("/{id}/cliente/{clienteId}/alugueis")
    @ResponseBody
    @Operation(summary = "Listar todos os alugueis feitos pelo cliente em um pedido")
    public List<Aluguel> listarAlugueisCliente(@PathVariable("id") Long id, @PathVariable("clienteId") Long clienteId) {
        return pedidoService.listarAlugueisPorCliente(id, clienteId);
    }
}
