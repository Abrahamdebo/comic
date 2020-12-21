package control;

import modelo.Editoriales;
import control.util.JsfUtil;
import control.util.JsfUtil.PersistAction;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.primefaces.model.UploadedFile;

@Named("editorialesController")
@SessionScoped
public class EditorialesController implements Serializable {

    @EJB
    private control.EditorialesFacade ejbFacade;
    private List<Editoriales> items = null;
    private List<Editoriales> items_baja = null;
    private Editoriales selected;
    private UploadedFile ruta;
    private String aux;

    public UploadedFile getRuta() {
        return ruta;
    }

    public void setRuta(UploadedFile ruta) {
        this.ruta = ruta;
    }

    public String getAux() {
        return aux;
    }

    public void setAux(String aux) {
        this.aux = aux;
    }
    
    
    public List<Editoriales> getItems_baja() {
        if (items_baja == null) {
            items_baja = getFacade().ListaBaja();
            //items = getFacade().findAll();
        }
        return items_baja;
    }

    public void setItems_baja(List<Editoriales> items_baja) {
        this.items_baja = items_baja;
    }
    

    public EditorialesController() {
    }

    public Editoriales getSelected() {
        return selected;
    }

    public void setSelected(Editoriales selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private EditorialesFacade getFacade() {
        return ejbFacade;
    }

    public Editoriales prepareCreate() {
        selected = new Editoriales();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        selected.setStatus(1);
        selected.setFoto(aux);
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("EditorialesCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }
    
    
    public void Almacenafoto() {
        System.out.println("MIME TIPE:" + getRuta().getContentType());
        System.out.println("Tamaño:" + getRuta().getSize());
        System.out.println("Extension PNG:" + getRuta().getFileName().endsWith(".png"));
        System.out.println("Extension JPG:" + getRuta().getFileName().endsWith(".jpg"));
        System.out.println("Extension JPEG:" + getRuta().getFileName().endsWith(".jpeg"));
        System.out.println("Extension GIF:" + getRuta().getFileName().endsWith(".gif"));

        if (getRuta().getFileName().endsWith(".png")
                || getRuta().getFileName().endsWith(".jpg")
                || getRuta().getFileName().endsWith(".jpeg")
                || getRuta().getFileName().endsWith(".gif")) {

            //
            if (SubirArchivo()) {
                create();
                aux = "";

            }
        } else {
            FacesMessage mensaje = new FacesMessage("El archivo no es una imagen");
            FacesContext.getCurrentInstance().addMessage(null, mensaje);
            selected = null;
        }

    }

    public boolean SubirArchivo() {
        try {
            aux = "resources/fotoseditoriales";
            File archivo = new File(JsfUtil.getPath() + aux);
            if (!archivo.exists()) {
                archivo.mkdirs();
            }
            copiar_archivo(getRuta().getFileName(), getRuta().getInputstream());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void copiar_archivo(String nom, InputStream in) throws FileNotFoundException {
        aux = aux + "/" + nom;
        OutputStream out = new FileOutputStream(new File(JsfUtil.getPath() + aux));
        int read = 0;
        byte[] bytes = new byte[1024];

        try {
            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

            aux = aux.substring(9);
            in.close();
            out.flush();
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(EditorialesController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public void update() {
        
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("EditorialesUpdated"));
        items = null;
    }

    public void destroy() {
        selected.setStatus(0);
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("EditorialesUpdated"));
        //persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("EditorialesDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
            items_baja = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void restaurar() {
        //persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("CategoriasDeleted"));
        selected.setStatus(1);
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("EditorialesUpdated"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
            items_baja = null;    // Invalidate list of items to trigger re-query.
        }
    }
    
    public List<Editoriales> getItems() {
        if (items == null) {
            items = getFacade().ListaActivos();
            //items = getFacade().findAll();
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

    public Editoriales getEditoriales(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Editoriales> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Editoriales> getItemsAvailableSelectOne() {
        return getFacade().ListaActivos();
    }

    @FacesConverter(forClass = Editoriales.class)
    public static class EditorialesControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            EditorialesController controller = (EditorialesController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "editorialesController");
            return controller.getEditoriales(getKey(value));
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
            if (object instanceof Editoriales) {
                Editoriales o = (Editoriales) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Editoriales.class.getName()});
                return null;
            }
        }

    }

}
