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
import modelo.Fotos;

/**
 *
 * @author JuCa
 */
@Stateless
public class FotosFacade extends AbstractFacade<Fotos> {

    @PersistenceContext(unitName = "ProyectoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FotosFacade() {
        super(Fotos.class);
    }
    
      public List<Fotos> ListaActivos() {
        Query consulta = em.createNamedQuery("Fotos.findByActivos",Fotos.class);
        List<Fotos> lista = consulta.getResultList();
        if (!lista.isEmpty()) {
            return lista;
        }
        return null;
    }

    public List<Fotos> ListaBaja() {
        Query consulta = em.createNamedQuery("Fotos.findByInactivos",Fotos.class);
        List<Fotos> lista = consulta.getResultList();
        if (!lista.isEmpty()) {
            return lista;
        }
        return null;
    }
}
