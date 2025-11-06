package projeto.clinica.Clinica.service;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import projeto.clinica.Clinica.dto.ConsultaRequestDTO;
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

}
