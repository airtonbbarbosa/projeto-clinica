package projeto.clinica.Clinica.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import projeto.clinica.Clinica.model.Consulta;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    // Spring Data JPA cria a query: "SELECT c FROM Consulta c WHERE c.medico.id = ?1"
    List<Consulta> findByMedicoId(Long medicoId);

}
