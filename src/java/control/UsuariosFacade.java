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
import modelo.Usuarios;

import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author JuCa
 */
@Stateless
public class UsuariosFacade extends AbstractFacade<Usuarios> {
    
    @PersistenceContext(unitName = "ProyectoPU")
    private EntityManager em;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public UsuariosFacade() {
        super(Usuarios.class);
    }
    
    public List<Usuarios> ListaActivos() {
        Query consulta = em.createNamedQuery("Usuarios.findByActivos", Usuarios.class);
        List<Usuarios> lista = consulta.getResultList();
        if (!lista.isEmpty()) {
            return lista;
        }
        return null;
    }
    
    public List<Usuarios> ListaBaja() {
        Query consulta = em.createNamedQuery("Usuarios.findByInactivos", Usuarios.class);
        List<Usuarios> lista = consulta.getResultList();
        if (!lista.isEmpty()) {
            return lista;
        }
        return null;
    }
    public List<Usuarios> BuscarCliente() {
        Query consulta = em.createNamedQuery("Usuarios.BuscarCliente", Usuarios.class);
        List<Usuarios> lista = consulta.getResultList();
        if (!lista.isEmpty()) {
            return lista;
        }
        return null;
    }
    public List<Usuarios> BuscarUsuario() {
        Query consulta = em.createNamedQuery("Usuarios.BuscarUsuario", Usuarios.class);
        List<Usuarios> lista = consulta.getResultList();
        if (!lista.isEmpty()) {
            return lista;
        }
        return null;
    }
    
    public Usuarios Buscar(String username, String password) {
        String pass_encrip = DigestUtils.shaHex(password);
        Query consul = em.createNamedQuery("Usuarios.Buscar", Usuarios.class).setParameter("usu", username).setParameter("pass", pass_encrip);
        
        List<Usuarios> lista = consul.getResultList();
        
        if (!lista.isEmpty()) {
            return lista.get(0);
        }
        return null;
    }
    public Usuarios Buscarusername(String username) {
        Query consul = em.createNamedQuery("Usuarios.Buscarusername", Usuarios.class)
        .setParameter("usu", username);
        List<Usuarios> lista = consul.getResultList();
        if (!lista.isEmpty()) {
            return lista.get(0);
        }
        return null;
    }
    
    
    
}
