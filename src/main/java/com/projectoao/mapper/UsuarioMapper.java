package com.projectoao.mapper;

import com.projectoao.dto.UsuarioDto;
import com.projectoao.dto.UsuarioRequestDto;
import com.projectoao.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Mapper MapStruct entre {@link Usuario} y sus DTOs. Delega en {@link RolMapper} para el rol anidado.
 */
@Mapper(componentModel = "spring", uses = RolMapper.class,
		nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UsuarioMapper {

	/**
	 * Convierte una entidad {@link Usuario} a su DTO de respuesta, incluyendo el rol anidado.
	 *
	 * @param usuario entidad a convertir
	 * @return el DTO resultante
	 */
	UsuarioDto toDto(Usuario usuario);

	/**
	 * Crea una nueva entidad {@link Usuario} a partir de un DTO de entrada. El rol se ignora
	 * aqui porque debe resolverse en el servicio a partir del {@code rolId}.
	 *
	 * @param requestDto datos de entrada
	 * @return la entidad resultante, sin el rol asignado
	 */
	@Mapping(target = "rol", ignore = true)
	Usuario toEntity(UsuarioRequestDto requestDto);

	/**
	 * Vuelca los datos de un DTO de entrada sobre una entidad {@link Usuario} existente. El rol
	 * se ignora aqui porque debe resolverse en el servicio a partir del {@code rolId}.
	 *
	 * @param requestDto datos de entrada
	 * @param usuario entidad a actualizar
	 */
	@Mapping(target = "rol", ignore = true)
	void actualizarEntidadDesdeDto(UsuarioRequestDto requestDto, @MappingTarget Usuario usuario);

}
