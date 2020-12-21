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
import modelo.Tipospago;

/**
 *
 * @author JuCa
 */
@Stateless
public class TipospagoFacade extends AbstractFacade<Tipospago> {

    @PersistenceContext(unitName = "ProyectoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TipospagoFacade() {
        super(Tipospago.class);
    }
    
      public List<Tipospago> ListaActivos() {
          Query consulta = em.createNamedQuery("Tipospago.findByActivos",Tipospago.class);
        List<Tipospago> lista = consulta.getResultList();
        if (!lista.isEmpty()) {
            return lista;
        }
        return null;
    }

    public List<Tipospago> ListaBaja() {
        Query consulta = em.createNamedQuery("Tipospago.findByInactivos",Tipospago.class);
        List<Tipospago> lista = consulta.getResultList();
        if (!lista.isEmpty()) {
            return lista;
        }
        return null;
    }
    
}
