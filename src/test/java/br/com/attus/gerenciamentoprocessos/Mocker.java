package br.com.attus.gerenciamentoprocessos;

import br.com.attus.gerenciamentoprocessos.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class Mocker {

    public Usuario gerarUsuario(Long id) {
        return Usuario.builder()
                .id(id)
                .nome("fulano")
                .email("email@gmail.com")
                .senha("12345678")
                .build();
    }

}
