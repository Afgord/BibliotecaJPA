/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibliotecajpa.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import java.io.Serializable;

/**
 * La entidad Usuario representa a un usuario de la Biblioteca.
 * 
 * Para esta asignación se consideró que el usuario cuente con
 * id, nombre y correo electrónico, así como una credencial a su
 * nombre.
 * @author cmartinez
 */
@Entity
public class Usuario implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * El atributo id refiere a la clave única de identificación del 
     * usuario.  Se seleccionó el tipo de generación del id como IDENTITY,
     * similar a AUTO_INCREMENT en MySQL.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Atributo nombre refiere al nombre del usuario
    private String nombre;
    
    // Atributo correo refiere al email del usuario
    private String correo;

    // La clase Usuario tiene una relación 1-1 con la clase Credencial.
    @OneToOne(mappedBy = "usuario")
    private Credencial credencial;

    // Constructor vacío
    public Usuario() {
    }

    // Constructor Usuario con todos los atributos correspondientes
    public Usuario(Long id, String nombre, String correo, Credencial credencial) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.credencial = credencial;
    }

    // Getters y Setters
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Credencial getCredencial() {
        return credencial;
    }

    public void setCredencial(Credencial credencial) {
        this.credencial = credencial;
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
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Usuario{"
                + "id=" + id
                + ", nombre='" + nombre + '\''
                + ", correo='" + correo + '\''
                + '}';
    }

}
