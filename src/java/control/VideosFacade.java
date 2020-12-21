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
import modelo.Videos;

/**
 *
 * @author JuCa
 */
@Stateless
public class VideosFacade extends AbstractFacade<Videos> {

    @PersistenceContext(unitName = "ProyectoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public VideosFacade() {
        super(Videos.class);
    }
    
      public List<Videos> ListaActivos() {
        Query consulta = em.createNamedQuery("Videos.findByActivos",Videos.class);
        List<Videos> lista = consulta.getResultList();
        if (!lista.isEmpty()) {
            return lista;
        }
        return null;
    }

    public List<Videos> ListaBaja() {
        Query consulta = em.createNamedQuery("Videos.findByInactivos",Videos.class);
        List<Videos> lista = consulta.getResultList();
        if (!lista.isEmpty()) {
            return lista;
        }
        return null;
    }
    
}
