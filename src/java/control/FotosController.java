package control;

import com.sun.tools.ws.wsdl.document.Output;
import modelo.Fotos;
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

@Named("fotosController")
@SessionScoped
public class FotosController implements Serializable {

    @EJB
    private control.FotosFacade ejbFacade;
    private List<Fotos> items = null;
    private List<Fotos> items_baja = null;
    private Fotos selected;
    private UploadedFile foto;
    private String aux;

    public String getAux() {
        return aux;
    }

    public void setAux(String aux) {
        this.aux = aux;
    }

    public List<Fotos> getItems_baja() {
        if (items_baja == null) {
            items_baja = getFacade().ListaBaja();
        }
        return items_baja;
    }

    public void setItems_baja(List<Fotos> items_baja) {
        this.items_baja = items_baja;
    }

    public FotosController() {
    }

    public Fotos getSelected() {
        return selected;
    }

    public void setSelected(Fotos selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private FotosFacade getFacade() {
        return ejbFacade;
    }

    public Fotos prepareCreate() {
        selected = new Fotos();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        selected.setStatus(1);
        selected.setRuta(aux);
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("FotosCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void Almacenafoto() {
        System.out.println("MIME TIPE:" + getFoto().getContentType());
        System.out.println("Tamaño:" + getFoto().getSize());
        System.out.println("Extension PNG:" + getFoto().getFileName().endsWith(".png"));
        System.out.println("Extension JPG:" + getFoto().getFileName().endsWith(".jpg"));
        System.out.println("Extension JPEG:" + getFoto().getFileName().endsWith(".jpeg"));
        System.out.println("Extension GIF:" + getFoto().getFileName().endsWith(".gif"));

        if (getFoto().getFileName().endsWith(".png")
                || getFoto().getFileName().endsWith(".jpg")
                || getFoto().getFileName().endsWith(".jpeg")
                || getFoto().getFileName().endsWith(".gif")) {

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
            aux = "resources/fotosproductos";
            File archivo = new File(JsfUtil.getPath() + aux);
            if (!archivo.exists()) {
                archivo.mkdirs();
            }
            copiar_archivo(getFoto().getFileName(), getFoto().getInputstream());
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
            Logger.getLogger(FotosController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("FotosUpdated"));
        items = null;
    }

    public void destroy() {
        selected.setStatus(0);
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("FotosUpdated"));
        //persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("FotosDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
            items_baja = null;
        }
    }

    public void restaurar() {
        //persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("CategoriasDeleted"));
        selected.setStatus(1);
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("FotosUpdated"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
            items_baja = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Fotos> getItems() {
        if (items == null) {
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

    public Fotos getFotos(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Fotos> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Fotos> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public UploadedFile getFoto() {
        return foto;
    }

    public void setFoto(UploadedFile foto) {
        this.foto = foto;
    }

    public control.FotosFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(control.FotosFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    @FacesConverter(forClass = Fotos.class)
    public static class FotosControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            FotosController controller = (FotosController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "fotosController");
            return controller.getFotos(getKey(value));
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
            if (object instanceof Fotos) {
                Fotos o = (Fotos) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Fotos.class.getName()});
                return null;
            }
        }

    }

}
