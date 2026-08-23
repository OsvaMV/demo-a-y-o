exigirSesion();
pintarNav('/almacenamiento.html');

let almacenamientoCache = [];
const puedeEditar = ['ADMINISTRADOR', 'GERENTE'].includes(obtenerRol());

function escaparHtml(texto) {
	const div = document.createElement('div');
	div.textContent = texto;
	return div.innerHTML;
}

async function cargarAlmacenamiento(queryString = '') {
	const respuesta = await apiFetch('/almacenamientos' + queryString);
	if (!respuesta.ok) {
		mostrarMensaje(await mensajeDeError(respuesta), 'error');
		return;
	}
	almacenamientoCache = await respuesta.json();
	pintarTabla(almacenamientoCache);
}

function pintarTabla(registros) {
	document.querySelector('#tabla-almacenamiento tbody').innerHTML = registros.map(registro => `
		<tr>
			<td>${registro.id}</td>
			<td>${escaparHtml(registro.objetoAlmacenado)}</td>
			<td>${formatearFecha(registro.fechaIngreso)}</td>
			<td>${formatearFecha(registro.fechaSalida)}</td>
			<td class="acciones-celda">
				${puedeEditar ? `
					<button class="secundario" onclick="editarRegistro(${registro.id})">Editar</button>
					<button class="peligro" onclick="eliminarRegistro(${registro.id})">Eliminar</button>
				` : '<span class="ayuda">Sin permisos</span>'}
			</td>
		</tr>
	`).join('') || '<tr><td colspan="5">Sin resultados</td></tr>';
}

document.getElementById('almacenamiento-form').addEventListener('submit', async (evento) => {
	evento.preventDefault();

	const cuerpo = {
		objetoAlmacenado: document.getElementById('almacenamiento-objeto').value,
		fechaIngreso: normalizarFechaHora(document.getElementById('almacenamiento-ingreso').value),
		fechaSalida: normalizarFechaHora(document.getElementById('almacenamiento-salida').value)
	};

	const id = document.getElementById('almacenamiento-id').value;
	const respuesta = await apiFetch(id ? `/almacenamientos/${id}` : '/almacenamientos', {
		method: id ? 'PUT' : 'POST',
		body: JSON.stringify(cuerpo)
	});

	if (!respuesta.ok) {
		mostrarMensaje(await mensajeDeError(respuesta), 'error');
		return;
	}

	mostrarMensaje(id ? 'Registro actualizado' : 'Registro creado', 'exito');
	cancelarEdicion();
	cargarAlmacenamiento();
});

function aInputDatetimeLocal(fechaIso) {
	return fechaIso ? fechaIso.substring(0, 16) : '';
}

function editarRegistro(id) {
	const registro = almacenamientoCache.find(r => r.id === id);
	if (!registro) {
		return;
	}
	document.getElementById('form-titulo').textContent = 'Editar registro #' + id;
	document.getElementById('almacenamiento-id').value = registro.id;
	document.getElementById('almacenamiento-objeto').value = registro.objetoAlmacenado;
	document.getElementById('almacenamiento-ingreso').value = aInputDatetimeLocal(registro.fechaIngreso);
	document.getElementById('almacenamiento-salida').value = aInputDatetimeLocal(registro.fechaSalida);
	document.getElementById('btn-cancelar').style.display = 'inline-block';
	window.scrollTo({ top: 0, behavior: 'smooth' });
}

function cancelarEdicion() {
	document.getElementById('form-titulo').textContent = 'Nuevo registro';
	document.getElementById('almacenamiento-form').reset();
	document.getElementById('almacenamiento-id').value = '';
	document.getElementById('btn-cancelar').style.display = 'none';
}

document.getElementById('btn-cancelar').addEventListener('click', cancelarEdicion);

async function eliminarRegistro(id) {
	if (!confirm('¿Eliminar el registro #' + id + '?')) {
		return;
	}
	const respuesta = await apiFetch(`/almacenamientos/${id}`, { method: 'DELETE' });
	if (!respuesta.ok) {
		mostrarMensaje(await mensajeDeError(respuesta), 'error');
		return;
	}
	mostrarMensaje('Registro eliminado', 'exito');
	cargarAlmacenamiento();
}

document.getElementById('buscar-form').addEventListener('submit', async (evento) => {
	evento.preventDefault();

	const parametros = new URLSearchParams();
	const objeto = document.getElementById('buscar-objeto').value.trim();
	const ingresoDesde = normalizarFechaHora(document.getElementById('buscar-ingreso-desde').value);
	const ingresoHasta = normalizarFechaHora(document.getElementById('buscar-ingreso-hasta').value);
	const salidaDesde = normalizarFechaHora(document.getElementById('buscar-salida-desde').value);
	const salidaHasta = normalizarFechaHora(document.getElementById('buscar-salida-hasta').value);

	if (objeto) parametros.set('objetoAlmacenado', objeto);
	if (ingresoDesde) parametros.set('fechaIngresoDesde', ingresoDesde);
	if (ingresoHasta) parametros.set('fechaIngresoHasta', ingresoHasta);
	if (salidaDesde) parametros.set('fechaSalidaDesde', salidaDesde);
	if (salidaHasta) parametros.set('fechaSalidaHasta', salidaHasta);

	const query = parametros.toString();
	cargarAlmacenamiento(query ? '?' + query : '');
});

document.getElementById('btn-ver-todos').addEventListener('click', () => {
	document.getElementById('buscar-form').reset();
	cargarAlmacenamiento();
});

function mostrarMensaje(texto, tipo) {
	const el = document.getElementById('mensaje');
	el.textContent = texto;
	el.className = 'mensaje ' + tipo;
	setTimeout(() => {
		el.textContent = '';
		el.className = 'mensaje';
	}, 4000);
}

cargarAlmacenamiento();
