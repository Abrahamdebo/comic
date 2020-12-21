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
import modelo.Ventas;

/**
 *
 * @author JuCa
 */
@Stateless
public class VentasFacade extends AbstractFacade<Ventas> {

    @PersistenceContext(unitName = "ProyectoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public VentasFacade() {
        super(Ventas.class);
    }
    
      public List<Ventas> ListaActivos() {
        Query consulta = em.createNamedQuery("Ventas.findByActivos",Ventas.class);
        List<Ventas> lista = consulta.getResultList();
        if (!lista.isEmpty()) {
            return lista;
        }
        return null;
    }

    public List<Ventas> ListaBaja() {
        Query consulta = em.createNamedQuery("Ventas.findByInactivos",Ventas.class);
        List<Ventas> lista = consulta.getResultList();
        if (!lista.isEmpty()) {
            return lista;
        }
        return null;
    }
    
}
