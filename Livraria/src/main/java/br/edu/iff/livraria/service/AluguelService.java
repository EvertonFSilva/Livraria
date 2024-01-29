package br.edu.iff.livraria.service;

import br.edu.iff.livraria.entities.Aluguel;
import br.edu.iff.livraria.entities.Item;
import br.edu.iff.livraria.repository.AluguelRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AluguelService {

    @Autowired
    private AluguelRepository aluguelRepository;

    public Aluguel buscarPorId(Long id) {
        return aluguelRepository.findById(id).orElse(null);
    }

    public List<Aluguel> listarAlugueis() {
        return aluguelRepository.listarAlugueis();
    }

    public List<Aluguel> listarAlugueisPorCliente(Long clienteId) {
        return aluguelRepository.listarAlugueisPorCliente(clienteId);
    }

    public boolean adicionarAluguel(Item item) {
        if (item.getAluguel() == null) {
            Date dataInicio = new Date();
            Date dataFim = new Date(dataInicio.getTime() + (7 * 24 * 60 * 60 * 1000));
            Aluguel novoAluguel = new Aluguel(item, dataInicio, dataFim);
            aluguelRepository.save(novoAluguel);
            item.setAluguel(novoAluguel);
            return true;
        }
        return false;
    }

    public boolean atualizarAluguel(Aluguel aluguel, Date dataInicio, Date dataFim) {
        if (aluguel != null) {
            aluguel.setDataInicio(dataInicio);
            aluguel.setDataFim(dataFim);
            aluguelRepository.save(aluguel);
            return true;
        }
        return false;
    }

    public boolean deletarAluguel(Long id) {
        Aluguel aluguelExistente = buscarPorId(id);

        if (aluguelExistente != null) {
            aluguelRepository.delete(aluguelExistente);
            return true;
        }
        return false;
    }
}
