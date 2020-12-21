/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
@Table(name = "Tipos_usuario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tiposusuario.findAll", query = "SELECT t FROM Tiposusuario t")
    , @NamedQuery(name = "Tiposusuario.findById", query = "SELECT t FROM Tiposusuario t WHERE t.id = :id")
    , @NamedQuery(name = "Tiposusuario.findByActivos", query = "SELECT t FROM Tiposusuario t WHERE t.status = 1")
    , @NamedQuery(name = "Tiposusuario.findByInactivos", query = "SELECT t FROM Tiposusuario t WHERE t.status = 0")
    , @NamedQuery(name = "Tiposusuario.findByNombre", query = "SELECT t FROM Tiposusuario t WHERE t.nombre = :nombre")
    , @NamedQuery(name = "Tiposusuario.findByNivel", query = "SELECT t FROM Tiposusuario t WHERE t.nivel = :nivel")
    , @NamedQuery(name = "Tiposusuario.findByStatus", query = "SELECT t FROM Tiposusuario t WHERE t.status = :status")})
public class Tiposusuario implements Serializable {

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
    @Column(name = "nivel")
    private int nivel;
    @Basic(optional = false)
    @NotNull
    @Column(name = "status")
    private int status;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTipoUsu")
    private Collection<Usuarios> usuariosCollection;

    public Tiposusuario() {
    }

    public Tiposusuario(Integer id) {
        this.id = id;
    }

    public Tiposusuario(Integer id, String nombre, int nivel, int status) {
        this.id = id;
        this.nombre = nombre;
        this.nivel = nivel;
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

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @XmlTransient
    public Collection<Usuarios> getUsuariosCollection() {
        return usuariosCollection;
    }

    public void setUsuariosCollection(Collection<Usuarios> usuariosCollection) {
        this.usuariosCollection = usuariosCollection;
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
        if (!(object instanceof Tiposusuario)) {
            return false;
        }
        Tiposusuario other = (Tiposusuario) object;
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
