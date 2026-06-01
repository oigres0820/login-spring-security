package uts.edu.java.sistemaacademico.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import uts.edu.java.sistemaacademico.model.Carrera;
import uts.edu.java.sistemaacademico.model.Estudiante;
import uts.edu.java.sistemaacademico.model.Semestre;
import uts.edu.java.sistemaacademico.model.Usuario;

public interface EstudianteRepository extends JpaRepository<Estudiante, Integer> {

    Optional<Estudiante> findByUsuario(Usuario usuario);

    List<Estudiante> findByCarreraAndSemestre(Carrera carrera, Semestre semestre);
}