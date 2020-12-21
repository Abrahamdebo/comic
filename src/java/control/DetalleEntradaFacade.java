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
import modelo.DetalleEntrada;

/**
 *
 * @author JuCa
 */
@Stateless
public class DetalleEntradaFacade extends AbstractFacade<DetalleEntrada> {

    @PersistenceContext(unitName = "ProyectoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DetalleEntradaFacade() {
        super(DetalleEntrada.class);
    }
    
        public List<DetalleEntrada> ListaActivos() {
        Query consulta = em.createNamedQuery("DetalleEntrada.findByActivos", DetalleEntrada.class);
        List<DetalleEntrada> lista = consulta.getResultList();
        if (!lista.isEmpty()) {
            return lista;
        }
        return null;
    }

    public List<DetalleEntrada> ListaBaja() {
        Query consulta = em.createNamedQuery("DetalleEntrada.findByInactivos", DetalleEntrada.class);
        List<DetalleEntrada> lista = consulta.getResultList();
        if (!lista.isEmpty()) {
            return lista;
        }
        return null;
    }
    
}
