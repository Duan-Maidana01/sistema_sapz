package com.sagsa.api.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sagsa.api.models.Perfil;
import com.sagsa.api.models.Usuario;
import com.sagsa.api.models.enums.PerfilEnum;
import com.sagsa.api.repositories.PerfilRepository;
import com.sagsa.api.repositories.UsuarioRepository;

// Carga inicial: garante os perfis padrão e um administrador para o primeiro acesso.
// Desative com app.seed.enabled=false.
@Component
@ConditionalOnProperty(name = "app.seed.enabled", havingValue = "true", matchIfMissing = true)
public class DataInitializer implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${app.admin.nome}")
    private String adminNome;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.senha}")
    private String adminSenha;

    @Override
    @Transactional
    public void run(String... args) {
        for (PerfilEnum perfilEnum : PerfilEnum.values()) {
            if (!this.perfilRepository.existsByNome(perfilEnum.name())) {
                this.perfilRepository.save(new Perfil(null, perfilEnum.name(), perfilEnum.getDescricao()));
                LOGGER.info("Perfil criado: {}", perfilEnum.name());
            }
        }

        if (!this.usuarioRepository.existsByPerfil_Nome(PerfilEnum.ADMIN.name())
                && !this.usuarioRepository.existsByEmail(this.adminEmail)) {
            Perfil perfilAdmin = this.perfilRepository.findByNome(PerfilEnum.ADMIN.name()).orElseThrow();
            Usuario admin = new Usuario();
            admin.setNome(this.adminNome);
            admin.setEmail(this.adminEmail);
            admin.setSenhaHash(this.bCryptPasswordEncoder.encode(this.adminSenha));
            admin.setPerfil(perfilAdmin);
            admin.setAtivo(true);
            this.usuarioRepository.save(admin);
            LOGGER.warn("Administrador inicial criado: {} (altere a senha após o primeiro login)", this.adminEmail);
        }
    }
}
