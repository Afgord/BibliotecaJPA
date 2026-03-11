/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.bibliotecajpa;

import com.mycompany.bibliotecajpa.models.Autor;
import com.mycompany.bibliotecajpa.models.Categoria;
import com.mycompany.bibliotecajpa.models.Credencial;
import com.mycompany.bibliotecajpa.models.Libro;
import com.mycompany.bibliotecajpa.models.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author cmartinez
 */
public class BibliotecaJPA {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("BibliotecaPU");
        EntityManager em = emf.createEntityManager();

        try {
            // Flujo principal
            flujoPrincipal(em);

            // Flujo alterno 1: consultar datos guardados
            consultarLibros(em);

            // Flujo alterno 2: agregar una categoría nueva a un libro existente
            agregarCategoriaALibro(em);

            // Flujo alterno 3: ejemplo de manejo de error con rollback
            flujoAlternoConError(em);

        } finally {
            em.close();
            emf.close();
        }

    }

    private static void flujoPrincipal(EntityManager em) {
        System.out.println("\n=== FLUJO PRINCIPAL: guardar entidades relacionadas ===");

        try {
            em.getTransaction().begin();

            // Usuario y credencial
            Usuario usuario = new Usuario();
            usuario.setNombre("Christian Martinez");
            usuario.setCorreo("christian@gmail.com");

            Credencial credencial = new Credencial();
            credencial.setUsername("cmartinez");
            credencial.setPassword("12345");
            credencial.setUsuario(usuario);
            usuario.setCredencial(credencial);

            // Autor
            Autor autor = new Autor();
            autor.setNombre("Gabriel Garcia Marquez");
            autor.setNacionalidad("Colombiana");

            // Categorías
            Categoria categoria1 = new Categoria();
            categoria1.setNombre("Novela");

            Categoria categoria2 = new Categoria();
            categoria2.setNombre("Realismo Magico");

            // Libro
            Libro libro = new Libro();
            libro.setTitulo("Cien anios de soledad");
            libro.setAnio_publicacion(1967);
            libro.setAutor(autor);

            Set<Categoria> categoriasLibro = new HashSet<>();
            categoriasLibro.add(categoria1);
            categoriasLibro.add(categoria2);
            libro.setCategorias(categoriasLibro);

            // Relación inversa Categoria -> Libro
            Set<Libro> librosCat1 = new HashSet<>();
            librosCat1.add(libro);
            categoria1.setLibros(librosCat1);

            Set<Libro> librosCat2 = new HashSet<>();
            librosCat2.add(libro);
            categoria2.setLibros(librosCat2);

            // Relación inversa Autor -> Libro
            Set<Libro> librosAutor = new HashSet<>();
            librosAutor.add(libro);
            autor.setLibros(librosAutor);

            // Persistencias
            em.persist(credencial); // guarda también usuario por cascade
            em.persist(autor);
            em.persist(categoria1);
            em.persist(categoria2);
            em.persist(libro);

            em.getTransaction().commit();
            System.out.println("Flujo principal completado correctamente.");

        } catch (Exception e) {
            System.out.println("Error en flujo principal: " + e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }

    private static void consultarLibros(EntityManager em) {
        System.out.println("\n=== FLUJO ALTERNO 1: consultar libros guardados ===");

        try {
            var libros = em.createQuery("SELECT l FROM Libro l", Libro.class).getResultList();

            if (libros.isEmpty()) {
                System.out.println("No hay libros registrados.");
            } else {
                for (Libro libro : libros) {
                    System.out.println("Libro: " + libro.getTitulo());
                    System.out.println("Año: " + libro.getAnio_publicacion());

                    if (libro.getAutor() != null) {
                        System.out.println("Autor: " + libro.getAutor().getNombre());
                    }

                    System.out.println("Categorias:");
                    if (libro.getCategorias() != null && !libro.getCategorias().isEmpty()) {
                        for (Categoria categoria : libro.getCategorias()) {
                            System.out.println("- " + categoria.getNombre());
                        }
                    } else {
                        System.out.println("- Sin categorias");
                    }

                    System.out.println("---------------------------");
                }
            }

        } catch (Exception e) {
            System.out.println("Error al consultar libros: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void agregarCategoriaALibro(EntityManager em) {
        System.out.println("\n=== FLUJO ALTERNO 2: agregar nueva categoria a un libro existente ===");

        try {
            em.getTransaction().begin();

            var libros = em.createQuery("SELECT l FROM Libro l", Libro.class).getResultList();

            if (libros.isEmpty()) {
                System.out.println("No hay libros para actualizar.");
                em.getTransaction().rollback();
                return;
            }

            Libro libro = libros.get(0);

            Categoria nuevaCategoria = new Categoria();
            nuevaCategoria.setNombre("Clasico");

            em.persist(nuevaCategoria);

            if (libro.getCategorias() == null) {
                libro.setCategorias(new HashSet<>());
            }
            libro.getCategorias().add(nuevaCategoria);

            if (nuevaCategoria.getLibros() == null) {
                nuevaCategoria.setLibros(new HashSet<>());
            }
            nuevaCategoria.getLibros().add(libro);

            em.merge(libro);

            em.getTransaction().commit();
            System.out.println("Categoria agregada correctamente al libro: " + libro.getTitulo());

        } catch (Exception e) {
            System.out.println("Error al agregar categoria: " + e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }

    private static void flujoAlternoConError(EntityManager em) {
        System.out.println("\n=== FLUJO ALTERNO 3: ejemplo de rollback por error ===");

        try {
            em.getTransaction().begin();

            Categoria categoriaError = new Categoria();
            categoriaError.setNombre(null); // error intencional si tu BD no acepta null

            em.persist(categoriaError);

            em.getTransaction().commit();
            System.out.println("Operacion terminada.");

        } catch (Exception e) {
            System.out.println("Se detecto un error y se hara rollback.");
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Rollback ejecutado correctamente.");
        }
    }
}
