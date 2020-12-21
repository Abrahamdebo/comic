package control;

import modelo.Entradas;
import control.util.JsfUtil;
import control.util.JsfUtil.PersistAction;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.event.AjaxBehaviorEvent;

@Named("entradasController")
@SessionScoped
public class EntradasController implements Serializable {

    @EJB
    private control.EntradasFacade ejbFacade;
    private List<Entradas> items = null;
    private List<Entradas> items_baja = null;
   
    

    
    
    
    public List<Entradas> getItems_baja() {
        if (items_baja == null) {
            //items = getFacade().findAll();
            items_baja = getFacade().ListaBaja();
        }
        return items_baja;
    }

    public void setItems_baja(List<Entradas> items_baja) {
        this.items_baja = items_baja;
    }
    private Entradas selected;

    public EntradasController() {
    }

    public Entradas getSelected() {
        return selected;
    }

    public void setSelected(Entradas selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private EntradasFacade getFacade() {
        return ejbFacade;
    }

    public Entradas prepareCreate() {
        selected = new Entradas();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        selected.setIva(0.16);
        selected.setStatus(1);
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("EntradasCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("EntradasUpdated"));
        items = null;
    }

    public void destroy() {
        selected.setStatus(0);
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("EntradasUpdated"));
        //persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("EntradasDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
            items_baja = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void restaurar() {
        //persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("CategoriasDeleted"));
        selected.setStatus(1);
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("EntradasUpdated"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
            items_baja = null;    // Invalidate list of items to trigger re-query.
        }
    }
    
    public List<Entradas> getItems() {
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

    public Entradas getEntradas(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Entradas> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Entradas> getItemsAvailableSelectOne() {
        return getFacade().ListaActivos();
    }

    

    @FacesConverter(forClass = Entradas.class)
    public static class EntradasControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            EntradasController controller = (EntradasController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "entradasController");
            return controller.getEntradas(getKey(value));
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
            if (object instanceof Entradas) {
                Entradas o = (Entradas) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Entradas.class.getName()});
                return null;
            }
        }

    }

    public void calcular(final  AjaxBehaviorEvent event){
        selected.setIva(0.16);
      double r = (selected.getSubtotal() * selected.getIva());
     selected.setTotal( selected.getSubtotal() +r ) ;
    }

}
