package uts.edu.java.sistemaacademico.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import uts.edu.java.sistemaacademico.model.Docente;
import uts.edu.java.sistemaacademico.model.Usuario;

public interface DocenteRepository extends JpaRepository<Docente, Integer> {

    Optional<Docente> findByUsuario(Usuario usuario);
}