/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import ManagedBean.ClienteBean;
import ManagedBean.exceptions.RollbackFailureException;
import entity.Clientes;
import entity.Login;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.UserTransaction;

/**
 *
 * @author Alejandro
 */
public class LoginFacade {
    
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("OpticaAndes-Persist");
    UserTransaction utx = null;
    LoginJpaController loginJPA = new LoginJpaController(utx, emf);

    public Login nuevoLogin(String correo, String passw) {
        Login login = new Login(correo, passw);
        try {
            loginJPA.create(login);
            return login;
        } catch (RollbackFailureException ex) {
            System.out.println("Error en rollback" + ex);
            return null;
        } catch (Exception ex) {
            System.out.println("Error en inserción" + ex);
            return null;
        }
    }

}
