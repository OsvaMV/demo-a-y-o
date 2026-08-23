package com.projectoao.config;

import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

/**
 * Levanta y detiene la consola web propia de H2 junto con el ciclo de vida de Spring,
 * ya que este proyecto es 100% WebFlux (sin Servlet container) y no puede usar la
 * consola H2 estandar basada en Servlet.
 */
@Component
public class H2ConsoleServer implements SmartLifecycle {

	@Value("${h2.console.port:8082}")
	private int port;

	@Value("${h2.console.enabled:true}")
	private boolean enabled;

	private Server webServer;
	private boolean running = false;

	/** {@inheritDoc} */
	@Override
	public void start() {
		if (!enabled) {
			return;
		}
		try {
			webServer = Server.createWebServer("-webPort", String.valueOf(port)).start();
			running = true;
		} catch (SQLException e) {
			throw new IllegalStateException("No se pudo iniciar la consola web de H2", e);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void stop() {
		if (webServer != null) {
			webServer.stop();
		}
		running = false;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isRunning() {
		return running;
	}

}
