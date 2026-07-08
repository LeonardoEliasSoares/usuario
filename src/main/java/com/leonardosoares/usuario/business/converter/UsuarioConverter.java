package com.leonardosoares.usuario.business.converter;

import com.leonardosoares.usuario.business.dto.EnderecoDTO;
import com.leonardosoares.usuario.business.dto.TelefoneDTO;
import com.leonardosoares.usuario.business.dto.UsuarioDTO;
import com.leonardosoares.usuario.infrastructure.entity.Endereco;
import com.leonardosoares.usuario.infrastructure.entity.Telefone;
import com.leonardosoares.usuario.infrastructure.entity.Usuario;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsuarioConverter {

    //    CONVERTE OS DADOS DE USUARIO PARA USUARIODTO
    public Usuario paraUsuario(UsuarioDTO usuarioDTO) {

        return Usuario.builder()
                .nome(usuarioDTO.getNome())
                .email(usuarioDTO.getEmail())
                .senha(usuarioDTO.getSenha())
                .enderecos(paraListaEnderecos(usuarioDTO.getEnderecos()))
                .telefones(paraListaTelefones(usuarioDTO.getTelefones()))
                .build();
    }

    //    RECEBE UMA LISTA DE ENDERECODTO E TRANSFORMA CADA ITEM EM UM OBJETO ENDERECO
    public List<Endereco> paraListaEnderecos(List<EnderecoDTO> enderecosDTO) {
        return enderecosDTO.stream().map(this::paraEndereco).toList();
    }

    //    CONVERTE UM UNICO ENDERECODTO EM UM OBJETO ENDERECO
    public Endereco paraEndereco(EnderecoDTO enderecoDTO) {
        return Endereco.builder()
                .rua(enderecoDTO.getRua())
                .numero(enderecoDTO.getNumero())
                .cidade(enderecoDTO.getCidade())
                .complemento(enderecoDTO.getComplemento())
                .cep(enderecoDTO.getCep())
                .estado(enderecoDTO.getEstado())
                .build();
    }

    //    CONVERTE UMA LISTA DE TELEFONEDTO EM UMA LISTA DE TELEFONE
    public List<Telefone> paraListaTelefones(List<TelefoneDTO> telefoneDTO) {
        return telefoneDTO.stream().map(this::paraTelefone).toList();
    }

    // CONVERTE UM UNICO TELEFONEDTO EM UM OBJETO TELEFONE
    public Telefone paraTelefone(TelefoneDTO telefoneDTO) {
        return Telefone.builder()
                .numero(telefoneDTO.getNumero())
                .ddd(telefoneDTO.getDdd())
                .build();
    }

    //#############################################################################################

    //    CONVERTE OS DADOS DE USUARIODTO PARA USUARIO
    public UsuarioDTO paraUsuarioDTO(Usuario usuario) {

        return UsuarioDTO.builder()
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .senha(usuario.getSenha())
                .enderecos(paraListaEnderecosDTO(usuario.getEnderecos()))
                .telefones(paraListaTelefonesDTO(usuario.getTelefones()))
                .build();
    }

    //    RECEBE UMA LISTA DE ENDERECO E TRANSFORMA CADA ITEM EM UM OBJETO ENDERECODTO
    public List<EnderecoDTO> paraListaEnderecosDTO(List<Endereco> enderecosDTO) {
        return enderecosDTO.stream().map(this::paraEnderecoDTO).toList();
    }

    //    CONVERTE UM UNICO ENDERECO EM UM OBJETO ENDERECODTO
    public EnderecoDTO paraEnderecoDTO(Endereco endereco) {
        return EnderecoDTO.builder()
                .rua(endereco.getRua())
                .numero(endereco.getNumero())
                .cidade(endereco.getCidade())
                .complemento(endereco.getComplemento())
                .cep(endereco.getCep())
                .estado(endereco.getEstado())
                .build();
    }

    //    CONVERTE UMA LISTA DE TELEFONEEM UMA LISTA DE TELEFONEDTO
    public List<TelefoneDTO> paraListaTelefonesDTO(List<Telefone> telefone) {
        return telefone.stream().map(this::paraTelefoneDTO).toList();
    }

    //    CONVERTE UMA LISTA DE TELEFONE EM UMA LISTA DE TELEFONEDTO
    public TelefoneDTO paraTelefoneDTO(Telefone telefone) {
        return TelefoneDTO.builder()
                .numero(telefone.getNumero())
                .ddd(telefone.getDdd())
                .build();
    }


}
