package uts.edu.java.sistemaacademico.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "semestres")
public class Semestre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idSemestre;

    private Integer numeroSemestre;

    @OneToMany(mappedBy = "semestre")
    private List<Materia> materias;

    @OneToMany(mappedBy = "semestre")
    private List<Estudiante> estudiantes;

    public Semestre() {}

    public Integer getIdSemestre() { return idSemestre; }
    public void setIdSemestre(Integer idSemestre) { this.idSemestre = idSemestre; }

    public Integer getNumeroSemestre() { return numeroSemestre; }
    public void setNumeroSemestre(Integer numeroSemestre) { this.numeroSemestre = numeroSemestre; }

    public List<Materia> getMaterias() { return materias; }
    public void setMaterias(List<Materia> materias) { this.materias = materias; }

    public List<Estudiante> getEstudiantes() { return estudiantes; }
    public void setEstudiantes(List<Estudiante> estudiantes) { this.estudiantes = estudiantes; }
}