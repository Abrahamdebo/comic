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
import modelo.Entradas;

/**
 *
 * @author JuCa
 */
@Stateless
public class EntradasFacade extends AbstractFacade<Entradas> {

    @PersistenceContext(unitName = "ProyectoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EntradasFacade() {
        super(Entradas.class);
    }
    
      public List<Entradas> ListaActivos() {
       Query consulta = em.createNamedQuery("Entradas.findByActivos",Entradas.class);
       List<Entradas>  lista = consulta.getResultList();
        if (!lista.isEmpty()) {
            return lista;
        }
        return null;
    }

    public List<Entradas> ListaBaja() {
        Query consulta = em.createNamedQuery("Entradas.findByInactivos",Entradas.class);
        List<Entradas> lista = consulta.getResultList();
        if (!lista.isEmpty()) {
            return lista;
        }
        return null;
    }
    
}
