package ar.edu.unlam.tallerweb1.dao;

import java.util.Calendar;
import java.util.List;
import javax.inject.Inject;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ar.edu.unlam.tallerweb1.modelo.Evento;


@Service("EventoDao")
@Transactional
@SuppressWarnings("unchecked")
public class EventoDaoImpl implements EventoDao{
	
	@Inject
	private SessionFactory sessionFactory;	

	@Override
	public List<Evento> findAll() {
		List<Evento> evento =  sessionFactory.getCurrentSession().createCriteria(Evento.class)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.list();
		return evento;
	}

	@Override
	public List<Evento> findByNombre(String nombreDado){
		Calendar c = Calendar.getInstance();
		c.add(Calendar.HOUR, +1);
		List<Evento> evento = sessionFactory.getCurrentSession().createCriteria(Evento.class)
				.add(Restrictions.eq("nombre", nombreDado))
				.add(Restrictions.eq("isTerminado", false))
				.createAlias("partido", "p")
				.add(Restrictions.ge("p.fecha", c.getTime()))
				.add(Restrictions.eq("p.isTerminado", false))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.list();
		return evento;
	}

	@Override
	public Evento findById(Long id) {
		Evento evento = (Evento)sessionFactory.getCurrentSession().createCriteria(Evento.class)
				.add(Restrictions.eq("id", id))
				.uniqueResult();
		return evento;
	}

	@Override
	public void save(Evento evento) {
		evento.generarDescripcionDinamica();
		evento.generarNombresDeCuotasDinamicos();
		sessionFactory.getCurrentSession().save(evento);		
	}	

	@Override
	public void update(Evento evento) {
		sessionFactory.getCurrentSession().update(evento);
	}

	@Override
	public List<Evento> findModificables() {
		return sessionFactory.getCurrentSession().createCriteria(Evento.class)
				.add(Restrictions.eq("isTerminado", false))
				.createAlias("partido", "p")
				.add(Restrictions.eq("p.isTerminado", false))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.list();
	}

	@Override
	public List<Evento> findFinalizables() {
		return sessionFactory.getCurrentSession().createCriteria(Evento.class)
				.add(Restrictions.eq("isTerminado", false))
				.createAlias("partido", "p")
				.add(Restrictions.eq("p.isTerminado", true))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.list();
	}
	
	@Override
	public List<Evento> findFinalizados(){
		return sessionFactory.getCurrentSession().createCriteria(Evento.class)
				.add(Restrictions.eq("isTerminado", true))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.list();
	}
}