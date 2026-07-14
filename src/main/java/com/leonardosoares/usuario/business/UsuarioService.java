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

    //Cria um novo usuario
    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO) {
        //verifica se o email ja existe no sistema
        emailExiste(usuarioDTO.getEmail());
        //encripta a senha do usuario
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        //converte para usuarioEntity e grava no banco
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }

    //verifica se o email ja existe no sistema
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

    //busca o usuarioEntity por email
    public Usuario buscarUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Email não encontrado"));
    }

    //Exclui um usuario passando o email
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

    //Altera/atualiza dados de endereço
    public EnderecoDTO atualizaDadosEndereco(Long idEndereco, EnderecoDTO enderecoDTO) {

        //verifica se tem esse idEndereco no banco e retorna um Endereco
        Endereco endereco = enderecoRepository.findById(idEndereco).orElseThrow(() -> new ResourceNotFoundException("id não encontrado " + idEndereco));

        //Aplica alterações ao endereço
        Endereco enderecoAtualizado = usuarioConverter.atualizaDadosEndereco(endereco, enderecoDTO);

        //converte para EnderecoDTO e grava
        return usuarioConverter.paraEnderecoDTO(enderecoRepository.save(enderecoAtualizado));

    }

    //Altera/atualiza dados de telefone
    public TelefoneDTO atualizaDadosTelefone(Long idTelefone, TelefoneDTO telefoneDTO) {

        //verifica se tem esse idTelefone no banco e retorna um Telefone
        Telefone telefone = telefoneRepository.findById(idTelefone).orElseThrow(() -> new ResourceNotFoundException("id não encontrado " + idTelefone));

        //Aplica alterações ao telefone
        Telefone telefoneAtualizado = usuarioConverter.atualizaDadosTelefone(telefone, telefoneDTO);

        //converte para TelefoneDTO e grava
        return usuarioConverter.paraTelefoneDTO(telefoneRepository.save(telefoneAtualizado));

    }

    //Inclui mais um endereço
    public EnderecoDTO adicionaEndereco(String token, EnderecoDTO enderecoDTO) {

        //busca o email pelo token do usuario
        String email = jwtUtil.extrairEmailToken(token.substring(7));

        //verifica se o email passado esta no banco e retorna o usuarioEntity
        Usuario usuarioEntity = usuarioRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Email não encontrado"));

        //Aplica o novo endereço
        Endereco enderecoEntity = usuarioConverter.adiconarEndereco(usuarioEntity, enderecoDTO);

        //Salva o novo endereço
        return usuarioConverter.paraEnderecoDTO(enderecoRepository.save(enderecoEntity));

    }
    //Inclui mais um telefone
    public TelefoneDTO adicionaTelefone(String token, TelefoneDTO telefoneDTO) {

        //busca o email pelo token do usuario
        String email = jwtUtil.extrairEmailToken(token.substring(7));

        //verifica se o email passado esta no banco e retorna o usuarioEntity
        Usuario usuarioEntity = usuarioRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Email não encontrado"));

        //Aplica o novo telefone
        Telefone telefoneEntity = usuarioConverter.adiconarTelefone(usuarioEntity, telefoneDTO);

        //Salva o novo telefone
        return usuarioConverter.paraTelefoneDTO(telefoneRepository.save(telefoneEntity));
    }


}
