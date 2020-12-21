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
import modelo.Estados;

/**
 *
 * @author JuCa
 */
@Stateless
public class EstadosFacade extends AbstractFacade<Estados> {

    @PersistenceContext(unitName = "ProyectoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EstadosFacade() {
        super(Estados.class);
    }
    
      public List<Estados> ListaActivos() {
          Query consulta = em.createNamedQuery("Estados.findByActivos",Estados.class);
        List<Estados> lista = consulta.getResultList();
        if (!lista.isEmpty()) {
            return lista;
        }
        return null;
    }

    public List<Estados> ListaBaja() {
        Query consulta = em.createNamedQuery("Estados.findByInactivos",Estados.class);
        List<Estados> lista = consulta.getResultList();
        if (!lista.isEmpty()) {
            return lista;
        }
        return null;
    }
    
   public List<Estados> Buscar(int id_pais){
       Query consul = em.createNamedQuery("Estados.findBuscar",Estados.class).setParameter("idpais",id_pais);
       List<Estados> list = consul.getResultList();
       return  list;
   }
  
}
