/*
 Proyecto Java EE, DAGSS-2013
 */

package es.uvigo.esei.dagss.dominio.daos;

import es.uvigo.esei.dagss.dominio.entidades.Medicamento;
import es.uvigo.esei.dagss.dominio.entidades.Prescripcion;
import es.uvigo.esei.dagss.dominio.entidades.Tratamiento;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
@LocalBean
public class TratamientoDAO extends GenericoDAO<Tratamiento>{
    
    // Completar aqui
    public List<Tratamiento> buscarTratamientoPorPacienteMedico(Long medico_id, Long paciente_id)
    {
        Query q;
        q = em.createQuery("SELECT t FROM Tratamiento AS t"
                + " WHERE t.medico.id = :medico_id AND"
                + " t.paciente.id = :paciente_id");
        q.setParameter("medico_id", medico_id);
        q.setParameter("paciente_id", paciente_id);
        return q.getResultList();
    }        
}
