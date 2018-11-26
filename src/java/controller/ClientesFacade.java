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
import java.util.Date;
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

    public ClienteBean getCliente(String correo) {
        Clientes cl = clientesJPA.findClienteByCorreo(correo);
        String fechaNac = "";
        if (cl.getFechaNac() != null) {
            fechaNac = String.valueOf(cl.getFechaNac().getDate()) + "/" + String.valueOf(cl.getFechaNac().getMonth() + 1) + "/" + String.valueOf(cl.getFechaNac().getYear() + 1900);
        }
        return new ClienteBean(cl.getIdCliente(), cl.getNombre(), cl.getApPaterno(), cl.getApMaterno(), fechaNac, cl.getDireccion(), cl.getCiudad());
    }

    public ClienteBean editarClienteFacade(ClienteBean cliente) {
        String correo = cliente.getCorreo();
        Clientes cl = clientesJPA.findClienteByCorreo(correo);
        cl.setCiudad(cliente.getCiudad());
        cl.setDireccion(cliente.getDireccion());
        Date fechaN = new Date();
        fechaN.setMonth(Integer.parseInt(cliente.getFechaNac().split("/")[1]) - 1);
        fechaN.setDate(Integer.parseInt(cliente.getFechaNac().split("/")[0]));
        fechaN.setYear(Integer.parseInt(cliente.getFechaNac().split("/")[2]) - 1900);
        cl.setFechaNac(fechaN);
        try {
            clientesJPA.edit(cl);
            String fechaNac = String.valueOf(cl.getFechaNac().getDate()) + "/" + String.valueOf(cl.getFechaNac().getMonth() + 1) + "/" + String.valueOf(cl.getFechaNac().getYear() + 1900);
            return new ClienteBean(cliente.getIdCliente(), cl.getNombre(), cl.getApPaterno(), cl.getApMaterno(), fechaNac, cl.getDireccion(), cl.getCiudad());
        } catch (RollbackFailureException ex) {
            System.out.println("Error en rollback, cliente " + ex);
            return null;
        } catch (Exception ex) {
            return null;//dd/mm/yyyy
        }

    }
}
