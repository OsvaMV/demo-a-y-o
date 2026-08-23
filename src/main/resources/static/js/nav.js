// Pinta el menu superior, mostrando solo las secciones permitidas para el rol
// logueado (misma matriz de permisos que ya aplica el backend; esto es solo
// para no ofrecer enlaces que de todas formas el backend rechazaria).
function pintarNav(paginaActiva) {
	const enlaces = [
		{ href: '/dashboard.html', texto: 'Inicio', roles: ['ADMINISTRADOR', 'GERENTE', 'TECNICO'] },
		{ href: '/roles.html', texto: 'Roles', roles: ['ADMINISTRADOR', 'GERENTE'] },
		{ href: '/usuarios.html', texto: 'Usuarios', roles: ['ADMINISTRADOR', 'GERENTE'] },
		{ href: '/almacenamiento.html', texto: 'Almacenamiento', roles: ['ADMINISTRADOR', 'GERENTE', 'TECNICO'] }
	];

	const rol = obtenerRol();
	const nav = document.getElementById('nav');
	if (nav) {
		nav.innerHTML = enlaces
			.filter(enlace => enlace.roles.includes(rol))
			.map(enlace => `<a href="${enlace.href}" class="${enlace.href === paginaActiva ? 'activo' : ''}">${enlace.texto}</a>`)
			.join('');
	}

	const userInfo = document.getElementById('user-info');
	if (userInfo) {
		userInfo.textContent = obtenerUsername() + ' (' + rol + ')';
	}
}
