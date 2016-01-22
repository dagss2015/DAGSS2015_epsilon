/*
 Proyecto Java EE, DAGSS-2014
 */
package es.uvigo.esei.dagss.dominio.daos;

import es.uvigo.esei.dagss.dominio.entidades.Medicamento;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
@LocalBean
public class MedicamentoDAO extends GenericoDAO<Medicamento> {

    // Completar aqui
    
    public List<Medicamento> buscarMedicamento(String medicamento){
        Query q;
        q = em.createQuery("SELECT m FROM Medicamento AS m"
                + " WHERE m.nombre = :medicamento");
        q.setParameter("medicamento", medicamento);
        return q.getResultList();
    }
}
