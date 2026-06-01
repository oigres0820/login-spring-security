package uts.edu.java.sistemaacademico.model;

import jakarta.persistence.*;

@Entity
@Table(name = "notas")
public class Nota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idNota;

    @ManyToOne
    @JoinColumn(name = "idEstudiante")
    private Estudiante estudiante;

    @ManyToOne
    @JoinColumn(name = "idMateria")
    private Materia materia;

    @ManyToOne
    @JoinColumn(name = "idDocente")
    private Docente docente;

    private Integer corte;
    private Double parcial;
    private Double actividades;
    private Double autoevaluacion;
    private Double definitiva;

    public Nota() {}

    public Integer getIdNota() { return idNota; }
    public void setIdNota(Integer idNota) { this.idNota = idNota; }

    public Estudiante getEstudiante() { return estudiante; }
    public void setEstudiante(Estudiante estudiante) { this.estudiante = estudiante; }

    public Materia getMateria() { return materia; }
    public void setMateria(Materia materia) { this.materia = materia; }

    public Docente getDocente() { return docente; }
    public void setDocente(Docente docente) { this.docente = docente; }

    public Integer getCorte() { return corte; }
    public void setCorte(Integer corte) { this.corte = corte; }

    public Double getParcial() { return parcial; }
    public void setParcial(Double parcial) { this.parcial = parcial; }

    public Double getActividades() { return actividades; }
    public void setActividades(Double actividades) { this.actividades = actividades; }

    public Double getAutoevaluacion() { return autoevaluacion; }
    public void setAutoevaluacion(Double autoevaluacion) { this.autoevaluacion = autoevaluacion; }

    public Double getDefinitiva() { return definitiva; }
    public void setDefinitiva(Double definitiva) { this.definitiva = definitiva; }
}