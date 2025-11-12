package projeto.clinica.Clinica.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.persistence.EntityNotFoundException;
import projeto.clinica.Clinica.dto.AtualizarStatusDTO;
import projeto.clinica.Clinica.dto.ConsultaRequestDTO;
import projeto.clinica.Clinica.dto.ConsultaResponseDTO;
import projeto.clinica.Clinica.model.Consulta;
import projeto.clinica.Clinica.service.ConsultaService;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {

    // 1. Dependência declarada como 'final'
    private final ConsultaService consultaService;

    // 2. Construtor para injeção de dependência
    public ConsultaController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @PostMapping 
    public ResponseEntity<Consulta> criarConsulta(@RequestBody ConsultaRequestDTO dto) {
        try {
            Consulta consultaAgendada = consultaService.agendar(dto);
            // Retorna 201 Created com a consulta criada no corpo da resposta
            return new ResponseEntity<>(consultaAgendada, HttpStatus.CREATED);
        } catch (Exception e) {
            // Se o médico não for encontrado, por exemplo
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // NOVO ENDPOINT: GET /consultas
    @GetMapping
    public ResponseEntity<List<ConsultaResponseDTO>> listarConsultas() {
        List<ConsultaResponseDTO> consultas = consultaService.listarTodas();
        return ResponseEntity.ok(consultas); // Retorna 200 OK com a lista
    }

    // NOVO ENDPOINT: GET /consultas/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ConsultaResponseDTO> buscarConsultaPorId(@PathVariable Long id) {
        try {
            ConsultaResponseDTO consulta = consultaService.buscarPorId(id);
            return ResponseEntity.ok(consulta); // Retorna 200 OK com a consulta
        } catch (EntityNotFoundException e) {
            // Se o service lançar a exceção (consulta não encontrada)
            return ResponseEntity.notFound().build(); // Retorna 404 Not Found
        }
    }

    // NOVO ENDPOINT: PATCH api/consultas/{id}/status
    // (Usamos PATCH porque é uma atualização parcial, apenas do status)
    @PatchMapping("/{id}/status")
    public ResponseEntity<ConsultaResponseDTO> atualizarStatusConsulta(
            @PathVariable Long id, 
            @RequestBody AtualizarStatusDTO dto) {
        try {
            ConsultaResponseDTO consultaAtualizada = consultaService.atualizarStatus(id, dto);
            return ResponseEntity.ok(consultaAtualizada); // Retorna 200 OK com a consulta atualizada
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build(); // Retorna 404
        }
    }

    // NOVO ENDPOINT: DELETE /consultas/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarConsulta(@PathVariable Long id) {
        try {
            consultaService.deletarPorId(id);
            // 204 No Content: Sucesso na exclusão, sem corpo de resposta
            return ResponseEntity.noContent().build(); 
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build(); // Retorna 404
        }
    }

}
