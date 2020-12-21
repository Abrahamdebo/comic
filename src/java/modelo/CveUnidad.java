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
@Table(name = "Cve_Unidad")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CveUnidad.findAll", query = "SELECT c FROM CveUnidad c")
    , @NamedQuery(name = "CveUnidad.findById", query = "SELECT c FROM CveUnidad c WHERE c.id = :id")
    , @NamedQuery(name = "CveUnidad.findByActivos", query = "SELECT c FROM CveUnidad c WHERE c.status = 1")
    , @NamedQuery(name = "CveUnidad.findByInactivos", query = "SELECT c FROM CveUnidad c WHERE c.status = 0")
    , @NamedQuery(name = "CveUnidad.findByNombre", query = "SELECT c FROM CveUnidad c WHERE c.nombre = :nombre")
    , @NamedQuery(name = "CveUnidad.findByStatus", query = "SELECT c FROM CveUnidad c WHERE c.status = :status")})
public class CveUnidad implements Serializable {

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
    @OneToMany(mappedBy = "idUnidad")
    private Collection<Productos> productosCollection;

    public CveUnidad() {
    }

    public CveUnidad(Integer id) {
        this.id = id;
    }

    public CveUnidad(Integer id, String nombre, int status) {
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
    public Collection<Productos> getProductosCollection() {
        return productosCollection;
    }

    public void setProductosCollection(Collection<Productos> productosCollection) {
        this.productosCollection = productosCollection;
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
        if (!(object instanceof CveUnidad)) {
            return false;
        }
        CveUnidad other = (CveUnidad) object;
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
