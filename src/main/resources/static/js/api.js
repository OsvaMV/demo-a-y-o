// Helpers compartidos: token JWT, llamadas a la API y sesion.
// El token se guarda en localStorage por simplicidad (proyecto demo).

function obtenerToken() {
	return localStorage.getItem('token');
}

function obtenerRol() {
	return localStorage.getItem('rol');
}

function obtenerUsername() {
	return localStorage.getItem('username');
}

function cerrarSesion() {
	localStorage.clear();
	window.location.href = '/login.html';
}

function exigirSesion() {
	if (!obtenerToken()) {
		window.location.href = '/login.html';
	}
}

// fetch con el header Authorization ya puesto. Si el token vencio o no es
// valido, el backend responde 401 y aqui se cierra la sesion automaticamente.
async function apiFetch(path, opciones = {}) {
	const headers = Object.assign({ 'Content-Type': 'application/json' }, opciones.headers || {});
	const token = obtenerToken();
	if (token) {
		headers.Authorization = 'Bearer ' + token;
	}

	const respuesta = await fetch(path, Object.assign({}, opciones, { headers }));

	if (respuesta.status === 401) {
		cerrarSesion();
		throw new Error('Sesion expirada, vuelve a iniciar sesion');
	}

	return respuesta;
}

// Extrae el mensaje de error de una respuesta ProblemDetail del backend.
async function mensajeDeError(respuesta) {
	try {
		const datos = await respuesta.json();
		if (Array.isArray(datos.errors) && datos.errors.length > 0) {
			return datos.errors.map(e => e.defaultMessage || e).join(', ');
		}
		return datos.detail || datos.title || ('Error ' + respuesta.status);
	} catch (e) {
		return 'Error ' + respuesta.status;
	}
}

function formatearFecha(fechaIso) {
	if (!fechaIso) {
		return '';
	}
	return new Date(fechaIso).toLocaleString();
}

// Los <input type="datetime-local"> no incluyen segundos; el backend los
// acepta igual, pero por si acaso normalizamos a "YYYY-MM-DDTHH:mm:ss".
function normalizarFechaHora(valor) {
	if (!valor) {
		return null;
	}
	return valor.length === 16 ? valor + ':00' : valor;
}
