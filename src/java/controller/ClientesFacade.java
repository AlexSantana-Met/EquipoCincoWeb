/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import ManagedBean.ClienteBean;
import ManagedBean.LoginBean;
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
public class ClientesFacade {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("OpticaAndes-Persist");
//            EntityManagerFactory emf = Persistence.createEntityManagerFactory("OpticaAndes-PrograWebPU");
    UserTransaction utx = null;
    ClientesJpaController clientesJPA = new ClientesJpaController(utx, emf);

    public String nuevoCliente(ClienteBean cliente) {
        int id = clientesJPA.getMaxId() + 1;
        LoginBean nuevoLoginBean = new LoginBean(cliente.getCorreo(), cliente.getAuxP());
        Login nuevoLogin = nuevoLoginBean.registroLogin();
        Clientes c = null;
        if (nuevoLogin != null) {
            try {
                c = new Clientes(id, cliente.getNombre(),
                        cliente.getApPaterno(), cliente.getApMaterno(), nuevoLogin);
                clientesJPA.create(c);
                return "EXITO";
            } catch (RollbackFailureException ex) {
                System.out.println("Error en rollback, cliente " + ex);
                return "ERROR";
            } catch (Exception ex) {
                System.out.println("Error en general, cliente " + ex);
                return "ERROR";
            }
        } else {
            System.out.println("Fuckin' Error cliente");
            return "CORREO";
        }
    }
    
    public ClienteBean getCliente(String correo){
        Clientes cl = clientesJPA.findClienteByCorreo(correo);
        return new ClienteBean(cl.getIdCliente(), cl.getNombre(), cl.getApPaterno(), cl.getApMaterno(), cl.getFechaNac(), cl.getDireccion(), cl.getCiudad());
    }
}
