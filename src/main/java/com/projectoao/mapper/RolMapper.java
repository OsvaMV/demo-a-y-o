package com.projectoao.mapper;

import com.projectoao.dto.RolDto;
import com.projectoao.dto.RolRequestDto;
import com.projectoao.entity.Rol;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Mapper MapStruct entre {@link Rol} y sus DTOs.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RolMapper {

	/**
	 * Convierte una entidad {@link Rol} a su DTO de respuesta.
	 *
	 * @param rol entidad a convertir
	 * @return el DTO resultante
	 */
	RolDto toDto(Rol rol);

	/**
	 * Crea una nueva entidad {@link Rol} a partir de un DTO de entrada.
	 *
	 * @param requestDto datos de entrada
	 * @return la entidad resultante
	 */
	Rol toEntity(RolRequestDto requestDto);

	/**
	 * Vuelca los datos de un DTO de entrada sobre una entidad {@link Rol} existente.
	 *
	 * @param requestDto datos de entrada
	 * @param rol entidad a actualizar
	 */
	void actualizarEntidadDesdeDto(RolRequestDto requestDto, @MappingTarget Rol rol);

}
