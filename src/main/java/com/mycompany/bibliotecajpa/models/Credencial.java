/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibliotecajpa.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.io.Serializable;

/**
 * La entidad Credencial representa a la credencial expedida para cada
 * usuario de la Biblioteca.
 * 
 * Para esta asignación se consideró que la credencial cuente con
 * id, username y password, así como un objeto de la entidad Usuario
 * para la relación que existe entre ambas.
 * @author cmartinez
 */
@Entity
public class Credencial implements Serializable {

    private static final long serialVersionUID = 1L;
    
    /**
     * Atributo id que representa el identificador único de cada credencial
     * expedida. Se genera automáticamente mediante IDENTITY.
     */ 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Atributo username
    private String username;
    
    // Atributo password
    private String password;
    
    //Llave foránea de tabla Usuario
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="id_usuario", referencedColumnName = "id", unique=true)
    private Usuario usuario;

    public Credencial() {
    }

    public Credencial(Long id, String username, String password, Usuario usuario) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.usuario = usuario;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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
        if (!(object instanceof Credencial)) {
            return false;
        }
        Credencial other = (Credencial) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Credencial{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }
    
}
