package com.leonardosoares.usuario.controller;

import com.leonardosoares.usuario.business.UsuarioService;
import com.leonardosoares.usuario.business.dto.EnderecoDTO;
import com.leonardosoares.usuario.business.dto.TelefoneDTO;
import com.leonardosoares.usuario.business.dto.UsuarioDTO;
import com.leonardosoares.usuario.infrastructure.entity.Usuario;
import com.leonardosoares.usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<UsuarioDTO> salvaUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        return ResponseEntity.ok(usuarioService.salvaUsuario(usuarioDTO));
    }

    @PostMapping("/login")
    public String login(@RequestBody UsuarioDTO usuarioDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usuarioDTO.getEmail(),
                        usuarioDTO.getSenha())
        );
        return "Bearer " + jwtUtil.generateToken(authentication.getName());
    }

    @GetMapping
    public ResponseEntity<Usuario> buscaUsuarioPorEmail(@RequestParam("email") String email) {
        return ResponseEntity.ok(usuarioService.buscarUsuarioPorEmail(email));
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deletaUsuarioPorEmail(@PathVariable String email) {

        usuarioService.deletaUsuarioPorEmail(email);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<UsuarioDTO> atualizarDadosUsuario(@RequestBody UsuarioDTO usuarioDTO,
                                                            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(usuarioService.atualizarDadosUsuario(usuarioDTO, token));

    }

    @PutMapping("endereco")
    public ResponseEntity<EnderecoDTO> atualizarDadosEndereco(@RequestBody EnderecoDTO enderecoDTO,
                                                           @RequestParam("id") Long idEndereco) {
        return ResponseEntity.ok(usuarioService.atualizaDadosEndereco(idEndereco, enderecoDTO));
    }

    @PutMapping("telefone")
    public ResponseEntity<TelefoneDTO> atualizarDadosTelefone(@RequestBody TelefoneDTO telefoneDTO,
                                                              @RequestParam("id") Long idTelefone) {
        return ResponseEntity.ok(usuarioService.atualizaDadosTelefone(idTelefone, telefoneDTO));
    }


}
