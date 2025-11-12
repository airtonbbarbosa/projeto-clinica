package projeto.clinica.Clinica.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import projeto.clinica.Clinica.dto.AtualizarStatusDTO;
import projeto.clinica.Clinica.dto.ConsultaRequestDTO;
import projeto.clinica.Clinica.dto.ConsultaResponseDTO;
import projeto.clinica.Clinica.model.Consulta;
import projeto.clinica.Clinica.model.Medico;
import projeto.clinica.Clinica.model.StatusConsulta;
import projeto.clinica.Clinica.repository.ConsultaRepository;
import projeto.clinica.Clinica.repository.MedicoRepository;

@Service
public class ConsultaService {

     private final ConsultaRepository consultaRepository;
    private final MedicoRepository medicoRepository;

    public ConsultaService(ConsultaRepository consultaRepository, MedicoRepository medicoRepository){
        this.consultaRepository = consultaRepository;
        this.medicoRepository = medicoRepository;
    }

     @Transactional
    public Consulta agendar(ConsultaRequestDTO dto) {
        
        // 1. Buscar o médico pelo ID fornecido no DTO
        Medico medico = medicoRepository.findById(dto.getMedicoId())
                .orElseThrow(() -> new EntityNotFoundException("Médico com ID " + dto.getMedicoId() + " não encontrado."));

        // 2. Criar a nova entidade Consulta
        Consulta novaConsulta = new Consulta();

        // 3. Mapear os dados do DTO para a Entidade
        novaConsulta.setNomeCliente(dto.getNomeCliente());
        novaConsulta.setDocumento(dto.getDocumento());
        novaConsulta.setContato(dto.getContato());
        novaConsulta.setDataHora(dto.getDataHora());
        novaConsulta.setObservacoes(dto.getObservacoes());
        
        // 4. Definir os dados que não vêm do DTO
        novaConsulta.setMedico(medico);
        novaConsulta.setStatus(StatusConsulta.PENDENTE); // Toda nova consulta começa como pendente

        // 5. Salvar no banco de dados
        return consultaRepository.save(novaConsulta);
    }

    // NOVO MÉTODO: Listar todas as consultas
    @Transactional
    public List<ConsultaResponseDTO> listarTodas() {
        return consultaRepository.findAll()
                .stream()
                .map(ConsultaResponseDTO::new) // Converte cada Consulta em ConsultaResponseDTO
                .collect(Collectors.toList());
    }

    // NOVO MÉTODO: Buscar uma consulta por ID
    @Transactional
    public ConsultaResponseDTO buscarPorId(Long id) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Consulta com ID " + id + " não encontrada."));
        
        return new ConsultaResponseDTO(consulta); // Converte a Consulta encontrada em DTO
    }

    // NOVO MÉTODO: Atualizar status
    @Transactional
    public ConsultaResponseDTO atualizarStatus(Long id, AtualizarStatusDTO dto) {
        // 1. Encontrar a consulta
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Consulta com ID " + id + " não encontrada."));

        // 2. Atualizar o status
        consulta.setStatus(dto.getNovoStatus());

        // 3. Salvar a consulta atualizada
        Consulta consultaAtualizada = consultaRepository.save(consulta);

        // 4. Retornar o DTO de resposta
        return new ConsultaResponseDTO(consultaAtualizada);
    }

    // NOVO MÉTODO: Deletar consulta
    @Transactional
    public void deletarPorId(Long id) {
        // Usamos existsById para garantir que a exceção seja lançada
        // antes de tentar deletar algo que não existe
        if (!consultaRepository.existsById(id)) {
            throw new EntityNotFoundException("Consulta com ID " + id + " não encontrada.");
        }
        
        // Se encontramos, deletamos
        consultaRepository.deleteById(id);
    }

}
