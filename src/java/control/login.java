/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import modelo.Usuarios;

/**
 *
 * @author JuCa
 */
@Named(value = "login")
@SessionScoped
public class login implements Serializable {

    @EJB
    private control.UsuariosFacade ejbFacade;
    private HttpServletRequest http;

    private String username;
    private String password;
    private Usuarios userioautenticado;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Usuarios getUserioautenticado() {
        return userioautenticado;
    }

    public void setUserioautenticado(Usuarios userioautenticado) {
        this.userioautenticado = userioautenticado;
    }

    /**
     * Creates a new instance of login
     */
    public login() {
        http = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }

    public void acceder() throws IOException {

        http = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        userioautenticado = ejbFacade.Buscar(username, password);
        if (userioautenticado != null) {
            //
            http.getSession().setAttribute("username", userioautenticado.getUsername());
            http.getSession().setAttribute("nom_completo", userioautenticado.getNombre() + " " + userioautenticado.getApPat() + " " + userioautenticado.getApMat());
            http.getSession().setAttribute("nivel_usu", userioautenticado.getIdTipoUsu().getNivel());
            http.getSession().setAttribute("usuario", userioautenticado);

            switch (userioautenticado.getIdTipoUsu().getNivel()) {
                case 1:
                    FacesContext.getCurrentInstance().getExternalContext().redirect("cruds.xhtml");
                    break;
                case 2:
                    FacesContext.getCurrentInstance().getExternalContext().redirect("super.xhtml");
                    break;
                case 3:
                    FacesContext.getCurrentInstance().getExternalContext().redirect("bienvenidoCliente.xhtml");

                    break;
                default:
                    FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
                    throw new AssertionError();
            }

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuario o Password incorrecto", null));
        }
    }

    public void cerrarsession() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
            FacesContext.getCurrentInstance().getExternalContext().redirect("/Proyecto/faces/bienvenido.xhtml");

        } catch (Exception e) {
            System.out.println("Error we" + e.toString());
        }
    }

    public void verificarNivel(int nivel) throws IOException {

        http = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        Usuarios usu = (Usuarios) http.getSession().getAttribute("usuario");
        if (usu != null) {
            if (usu.getIdTipoUsu().getNivel() != nivel) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/Proyecto/faces/login.xhtml");

            }

        } else {
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
            FacesContext.getCurrentInstance().getExternalContext().redirect("/Proyecto/faces/login.xhtml");

        }

    }

}
