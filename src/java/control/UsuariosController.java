package control;

import modelo.Usuarios;
import control.util.JsfUtil;
import control.util.JsfUtil.PersistAction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import modelo.Estados;
import modelo.Municipios;
import modelo.Paises;
import modelo.Tiposusuario;
import org.apache.commons.codec.digest.DigestUtils;

@Named("usuariosController")
@SessionScoped
public class UsuariosController implements Serializable {

    @EJB
    private control.UsuariosFacade ejbFacade;
    private List<Usuarios> items = null;
    private List<Usuarios> items_baja = null;
    private Usuarios selected;

    @EJB
    private control.PaisesFacade PaisesFacade;
    @EJB
    private control.EstadosFacade EstadosFacade;
    @EJB
    private control.MunicipiosFacade MunicipiosFacade;

    private List<SelectItem> listpais;
    private List<SelectItem> listaestado;
    private List<SelectItem> listaminicipios;

    private List<Paises> listpais1;
    private List<Estados> listaestado1;
    private List<Municipios> listaminicipios1;
    private List<Usuarios> cliente;
    private List<Usuarios> usuario;
    private String mensaje;
    private String mensaje2;

    public List<Usuarios> getUsuario() {
        return ejbFacade.BuscarUsuario();
    }

    public void setUsuario(List<Usuarios> usuario) {
        this.usuario = usuario;
    }

    
    
    
    public List<Usuarios> getCliente() {
    
        return ejbFacade.BuscarCliente();
    
    }

    public void setCliente(List<Usuarios> cliente) {
        this.cliente = cliente;
    }
    
    
    

    public String getMensaje2() {
        return mensaje2;
    }

    public void setMensaje2(String mensaje2) {
        this.mensaje2 = mensaje2;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public List<SelectItem> getListpais() {
        return listpais;
    }

    public void setListpais(List<SelectItem> listpais) {
        this.listpais = listpais;
    }

    public List<SelectItem> getListaestado() {
        return listaestado;
    }

    public void setListaestado(List<SelectItem> listaestado) {
        this.listaestado = listaestado;
    }

    public List<SelectItem> getListaminicipios() {
        return listaminicipios;
    }

    public void setListaminicipios(List<SelectItem> listaminicipios) {
        this.listaminicipios = listaminicipios;
    }

    public List<Usuarios> getItems_baja() {
        if (items_baja == null) {
            //items = getFacade().findAll();
            items_baja = getFacade().ListaBaja();
        }
        return items_baja;
    }

    public void setItems_baja(List<Usuarios> items_baja) {
        this.items_baja = items_baja;
    }

    public UsuariosController() {

    }

    public Usuarios getSelected() {
        if (selected == null) {
            selected = new Usuarios();
        }
        return selected;
    }

    public void setSelected(Usuarios selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private UsuariosFacade getFacade() {
        return ejbFacade;
    }

    public Usuarios prepareCreate() {
        selected = new Usuarios();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        String pass = selected.getPassword();
        String pass_encrip = DigestUtils.shaHex(pass);

        selected.setPassword(pass_encrip);
        selected.setStatus(1);
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("UsuariosCreated"));
        listaestado1 = EstadosFacade.Buscar(0);
        listaminicipios1 = MunicipiosFacade.Buscar(0);
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        String pass = selected.getPassword();
        String pass_encrip = DigestUtils.shaHex(pass);

        selected.setPassword(pass_encrip);
        
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("UsuariosUpdated"));
        items = null;
    }

    public void destroy() {
        selected.setStatus(0);
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("UsuariosUpdated"));
        //persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("UsuariosDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
            items_baja = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void restaurar() {
        //persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("CategoriasDeleted"));
        selected.setStatus(1);
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("UsuariosUpdated"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
            items_baja = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Usuarios> getItems() {
        if (items == null) {
            //items = getFacade().findAll();
            items = getFacade().ListaActivos();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public Usuarios getUsuarios(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Usuarios> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Usuarios> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

   

    @FacesConverter(forClass = Usuarios.class)
    public static class UsuariosControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UsuariosController controller = (UsuariosController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "usuariosController");
            return controller.getUsuarios(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Usuarios) {
                Usuarios o = (Usuarios) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Usuarios.class.getName()});
                return null;
            }
        }

    }

    public List<Paises> getListpais1() {
        return listpais1;
    }

    public void setListpais1(List<Paises> listpais1) {
        this.listpais1 = listpais1;
    }

    public List<Estados> getListaestado1() {
        return listaestado1;
    }

    public void setListaestado1(List<Estados> listaestado1) {
        this.listaestado1 = listaestado1;
    }

    public List<Municipios> getListaminicipios1() {
        return listaminicipios1;
    }

    public void setListaminicipios1(List<Municipios> listaminicipios1) {
        this.listaminicipios1 = listaminicipios1;
    }

    public void buscaestados(final AjaxBehaviorEvent event) {
        listaestado1 = EstadosFacade.Buscar(selected.getIdPais().getId());
        listaminicipios1 = MunicipiosFacade.Buscar(0);

    }

    public void buscamunicipios(final AjaxBehaviorEvent event) {
        listaminicipios1 = MunicipiosFacade.Buscar(selected.getIdEstado().getId());
    }

    @PostConstruct
    public void initialize() {
        listpais1 = PaisesFacade.ListaActivos();

    }

    public void registartajax(final AjaxBehaviorEvent event) {
        String pass = selected.getPassword();
        String pass_encrip = DigestUtils.shaHex(pass);

        selected.setPassword(pass_encrip);
        selected.setStatus(1);
        selected.setIdTipoUsu(new Tiposusuario(3));
        
        
         persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("UsuariosCreated"));
       
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
            items_baja = null;    // Invalidate list of items to trigger re-query.
        }
//            ejbFacade.create(selected);
            
            mensaje="Usuario Registrado";
            selected = new Usuarios();
            mensaje2="";
        
    }

    public void username(final AjaxBehaviorEvent event) {

        Usuarios aux = ejbFacade.Buscarusername(selected.getUsername());
        if (aux == null) {
            mensaje2 = "Nombre de usuario no registrado";
        } else {
            mensaje2 = "Usuario ya Registrado";
        }
    }

}
