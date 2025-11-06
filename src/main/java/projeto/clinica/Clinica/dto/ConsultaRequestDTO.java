package projeto.clinica.Clinica.dto;

// Ele representa os dados que o cliente vai enviar no JSON
public class ConsultaRequestDTO {

    private String nomeCliente;
    private String documento;
    private String contato;
    private String dataHora;
    private String observacoes;
    private Long medicoId; // O cliente envia apenas o ID do médico
    
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
    public String getObservacoes() {
        return observacoes;
    }
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
    public Long getMedicoId() {
        return medicoId;
    }
    public void setMedicoId(Long medicoId) {
        this.medicoId = medicoId;
    }

}
