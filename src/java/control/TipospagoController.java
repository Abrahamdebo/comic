package control;

import modelo.Tipospago;
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

@Named("tipospagoController")
@SessionScoped
public class TipospagoController implements Serializable {

    @EJB
    private control.TipospagoFacade ejbFacade;
    private List<Tipospago> items = null;
    private List<Tipospago> items_baja = null;
    private Tipospago selected;

    public List<Tipospago> getItems_baja() {
         if (items_baja == null) {
            //items = getFacade().findAll();
            items_baja = getFacade().ListaBaja();
        }
        return items_baja;
    }

    public void setItems_baja(List<Tipospago> items_baja) {
        this.items_baja = items_baja;
    }

    
    
    
    public TipospagoController() {
    }

    public Tipospago getSelected() {
        return selected;
    }

    public void setSelected(Tipospago selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private TipospagoFacade getFacade() {
        return ejbFacade;
    }

    public Tipospago prepareCreate() {
        selected = new Tipospago();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        selected.setStatus(1);
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("TipospagoCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("TipospagoUpdated"));
        items = null;
    }

    public void destroy() {
        selected.setStatus(0);
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("TipospagoUpdated"));
        //persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("TipospagoDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
            items_baja = null;    // Invalidate list of items to trigger re-query.
        }
    }
    
    public void restaurar() {
        //persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("CategoriasDeleted"));
        selected.setStatus(1);
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("TipospagoUpdated"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
            items_baja = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Tipospago> getItems() {
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

    public Tipospago getTipospago(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Tipospago> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Tipospago> getItemsAvailableSelectOne() {
        return getFacade().ListaActivos();
    }

    @FacesConverter(forClass = Tipospago.class)
    public static class TipospagoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TipospagoController controller = (TipospagoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "tipospagoController");
            return controller.getTipospago(getKey(value));
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
            if (object instanceof Tipospago) {
                Tipospago o = (Tipospago) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Tipospago.class.getName()});
                return null;
            }
        }

    }

}
