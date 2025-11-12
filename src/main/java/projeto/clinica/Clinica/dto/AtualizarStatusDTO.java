package projeto.clinica.Clinica.dto;

import projeto.clinica.Clinica.model.StatusConsulta;

// Este DTO é usado especificamente para a requisição 
// de mudança de status no endpoint PATCH
public class AtualizarStatusDTO {

    private StatusConsulta novoStatus;

    // Getters e Setters
    public StatusConsulta getNovoStatus() {
        return novoStatus;
    }

    public void setNovoStatus(StatusConsulta novoStatus) {
        this.novoStatus = novoStatus;
    }

}
