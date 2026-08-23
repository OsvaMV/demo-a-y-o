exigirSesion();
pintarNav('/usuarios.html');

let usuariosCache = [];
const esAdministrador = obtenerRol() === 'ADMINISTRADOR';

// El selector de rol se arma dinamicamente: si el usuario logueado puede
// listar roles (ADMINISTRADOR), se muestra un <select>. Si no (GERENTE, que
// puede crear usuarios pero no tiene acceso a /roles), se cae a un campo
// numerico para escribir el id del rol a mano.
async function armarSelectorRol() {
	const contenedor = document.getElementById('rol-selector-contenedor');
	try {
		const respuesta = await apiFetch('/roles');
		if (!respuesta.ok) {
			throw new Error('sin acceso a roles');
		}
		const roles = await respuesta.json();
		contenedor.innerHTML = `
			<label>Rol
				<select id="usuario-rolId" required>
					<option value="">-- selecciona --</option>
					${roles.map(r => `<option value="${r.id}">${escaparHtml(r.nombre)}</option>`).join('')}
				</select>
			</label>`;
	} catch (error) {
		contenedor.innerHTML = `
			<label>ID del rol
				<input type="number" id="usuario-rolId" min="1" required>
			</label>`;
		document.getElementById('ayuda-password').insertAdjacentHTML('beforebegin',
			'<p class="ayuda">No tienes acceso al listado de roles: pide el ID exacto al administrador.</p>');
	}
}

function escaparHtml(texto) {
	const div = document.createElement('div');
	div.textContent = texto;
	return div.innerHTML;
}

async function cargarUsuarios() {
	const respuesta = await apiFetch('/usuarios');
	if (!respuesta.ok) {
		mostrarMensaje(await mensajeDeError(respuesta), 'error');
		return;
	}
	usuariosCache = await respuesta.json();
	pintarTabla(usuariosCache);
}

function pintarTabla(usuarios) {
	document.querySelector('#tabla-usuarios tbody').innerHTML = usuarios.map(usuario => `
		<tr>
			<td>${usuario.id}</td>
			<td>${escaparHtml(usuario.username)}</td>
			<td>${escaparHtml(usuario.email)}</td>
			<td>${escaparHtml([usuario.nombre, usuario.apellido].filter(Boolean).join(' '))}</td>
			<td>${escaparHtml(usuario.rol ? usuario.rol.nombre : '')}</td>
			<td>${usuario.activo ? 'Si' : 'No'}</td>
			<td class="acciones-celda">
				${esAdministrador ? `
					<button class="secundario" onclick="editarUsuario(${usuario.id})">Editar</button>
					<button class="peligro" onclick="eliminarUsuario(${usuario.id})">Eliminar</button>
				` : '<span class="ayuda">Sin permisos</span>'}
			</td>
		</tr>
	`).join('') || '<tr><td colspan="7">Sin resultados</td></tr>';
}

document.getElementById('usuario-form').addEventListener('submit', async (evento) => {
	evento.preventDefault();

	const cuerpo = {
		username: document.getElementById('usuario-username').value,
		email: document.getElementById('usuario-email').value,
		password: document.getElementById('usuario-password').value,
		nombre: document.getElementById('usuario-nombre').value || null,
		apellido: document.getElementById('usuario-apellido').value || null,
		activo: document.getElementById('usuario-activo').checked,
		rolId: Number(document.getElementById('usuario-rolId').value)
	};

	const id = document.getElementById('usuario-id').value;
	const respuesta = await apiFetch(id ? `/usuarios/${id}` : '/usuarios', {
		method: id ? 'PUT' : 'POST',
		body: JSON.stringify(cuerpo)
	});

	if (!respuesta.ok) {
		mostrarMensaje(await mensajeDeError(respuesta), 'error');
		return;
	}

	mostrarMensaje(id ? 'Usuario actualizado' : 'Usuario creado', 'exito');
	cancelarEdicion();
	cargarUsuarios();
});

function editarUsuario(id) {
	const usuario = usuariosCache.find(u => u.id === id);
	if (!usuario) {
		return;
	}
	document.getElementById('form-titulo').textContent = 'Editar usuario #' + id;
	document.getElementById('usuario-id').value = usuario.id;
	document.getElementById('usuario-username').value = usuario.username;
	document.getElementById('usuario-email').value = usuario.email;
	document.getElementById('usuario-nombre').value = usuario.nombre || '';
	document.getElementById('usuario-apellido').value = usuario.apellido || '';
	document.getElementById('usuario-password').value = '';
	document.getElementById('usuario-activo').checked = usuario.activo;
	const rolSelector = document.getElementById('usuario-rolId');
	if (rolSelector && usuario.rol) {
		rolSelector.value = usuario.rol.id;
	}
	document.getElementById('btn-cancelar').style.display = 'inline-block';
	window.scrollTo({ top: 0, behavior: 'smooth' });
}

function cancelarEdicion() {
	document.getElementById('form-titulo').textContent = 'Nuevo usuario';
	document.getElementById('usuario-form').reset();
	document.getElementById('usuario-id').value = '';
	document.getElementById('usuario-activo').checked = true;
	document.getElementById('btn-cancelar').style.display = 'none';
}

document.getElementById('btn-cancelar').addEventListener('click', cancelarEdicion);

async function eliminarUsuario(id) {
	if (!confirm('¿Eliminar el usuario #' + id + '?')) {
		return;
	}
	const respuesta = await apiFetch(`/usuarios/${id}`, { method: 'DELETE' });
	if (!respuesta.ok) {
		mostrarMensaje(await mensajeDeError(respuesta), 'error');
		return;
	}
	mostrarMensaje('Usuario eliminado', 'exito');
	cargarUsuarios();
}

document.getElementById('buscar-form').addEventListener('submit', async (evento) => {
	evento.preventDefault();
	const username = document.getElementById('buscar-username').value.trim();
	if (!username) {
		cargarUsuarios();
		return;
	}

	const respuesta = await apiFetch(`/usuarios/username/${encodeURIComponent(username)}`);
	if (!respuesta.ok) {
		mostrarMensaje(await mensajeDeError(respuesta), 'error');
		return;
	}

	usuariosCache = await respuesta.json();
	pintarTabla(usuariosCache);
});

document.getElementById('btn-ver-todos').addEventListener('click', () => {
	document.getElementById('buscar-username').value = '';
	cargarUsuarios();
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

armarSelectorRol();
cargarUsuarios();
