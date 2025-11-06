package projeto.clinica.Clinica.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import projeto.clinica.Clinica.dto.ConsultaRequestDTO;
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

}
