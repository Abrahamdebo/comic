package control;

import modelo.Videos;
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

@Named("videosController")
@SessionScoped
public class VideosController implements Serializable {

    @EJB
    private control.VideosFacade ejbFacade;
    private List<Videos> items = null;
    private List<Videos> items_baja = null;
    private Videos selected;
    private UploadedFile video;
    String aux;

    public UploadedFile getVideo() {
        return video;
    }

    public void setVideo(UploadedFile video) {
        this.video = video;
    }

    public String getAux() {
        return aux;
    }

    public void setAux(String aux) {
        this.aux = aux;
    }

    public List<Videos> getItems_baja() {
        if (items_baja == null) {
            // items = getFacade().findAll();
            items_baja = getFacade().ListaBaja();
        }
        return items_baja;
    }

    public void setItems_baja(List<Videos> items_baja) {
        this.items_baja = items_baja;
    }

    public VideosController() {
    }

    public Videos getSelected() {
        items=null;
        return selected;
    }

    public void setSelected(Videos selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private VideosFacade getFacade() {
        return ejbFacade;
    }

    public Videos prepareCreate() {
        selected = new Videos();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        selected.setRuta(aux);
        selected.setStatus(1);
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("VideosCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }
    
     public void Almacenavideo() {
        System.out.println("MIME TIPE:" + getVideo().getContentType());
        System.out.println("Tamaño:" + getVideo().getSize());
        System.out.println("Extension WMV:" + getVideo().getFileName().endsWith(".wmv"));
        System.out.println("Extension ASF:" + getVideo().getFileName().endsWith(".asf"));
        System.out.println("Extension MOV:" + getVideo().getFileName().endsWith(".mov"));
        System.out.println("Extension FLV:" + getVideo().getFileName().endsWith(".flv"));
        System.out.println("Extension RM:" + getVideo().getFileName().endsWith(".rm"));
        System.out.println("Extension RMVB:" + getVideo().getFileName().endsWith(".rmvb"));
        System.out.println("Extension MKV:" + getVideo().getFileName().endsWith(".mkv"));
        System.out.println("Extension MKS:" + getVideo().getFileName().endsWith(".mks"));
        System.out.println("Extension 3GPP:" + getVideo().getFileName().endsWith(".3gpp"));
        System.out.println("Extension AVI:" + getVideo().getFileName().endsWith(".avi"));
        System.out.println("Extension MP4:" + getVideo().getFileName().endsWith(".mp4"));
        

        if (getVideo().getFileName().endsWith(".wmv")
                || getVideo().getFileName().endsWith(".asf")
                || getVideo().getFileName().endsWith(".mov")
                || getVideo().getFileName().endsWith(".flv")
                || getVideo().getFileName().endsWith(".rm")
                || getVideo().getFileName().endsWith(".rmvb")
                || getVideo().getFileName().endsWith(".mkv")
                || getVideo().getFileName().endsWith(".mks")
                || getVideo().getFileName().endsWith(".3gpp")
                || getVideo().getFileName().endsWith(".avi")
                || getVideo().getFileName().endsWith(".mp4")
                
            ) {

            if (SubirArchivo()) {
                create();
                aux = "";

            }
        } else {
            FacesMessage mensaje = new FacesMessage("El archivo no es una Video");
            FacesContext.getCurrentInstance().addMessage(null, mensaje);
            selected = null;
        }

    }

    public boolean SubirArchivo() {
        try {
            aux = "resources/videosproductos";
            File archivo = new File(JsfUtil.getPath() + aux);
            if (!archivo.exists()) {
                archivo.mkdirs();
            }
            copiar_archivo(getVideo().getFileName(), getVideo().getInputstream());
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
            Logger.getLogger(VideosController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("VideosUpdated"));
        items = null;
    }

    public void destroy() {
        selected.setStatus(0);
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("VideosUpdated"));
        //persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("VideosDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
            items_baja = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void restaurar() {
        //persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("CategoriasDeleted"));
        selected.setStatus(1);
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("VideosUpdated"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
            items_baja = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Videos> getItems() {
        if (items == null) {
            // items = getFacade().findAll();
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

    public Videos getVideos(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Videos> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Videos> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Videos.class)
    public static class VideosControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            VideosController controller = (VideosController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "videosController");
            return controller.getVideos(getKey(value));
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
            if (object instanceof Videos) {
                Videos o = (Videos) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Videos.class.getName()});
                return null;
            }
        }

    }

}
