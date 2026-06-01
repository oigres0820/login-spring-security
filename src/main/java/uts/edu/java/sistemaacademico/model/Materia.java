package uts.edu.java.sistemaacademico.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "materias")
public class Materia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMateria;

    private String nombreMateria;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idCarrera")
    private Carrera carrera;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idSemestre")
    private Semestre semestre;

    @OneToMany(mappedBy = "materia", fetch = FetchType.EAGER)
    private List<Nota> notas;

    public Materia() {}

    public Integer getIdMateria() { return idMateria; }
    public void setIdMateria(Integer idMateria) { this.idMateria = idMateria; }

    public String getNombreMateria() { return nombreMateria; }
    public void setNombreMateria(String nombreMateria) { this.nombreMateria = nombreMateria; }

    public Carrera getCarrera() { return carrera; }
    public void setCarrera(Carrera carrera) { this.carrera = carrera; }

    public Semestre getSemestre() { return semestre; }
    public void setSemestre(Semestre semestre) { this.semestre = semestre; }

    public List<Nota> getNotas() { return notas; }
    public void setNotas(List<Nota> notas) { this.notas = notas; }
}