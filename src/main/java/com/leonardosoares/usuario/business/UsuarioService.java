package com.leonardosoares.usuario.business;

import com.leonardosoares.usuario.business.converter.UsuarioConverter;
import com.leonardosoares.usuario.business.dto.EnderecoDTO;
import com.leonardosoares.usuario.business.dto.TelefoneDTO;
import com.leonardosoares.usuario.business.dto.UsuarioDTO;
import com.leonardosoares.usuario.infrastructure.entity.Endereco;
import com.leonardosoares.usuario.infrastructure.entity.Telefone;
import com.leonardosoares.usuario.infrastructure.entity.Usuario;
import com.leonardosoares.usuario.infrastructure.exceptions.ConflictException;
import com.leonardosoares.usuario.infrastructure.exceptions.ResourceNotFoundException;
import com.leonardosoares.usuario.infrastructure.repository.EnderecoRepository;
import com.leonardosoares.usuario.infrastructure.repository.TelefoneRepository;
import com.leonardosoares.usuario.infrastructure.repository.UsuarioRepository;
import com.leonardosoares.usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EnderecoRepository enderecoRepository;
    private final TelefoneRepository telefoneRepository;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO) {
        emailExiste(usuarioDTO.getEmail());
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }

    public void emailExiste(String email) {
        try {
            boolean existe = verificaEmailExistente(email);
            if (existe) {
                throw new ConflictException("Email já cadastrado " + email);
            }
        } catch (ConflictException e) {
            throw new ConflictException("Email já cadastrado " + e.getCause());
        }
    }

    public boolean verificaEmailExistente(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public Usuario buscarUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Email não encontrado"));
    }

    public void deletaUsuarioPorEmail(String email) {
        usuarioRepository.deleteByEmail(email);
    }

    public UsuarioDTO atualizarDadosUsuario(UsuarioDTO usuarioDTO, String token) {

        //Busca o email pelo token do usuário logado
        String emailUsuario = jwtUtil.extrairEmailToken(token.substring(7));

        //Criptografa a senha caso o usuário tenha alterado a senha
        usuarioDTO.setSenha(usuarioDTO.getSenha() != null ? passwordEncoder.encode(usuarioDTO.getSenha()) : null);

        //Busca o usuário pelo email
        Usuario usuarioEntity = usuarioRepository.findByEmail(emailUsuario).orElseThrow(() -> new ResourceNotFoundException("Email não encontrado"));

        //Chama o atualizaDadosUsuario e passa o que recebeu usuarioDTO e o que esta no banco usuarioEntity
        Usuario usuarioAtualizado = usuarioConverter.atualizaDadosUsuario(usuarioDTO, usuarioEntity);

        //Salva e converte para DTO
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuarioAtualizado));
    }

    public EnderecoDTO atualizaDadosEndereco(Long idEndereco, EnderecoDTO enderecoDTO) {

        Endereco endereco = enderecoRepository.findById(idEndereco).orElseThrow(() -> new ResourceNotFoundException("id não encontrado " + idEndereco));

        Endereco enderecoAtualizado = usuarioConverter.atualizaDadosEndereco(endereco, enderecoDTO);

        return usuarioConverter.paraEnderecoDTO(enderecoRepository.save(enderecoAtualizado));

    }

    public TelefoneDTO atualizaDadosTelefone(Long idTelefone, TelefoneDTO telefoneDTO) {

        Telefone telefone = telefoneRepository.findById(idTelefone).orElseThrow(() -> new ResourceNotFoundException("id não encontrado " + idTelefone));

        Telefone telefoneAtualizado = usuarioConverter.atualizaDadosTelefone(telefone, telefoneDTO);

        return usuarioConverter.paraTelefoneDTO(telefoneRepository.save(telefoneAtualizado));

    }


}
