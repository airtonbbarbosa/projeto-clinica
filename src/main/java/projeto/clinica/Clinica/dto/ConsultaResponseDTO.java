package projeto.clinica.Clinica.dto;

import projeto.clinica.Clinica.model.Consulta;
import projeto.clinica.Clinica.model.StatusConsulta;

public class ConsultaResponseDTO {

    private Long id;
    private String nomeCliente;
    private String documento;
    private String contato;
    private String dataHora;
    private StatusConsulta status;
    private String observacoes;
    private String nomeMedico; // Note: Apenas o nome do médico, não o objeto inteiro

    // Construtor que "mapeia" a Entidade Consulta para este DTO
    public ConsultaResponseDTO(Consulta consulta) {
        this.id = consulta.getId();
        this.nomeCliente = consulta.getNomeCliente();
        this.documento = consulta.getDocumento();
        this.contato = consulta.getContato();
        this.dataHora = consulta.getDataHora();
        this.status = consulta.getStatus();
        this.observacoes = consulta.getObservacoes();
        // Evitamos LazyInitializationException acessando o nome aqui
        if (consulta.getMedico() != null) {
            this.nomeMedico = consulta.getMedico().getNome();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public String getDataHora() {
        return dataHora;
    }

    public void setDataHora(String dataHora) {
        this.dataHora = dataHora;
    }

    public StatusConsulta getStatus() {
        return status;
    }

    public void setStatus(StatusConsulta status) {
        this.status = status;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public String getNomeMedico() {
        return nomeMedico;
    }

    public void setNomeMedico(String nomeMedico) {
        this.nomeMedico = nomeMedico;
    }

}
