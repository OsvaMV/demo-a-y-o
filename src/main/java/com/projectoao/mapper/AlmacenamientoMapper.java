package com.projectoao.mapper;

import com.projectoao.dto.AlmacenamientoDto;
import com.projectoao.dto.AlmacenamientoRequestDto;
import com.projectoao.entity.Almacenamiento;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Mapper MapStruct entre {@link Almacenamiento} y sus DTOs.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AlmacenamientoMapper {

	/**
	 * Convierte una entidad {@link Almacenamiento} a su DTO de respuesta.
	 *
	 * @param almacenamiento entidad a convertir
	 * @return el DTO resultante
	 */
	AlmacenamientoDto toDto(Almacenamiento almacenamiento);

	/**
	 * Crea una nueva entidad {@link Almacenamiento} a partir de un DTO de entrada.
	 *
	 * @param requestDto datos de entrada
	 * @return la entidad resultante
	 */
	Almacenamiento toEntity(AlmacenamientoRequestDto requestDto);

	/**
	 * Vuelca los datos de un DTO de entrada sobre una entidad {@link Almacenamiento} existente.
	 *
	 * @param requestDto datos de entrada
	 * @param almacenamiento entidad a actualizar
	 */
	void actualizarEntidadDesdeDto(AlmacenamientoRequestDto requestDto, @MappingTarget Almacenamiento almacenamiento);

}
