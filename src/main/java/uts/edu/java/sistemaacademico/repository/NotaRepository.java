package uts.edu.java.sistemaacademico.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import uts.edu.java.sistemaacademico.model.Docente;
import uts.edu.java.sistemaacademico.model.Estudiante;
import uts.edu.java.sistemaacademico.model.Materia;
import uts.edu.java.sistemaacademico.model.Nota;

public interface NotaRepository extends JpaRepository<Nota, Integer> {

    List<Nota> findByEstudiante(Estudiante estudiante);

    List<Nota> findByEstudianteAndMateria(Estudiante estudiante, Materia materia);

    Optional<Nota> findByEstudianteAndMateriaAndCorte(Estudiante estudiante, Materia materia, Integer corte);

    List<Nota> findByDocenteAndMateria(Docente docente, Materia materia);
}