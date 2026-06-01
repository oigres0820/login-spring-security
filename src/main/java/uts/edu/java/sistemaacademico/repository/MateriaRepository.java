package uts.edu.java.sistemaacademico.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import uts.edu.java.sistemaacademico.model.Carrera;
import uts.edu.java.sistemaacademico.model.Materia;
import uts.edu.java.sistemaacademico.model.Semestre;

public interface MateriaRepository extends JpaRepository<Materia, Integer> {

    List<Materia> findByCarreraAndSemestre(Carrera carrera, Semestre semestre);
}