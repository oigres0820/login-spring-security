package uts.edu.java.sistemaacademico.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import uts.edu.java.sistemaacademico.model.*;
import uts.edu.java.sistemaacademico.repository.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private CarreraRepository carreraRepository;
    @Autowired private SemestreRepository semestreRepository;
    @Autowired private MateriaRepository materiaRepository;
    @Autowired private DocenteRepository docenteRepository;
    @Autowired private EstudianteRepository estudianteRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    // --------------------------------------------------------
    // MÉTODO AUXILIAR
    // --------------------------------------------------------

    private Usuario getUsuarioAdmin(Authentication authentication) {
        String username = authentication.getName();
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    // --------------------------------------------------------
    // INICIO
    // --------------------------------------------------------

    @GetMapping("/inicio")
    public String inicio(Model model, Authentication authentication) {
        model.addAttribute("usuario", getUsuarioAdmin(authentication));
        model.addAttribute("totalUsuarios", usuarioRepository.count());
        model.addAttribute("totalDocentes", docenteRepository.count());
        model.addAttribute("totalEstudiantes", estudianteRepository.count());
        model.addAttribute("totalMaterias", materiaRepository.count());
        return "admin/inicio";
    }

    // --------------------------------------------------------
    // USUARIOS
    // --------------------------------------------------------

    @GetMapping("/usuarios")
    public String listarUsuarios(Model model, Authentication authentication) {
        model.addAttribute("usuario", getUsuarioAdmin(authentication));
        model.addAttribute("usuarios", usuarioRepository.findAll());
        model.addAttribute("docenteRepository", docenteRepository);
        model.addAttribute("estudianteRepository", estudianteRepository);
        return "admin/usuarios";
    }

    @GetMapping("/usuarios/nuevo")
    public String formNuevoUsuario(Model model, Authentication authentication) {
        model.addAttribute("usuario", getUsuarioAdmin(authentication));
        model.addAttribute("nuevoUsuario", new Usuario());
        model.addAttribute("carreras", carreraRepository.findAll());
        model.addAttribute("semestres", semestreRepository.findAll());
        return "admin/usuario-form";
    }

    @PostMapping("/usuarios/guardar")
    public String guardarUsuario(@ModelAttribute("nuevoUsuario") Usuario nuevoUsuario,
                                  @RequestParam(required = false) Integer idCarrera,
                                  @RequestParam(required = false) Integer idSemestre,
                                  Model model,
                                  Authentication authentication) {

        // Validar username duplicado
        Optional<Usuario> existenteUsername = usuarioRepository.findByUsername(nuevoUsuario.getUsername());
        if (existenteUsername.isPresent() &&
            !existenteUsername.get().getIdUsuario().equals(nuevoUsuario.getIdUsuario())) {
            model.addAttribute("usuario", getUsuarioAdmin(authentication));
            model.addAttribute("nuevoUsuario", nuevoUsuario);
            model.addAttribute("carreras", carreraRepository.findAll());
            model.addAttribute("semestres", semestreRepository.findAll());
            model.addAttribute("error", "Ya existe un usuario con ese username.");
            return "admin/usuario-form";
        }

        // Validar cédula duplicada
        Optional<Usuario> existenteCedula = usuarioRepository.findByCedula(nuevoUsuario.getCedula());
        if (existenteCedula.isPresent() &&
            !existenteCedula.get().getIdUsuario().equals(nuevoUsuario.getIdUsuario())) {
            model.addAttribute("usuario", getUsuarioAdmin(authentication));
            model.addAttribute("nuevoUsuario", nuevoUsuario);
            model.addAttribute("carreras", carreraRepository.findAll());
            model.addAttribute("semestres", semestreRepository.findAll());
            model.addAttribute("error", "Ya existe un usuario con esa cédula.");
            return "admin/usuario-form";
        }

        // Cifrar contraseña
        Integer idUsuario = nuevoUsuario.getIdUsuario();
        if (idUsuario == null || idUsuario == 0) {
            nuevoUsuario.setPassword(passwordEncoder.encode(nuevoUsuario.getPassword()));
        } else {
            Usuario existente = usuarioRepository.findById(idUsuario).orElseThrow();
            if (nuevoUsuario.getPassword() == null || nuevoUsuario.getPassword().isBlank()) {
                nuevoUsuario.setPassword(existente.getPassword());
            } else {
                nuevoUsuario.setPassword(passwordEncoder.encode(nuevoUsuario.getPassword()));
            }
        }

        // Activar por defecto si es nuevo
        if (nuevoUsuario.getActivo() == null) {
            nuevoUsuario.setActivo(true);
        }

        usuarioRepository.save(nuevoUsuario);

        if (nuevoUsuario.getRol().equals("DOCENTE")) {
            if (docenteRepository.findByUsuario(nuevoUsuario).isEmpty()) {
                Docente docente = new Docente();
                docente.setUsuario(nuevoUsuario);
                docenteRepository.save(docente);
            }
        }

        if (nuevoUsuario.getRol().equals("ESTUDIANTE") && idCarrera != null && idSemestre != null) {
            if (estudianteRepository.findByUsuario(nuevoUsuario).isEmpty()) {
                Estudiante estudiante = new Estudiante();
                estudiante.setUsuario(nuevoUsuario);
                estudiante.setCarrera(carreraRepository.findById(idCarrera).orElseThrow());
                estudiante.setSemestre(semestreRepository.findById(idSemestre).orElseThrow());
                estudianteRepository.save(estudiante);
            }
        }

        return "redirect:/admin/usuarios";
    }

    @GetMapping("/usuarios/editar/{id}")
    public String formEditarUsuario(@PathVariable Integer id, Model model, Authentication authentication) {
        model.addAttribute("usuario", getUsuarioAdmin(authentication));
        model.addAttribute("nuevoUsuario", usuarioRepository.findById(id).orElseThrow());
        model.addAttribute("carreras", carreraRepository.findAll());
        model.addAttribute("semestres", semestreRepository.findAll());
        return "admin/usuario-form";
    }

    @GetMapping("/usuarios/desactivar/{id}")
    public String desactivarUsuario(@PathVariable Integer id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow();
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/usuarios/activar/{id}")
    public String activarUsuario(@PathVariable Integer id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow();
        usuario.setActivo(true);
        usuarioRepository.save(usuario);
        return "redirect:/admin/usuarios";
    }

    // --------------------------------------------------------
    // ASIGNAR MATERIAS A DOCENTE
    // --------------------------------------------------------

    @GetMapping("/docente/{id}/materias")
    public String verMateriasDocente(@PathVariable Integer id, Model model, Authentication authentication) {
        model.addAttribute("usuario", getUsuarioAdmin(authentication));
        Docente docente = docenteRepository.findById(id).orElseThrow();
        List<Materia> todasLasMaterias = materiaRepository.findAll();
        model.addAttribute("docente", docente);
        model.addAttribute("todasLasMaterias", todasLasMaterias);
        return "admin/docente-materias";
    }

    @PostMapping("/docente/{id}/materias/guardar")
    public String guardarMateriasDocente(@PathVariable Integer id,
                                          @RequestParam(required = false) List<Integer> idMaterias) {
        Docente docente = docenteRepository.findById(id).orElseThrow();
        if (idMaterias != null) {
            List<Materia> materias = materiaRepository.findAllById(idMaterias);
            docente.setMaterias(materias);
        } else {
            docente.setMaterias(List.of());
        }
        docenteRepository.save(docente);
        return "redirect:/admin/usuarios";
    }

    // --------------------------------------------------------
    // MATRICULAR ESTUDIANTE EN MATERIAS
    // --------------------------------------------------------

    @GetMapping("/estudiante/{id}/materias")
    public String verMateriasEstudiante(@PathVariable Integer id, Model model, Authentication authentication) {
        model.addAttribute("usuario", getUsuarioAdmin(authentication));
        Estudiante estudiante = estudianteRepository.findById(id).orElseThrow();
        List<Materia> todasLasMaterias = materiaRepository.findAll();
        model.addAttribute("estudiante", estudiante);
        model.addAttribute("todasLasMaterias", todasLasMaterias);
        return "admin/estudiante-materias";
    }

    @PostMapping("/estudiante/{id}/materias/guardar")
    public String guardarMateriasEstudiante(@PathVariable Integer id,
                                             @RequestParam(required = false) List<Integer> idMaterias) {
        Estudiante estudiante = estudianteRepository.findById(id).orElseThrow();
        if (idMaterias != null) {
            List<Materia> materias = materiaRepository.findAllById(idMaterias);
            estudiante.setMaterias(materias);
        } else {
            estudiante.setMaterias(List.of());
        }
        estudianteRepository.save(estudiante);
        return "redirect:/admin/usuarios";
    }

    // --------------------------------------------------------
    // MATERIAS
    // --------------------------------------------------------

    @GetMapping("/materias")
    public String listarMaterias(Model model, Authentication authentication) {
        model.addAttribute("usuario", getUsuarioAdmin(authentication));
        model.addAttribute("materias", materiaRepository.findAll());
        return "admin/materias";
    }

    @GetMapping("/materias/nueva")
    public String formNuevaMateria(Model model, Authentication authentication) {
        model.addAttribute("usuario", getUsuarioAdmin(authentication));
        model.addAttribute("materia", new Materia());
        model.addAttribute("carreras", carreraRepository.findAll());
        model.addAttribute("semestres", semestreRepository.findAll());
        return "admin/materia-form";
    }

    @PostMapping("/materias/guardar")
    public String guardarMateria(@ModelAttribute("materia") Materia materia,
                                  @RequestParam Integer idCarrera,
                                  @RequestParam Integer idSemestre) {
        materia.setCarrera(carreraRepository.findById(idCarrera).orElseThrow());
        materia.setSemestre(semestreRepository.findById(idSemestre).orElseThrow());
        materiaRepository.save(materia);
        return "redirect:/admin/materias";
    }

    @GetMapping("/materias/editar/{id}")
    public String formEditarMateria(@PathVariable Integer id, Model model, Authentication authentication) {
        model.addAttribute("usuario", getUsuarioAdmin(authentication));
        model.addAttribute("materia", materiaRepository.findById(id).orElseThrow());
        model.addAttribute("carreras", carreraRepository.findAll());
        model.addAttribute("semestres", semestreRepository.findAll());
        return "admin/materia-form";
    }

    @GetMapping("/materias/eliminar/{id}")
    public String eliminarMateria(@PathVariable Integer id) {
        materiaRepository.deleteById(id);
        return "redirect:/admin/materias";
    }
}