exigirSesion();
pintarNav('/roles.html');

let rolesCache = [];
const puedeEditar = obtenerRol() === 'ADMINISTRADOR';

// GERENTE solo puede consultar roles (para poder asignarlos al crear usuarios);
// oculta el formulario de alta y las acciones de editar/eliminar.
if (!puedeEditar) {
	document.getElementById('tarjeta-form-rol').style.display = 'none';
}

async function cargarRoles() {
	const respuesta = await apiFetch('/roles');
	if (!respuesta.ok) {
		mostrarMensaje(await mensajeDeError(respuesta), 'error');
		return;
	}
	rolesCache = await respuesta.json();
	pintarTabla(rolesCache);
}

function pintarTabla(roles) {
	document.querySelector('#tabla-roles tbody').innerHTML = roles.map(rol => `
		<tr>
			<td>${rol.id}</td>
			<td>${escaparHtml(rol.nombre)}</td>
			<td>${escaparHtml(rol.descripcion || '')}</td>
			<td>${rol.activo ? 'Si' : 'No'}</td>
			<td>${formatearFecha(rol.fechaCreacion)}</td>
			<td class="acciones-celda">
				${puedeEditar ? `
					<button class="secundario" onclick="editarRol(${rol.id})">Editar</button>
					<button class="peligro" onclick="eliminarRol(${rol.id})">Eliminar</button>
				` : '<span class="ayuda">Solo lectura</span>'}
			</td>
		</tr>
	`).join('') || '<tr><td colspan="6">Sin resultados</td></tr>';
}

function escaparHtml(texto) {
	const div = document.createElement('div');
	div.textContent = texto;
	return div.innerHTML;
}

if (puedeEditar) {
	document.getElementById('rol-form').addEventListener('submit', async (evento) => {
		evento.preventDefault();

		const cuerpo = {
			nombre: document.getElementById('rol-nombre').value,
			descripcion: document.getElementById('rol-descripcion').value || null,
			activo: document.getElementById('rol-activo').checked
		};

		const id = document.getElementById('rol-id').value;
		const respuesta = await apiFetch(id ? `/roles/${id}` : '/roles', {
			method: id ? 'PUT' : 'POST',
			body: JSON.stringify(cuerpo)
		});

		if (!respuesta.ok) {
			mostrarMensaje(await mensajeDeError(respuesta), 'error');
			return;
		}

		mostrarMensaje(id ? 'Rol actualizado' : 'Rol creado', 'exito');
		cancelarEdicion();
		cargarRoles();
	});

	document.getElementById('btn-cancelar').addEventListener('click', cancelarEdicion);
}

function editarRol(id) {
	const rol = rolesCache.find(r => r.id === id);
	if (!rol) {
		return;
	}
	document.getElementById('form-titulo').textContent = 'Editar rol #' + id;
	document.getElementById('rol-id').value = rol.id;
	document.getElementById('rol-nombre').value = rol.nombre;
	document.getElementById('rol-descripcion').value = rol.descripcion || '';
	document.getElementById('rol-activo').checked = rol.activo;
	document.getElementById('btn-cancelar').style.display = 'inline-block';
	window.scrollTo({ top: 0, behavior: 'smooth' });
}

function cancelarEdicion() {
	document.getElementById('form-titulo').textContent = 'Nuevo rol';
	document.getElementById('rol-form').reset();
	document.getElementById('rol-id').value = '';
	document.getElementById('rol-activo').checked = true;
	document.getElementById('btn-cancelar').style.display = 'none';
}

async function eliminarRol(id) {
	if (!confirm('¿Eliminar el rol #' + id + '?')) {
		return;
	}
	const respuesta = await apiFetch(`/roles/${id}`, { method: 'DELETE' });
	if (!respuesta.ok) {
		mostrarMensaje(await mensajeDeError(respuesta), 'error');
		return;
	}
	mostrarMensaje('Rol eliminado', 'exito');
	cargarRoles();
}

document.getElementById('buscar-form').addEventListener('submit', async (evento) => {
	evento.preventDefault();
	const nombre = document.getElementById('buscar-nombre').value.trim();
	if (!nombre) {
		cargarRoles();
		return;
	}

	const respuesta = await apiFetch(`/roles/nombre/${encodeURIComponent(nombre)}`);
	if (!respuesta.ok) {
		mostrarMensaje(await mensajeDeError(respuesta), 'error');
		return;
	}

	rolesCache = await respuesta.json();
	pintarTabla(rolesCache);
});

document.getElementById('btn-ver-todos').addEventListener('click', () => {
	document.getElementById('buscar-nombre').value = '';
	cargarRoles();
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

cargarRoles();
