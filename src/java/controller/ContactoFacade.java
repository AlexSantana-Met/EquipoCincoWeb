/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import ManagedBean.ContactoBean;
import ManagedBean.exceptions.RollbackFailureException;
import entity.Contacto;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.UserTransaction;

/**
 *
 * @author Alejandro
 */
public class ContactoFacade {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("OpticaAndes-Persist");
    UserTransaction utx = null;

    ContactoJpaController contactoJPA = new ContactoJpaController(utx, emf);

    public String nuevoContacto(ContactoBean contacto) {
        int id = contactoJPA.getMaxId() + 1;
        Contacto c = new Contacto(id, contacto.getCorreo(), contacto.getNombre(), contacto.getMensaje());
        try {
            contactoJPA.create(c);
            return "EXITO";
        } catch (RollbackFailureException ex) {
            return "ERROR";
        } catch (Exception ex) {
            return "ERROR";
        }
    }

}
