/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author JuCa
 */
@Entity
@Table(name = "Tipos_pago")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tipospago.findAll", query = "SELECT t FROM Tipospago t")
    , @NamedQuery(name = "Tipospago.findById", query = "SELECT t FROM Tipospago t WHERE t.id = :id")
    , @NamedQuery(name = "Tipospago.findByActivos", query = "SELECT t FROM Tipospago t WHERE t.status = 1")
    , @NamedQuery(name = "Tipospago.findByInactivos", query = "SELECT t FROM Tipospago t WHERE t.status = 0")
    , @NamedQuery(name = "Tipospago.findByNombre", query = "SELECT t FROM Tipospago t WHERE t.nombre = :nombre")
    , @NamedQuery(name = "Tipospago.findByStatus", query = "SELECT t FROM Tipospago t WHERE t.status = :status")})
public class Tipospago implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 80)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "status")
    private int status;
    @OneToMany(mappedBy = "idTipoPago")
    private Collection<Ventas> ventasCollection;

    public Tipospago() {
    }

    public Tipospago(Integer id) {
        this.id = id;
    }

    public Tipospago(Integer id, String nombre, int status) {
        this.id = id;
        this.nombre = nombre;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @XmlTransient
    public Collection<Ventas> getVentasCollection() {
        return ventasCollection;
    }

    public void setVentasCollection(Collection<Ventas> ventasCollection) {
        this.ventasCollection = ventasCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tipospago)) {
            return false;
        }
        Tipospago other = (Tipospago) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre;
    }
    
}
