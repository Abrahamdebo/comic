package control;

import modelo.DetalleEntrada;
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

@Named("detalleEntradaController")
@SessionScoped
public class DetalleEntradaController implements Serializable {

    @EJB
    private control.DetalleEntradaFacade ejbFacade;
    private List<DetalleEntrada> items = null;
    private List<DetalleEntrada> items_baja = null;
    private DetalleEntrada selected;

    public List<DetalleEntrada> getItems_baja() {
        if (items_baja == null) {
            //items = getFacade().findAll();
            items_baja = getFacade().ListaBaja();
        }
        return items_baja;
    }

    public void setItems_baja(List<DetalleEntrada> items_baja) {
        this.items_baja = items_baja;
    }
    

    public DetalleEntradaController() {
    }

    public DetalleEntrada getSelected() {
        return selected;
    }

    public void setSelected(DetalleEntrada selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private DetalleEntradaFacade getFacade() {
        return ejbFacade;
    }

    public DetalleEntrada prepareCreate() {
        selected = new DetalleEntrada();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        selected.setStatus(1);
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("DetalleEntradaCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("DetalleEntradaUpdated"));
        items = null;
    }

    public void destroy() {
        selected.setStatus(0);
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("DetalleEntradaUpdated"));
        //persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("DetalleEntradaDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
            items_baja = null;    // Invalidate list of items to trigger re-query.
        }
    }
    public void restaurar() {
        //persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("CategoriasDeleted"));
        selected.setStatus(1);
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("DetalleEntradaUpdated"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
            items_baja = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<DetalleEntrada> getItems() {
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

    public DetalleEntrada getDetalleEntrada(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<DetalleEntrada> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<DetalleEntrada> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = DetalleEntrada.class)
    public static class DetalleEntradaControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            DetalleEntradaController controller = (DetalleEntradaController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "detalleEntradaController");
            return controller.getDetalleEntrada(getKey(value));
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
            if (object instanceof DetalleEntrada) {
                DetalleEntrada o = (DetalleEntrada) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), DetalleEntrada.class.getName()});
                return null;
            }
        }

    }

}
