package projeto.clinica.Clinica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import projeto.clinica.Clinica.model.Medico;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {

}
