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
import modelo.Editoriales;

/**
 *
 * @author JuCa
 */
@Stateless
public class EditorialesFacade extends AbstractFacade<Editoriales> {

    @PersistenceContext(unitName = "ProyectoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EditorialesFacade() {
        super(Editoriales.class);
    }
    
      public List<Editoriales> ListaActivos() {
        Query consulta = em.createNamedQuery("Editoriales.findByActivos",Editoriales.class);
        List<Editoriales> lista = consulta.getResultList();
        if (!lista.isEmpty()) {
            return lista;
        }
        return null;
    }

    public List<Editoriales> ListaBaja() {
        Query consulta = em.createNamedQuery("Editoriales.findByInactivos",Editoriales.class);
        List<Editoriales> lista = consulta.getResultList();
        if (!lista.isEmpty()) {
            return lista;
        }
        return null;
    }
    
}
