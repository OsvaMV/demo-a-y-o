package com.projectoao.config;

import com.projectoao.entity.Rol;
import com.projectoao.entity.Usuario;
import com.projectoao.repository.RolRepository;
import com.projectoao.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Siembra el rol ADMINISTRADOR y un usuario administrador al arrancar la aplicacion, para que
 * siempre exista una cuenta capaz de crear el resto de roles y usuarios (GERENTE, TECNICO, etc.)
 * a traves de la API.
 */
@Component
@RequiredArgsConstructor
public class DataSeeder implements ApplicationRunner {

	private static final String ROL_ADMINISTRADOR = "ADMINISTRADOR";
	private static final String USERNAME_ADMIN = "admin";
	private static final String PASSWORD_ADMIN = "Admin123!";

	private final RolRepository rolRepository;
	private final UsuarioRepository usuarioRepository;
	private final PasswordEncoder passwordEncoder;

	/** {@inheritDoc} */
	@Override
	public void run(ApplicationArguments args) {
		Rol administrador = rolRepository.findByNombre(ROL_ADMINISTRADOR)
				.orElseGet(() -> {
					Rol rol = new Rol();
					rol.setNombre(ROL_ADMINISTRADOR);
					rol.setDescripcion("Rol con acceso total a todos los endpoints del sistema");
					return rolRepository.save(rol);
				});

		if (usuarioRepository.findByUsername(USERNAME_ADMIN).isEmpty()) {
			Usuario admin = new Usuario();
			admin.setUsername(USERNAME_ADMIN);
			admin.setEmail("admin@proyectoao.com");
			admin.setPassword(passwordEncoder.encode(PASSWORD_ADMIN));
			admin.setNombre("Administrador");
			admin.setApellido("Sistema");
			admin.setRol(administrador);
			usuarioRepository.save(admin);
		}
	}

}
