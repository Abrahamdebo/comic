/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import modelo.CveUnidad;

/**
 *
 * @author JuCa
 */
@Stateless
public class CveUnidadFacade extends AbstractFacade<CveUnidad> {

    @PersistenceContext(unitName = "ProyectoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CveUnidadFacade() {
        super(CveUnidad.class);
    }
    
    public List<CveUnidad> ListaActivos() {
        Query consulta = em.createNamedQuery("CveUnidad.findByActivos", CveUnidad.class);
        List<CveUnidad> lista = consulta.getResultList();
        if (!lista.isEmpty()) {
            return lista;
        }
        return null;
    }

    public List<CveUnidad> ListaBaja() {
        Query consulta = em.createNamedQuery("CveUnidad.findByInactivos", CveUnidad.class);
        List<CveUnidad> lista = consulta.getResultList();
        if (!lista.isEmpty()) {
            return lista;
        }
        return null;
    }
    
}
