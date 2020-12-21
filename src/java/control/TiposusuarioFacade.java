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
import modelo.Tiposusuario;

/**
 *
 * @author JuCa
 */
@Stateless
public class TiposusuarioFacade extends AbstractFacade<Tiposusuario> {

    @PersistenceContext(unitName = "ProyectoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TiposusuarioFacade() {
        super(Tiposusuario.class);
    }
 
      public List<Tiposusuario> ListaActivos() {
          Query consulta = em.createNamedQuery("Tiposusuario.findByActivos",Tiposusuario.class);
        List<Tiposusuario> lista = consulta.getResultList();
        if (!lista.isEmpty()) {
            return lista;
        }
        return null;
    }

    public List<Tiposusuario> ListaBaja() {
        Query consulta = em.createNamedQuery("Tiposusuario.findByInactivos",Tiposusuario.class);
        List<Tiposusuario> lista = consulta.getResultList();
        if (!lista.isEmpty()) {
            return lista;
        }
        return null;
    }
    
}
