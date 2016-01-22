/*
 Proyecto Java EE, DAGSS-2014
 */

package es.uvigo.esei.dagss.dominio.daos;

import es.uvigo.esei.dagss.dominio.entidades.Cita;
import java.util.Date;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;


@Stateless
@LocalBean
public class CitaDAO  extends GenericoDAO<Cita>{    

    // Completar aqui
    public List<Cita> buscarCitasPorMedicoFecha(Long medico_id, Date fecha)
    {
        Query q;
        q = em.createQuery("SELECT c FROM Cita AS c"
                + " WHERE c.medico.id = :medico_id AND"
                + " c.fecha = :fecha");
        q.setParameter("medico_id", medico_id);
        q.setParameter("fecha", fecha);
        return q.getResultList();
    }
}
