package ar.edu.unlam.tallerweb1.controladores;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.ModelAndView;

import static org.mockito.Mockito.*;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import ar.edu.unlam.tallerweb1.modelo.Equipo;
import ar.edu.unlam.tallerweb1.modelo.Usuario;
import ar.edu.unlam.tallerweb1.servicios.ServicioEquipo;
import ar.edu.unlam.tallerweb1.servicios.ServicioEvento;
import ar.edu.unlam.tallerweb1.servicios.ServicioLogin;
import static org.assertj.core.api.Assertions.assertThat;

public class ControladorLoginTest {
	
	@Mock
	private ServicioLogin servicioLogin;
	
	@Mock
	private ServicioEvento servicioEvento;
	
	@Mock
	private ServicioEquipo servicioEquipo;	

	@Mock
	private Usuario usuario;
	
	@Mock
	private HttpServletRequest request;
	
	@Mock
	private HttpSession session;

	@InjectMocks
	private ControladorLogin controladorLogin;
	
	@Before
	public void inyeccionDeMocksInicializada() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testQueVerificaQueElLoginSeaValido() {
		when(request.getSession()).thenReturn(session);
		when(servicioLogin.consultarUsuario(any(Usuario.class))).thenReturn(usuario);
		when(servicioEquipo.listarTodosLosEquipos()).thenReturn(new LinkedList<Equipo>());
		when(usuario.getId()).thenReturn(91L);
		when(usuario.getEmail()).thenReturn("admin@betfutbol.com");
		when(usuario.getPassword()).thenReturn("admin");
		when(usuario.getRol()).thenReturn("ADMIN");
		
		ModelAndView modelo = controladorLogin.irALogin(usuario, request);

		assertThat(modelo.getViewName()).isEqualTo("ABM-Equipo");
		assertThat(modelo.getModel()).isNotEmpty();
		verify(session, times(1)).setAttribute("AdminId", 91L);
		
	}
	
	@Test
	public void testQueVerificaQueElLoginSeaInvalido() {
		when(request.getSession()).thenReturn(session);
		when(servicioLogin.consultarUsuario(any(Usuario.class))).thenReturn(null);
		when(servicioEquipo.listarTodosLosEquipos()).thenReturn(new LinkedList<Equipo>());
		
		ModelAndView modelo = controladorLogin.irALogin(usuario, request);
		
		assertThat(modelo.getViewName()).isEqualTo("index");
		assertThat(modelo.getModel().get("aviso")).isEqualTo("Usuario inexistente");
		verify(session , never()).setAttribute("ROL", "ADMIN");
	}

}