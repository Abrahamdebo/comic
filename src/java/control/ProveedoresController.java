package control;

import modelo.Proveedores;
import control.util.JsfUtil;
import control.util.JsfUtil.PersistAction;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.event.AjaxBehaviorEvent;
import modelo.Estados;
import modelo.Municipios;
import modelo.Paises;

@Named("proveedoresController")
@SessionScoped
public class ProveedoresController implements Serializable {

    @EJB
    private control.ProveedoresFacade ejbFacade;
    private List<Proveedores> items = null;
    private List<Proveedores> items_baja = null;
    private Proveedores selected;
    @EJB
    private control.PaisesFacade PaisesFacade;
    @EJB
    private control.EstadosFacade EstadosFacade;
    @EJB
    private control.MunicipiosFacade MunicipiosFacade;
   
    private List<Paises> listpais1;
    private List<Estados> listaestado1;
    private List<Municipios> listaminicipios1;
    
    

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
    
    

    public List<Proveedores> getItems_baja() {
        if (items_baja == null) {
            //items = getFacade().findAll();
            items_baja = getFacade().ListaBaja();
        }
        return items_baja;
    }

    public void setItems_baja(List<Proveedores> items_baja) {
        this.items_baja = items_baja;
    }

    
    public ProveedoresController() {
    }

    public Proveedores getSelected() {
        return selected;
    }

    public void setSelected(Proveedores selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ProveedoresFacade getFacade() {
        return ejbFacade;
    }

    public Proveedores prepareCreate() {
        selected = new Proveedores();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        selected.setStatus(1);
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ProveedoresCreated"));
         listaestado1 = EstadosFacade.Buscar(0);
        listaminicipios1 = MunicipiosFacade.Buscar(0);
       if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ProveedoresUpdated"));
        items = null;
    }

    public void destroy() {
        selected.setStatus(0);
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ProveedoresUpdated"));
        //persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ProveedoresDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
            items_baja = null;    // Invalidate list of items to trigger re-query.
        }
    }
    
    public void restaurar() {
        //persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("CategoriasDeleted"));
        selected.setStatus(1);
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ProveedoresUpdated"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
            items_baja = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Proveedores> getItems() {
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

    public Proveedores getProveedores(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Proveedores> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Proveedores> getItemsAvailableSelectOne() {
        return getFacade().ListaActivos();
    }

    @FacesConverter(forClass = Proveedores.class)
    public static class ProveedoresControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ProveedoresController controller = (ProveedoresController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "proveedoresController");
            return controller.getProveedores(getKey(value));
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
            if (object instanceof Proveedores) {
                Proveedores o = (Proveedores) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Proveedores.class.getName()});
                return null;
            }
        }

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

}
