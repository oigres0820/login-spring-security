package uts.edu.java.sistemaacademico.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import uts.edu.java.sistemaacademico.repository.UsuarioRepository;

@Controller
public class LoginController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    

    @GetMapping("/inicio")
    public String inicio(Authentication authentication) {
        String rol = authentication.getAuthorities().iterator().next().getAuthority();
        if (rol.equals("ROLE_ADMIN")) {
            return "redirect:/admin/inicio";
        } else if (rol.equals("ROLE_DOCENTE")) {
            return "redirect:/docente/inicio";
        } else {
            return "redirect:/estudiante/inicio";
        }
    }
}