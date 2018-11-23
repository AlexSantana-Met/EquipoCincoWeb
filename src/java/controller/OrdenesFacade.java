/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import ManagedBean.OrdenBean;
import entity.Ordenes;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.UserTransaction;

/**
 *
 * @author Alejandro
 */
public class OrdenesFacade {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("OpticaAndes-Persist");
    UserTransaction utx = null;
    OrdenesJpaController ordenesJPA = new OrdenesJpaController(utx, emf);

    public List<OrdenBean> obtenerOrdenes(int idCliente) {
        List<OrdenBean> listaRet = new ArrayList<>();
        List<Ordenes> listaOrdenes = ordenesJPA.findByIdCliente(idCliente);
        if (listaOrdenes == null) {
            return null;
        } else {
            for (Ordenes orden : listaOrdenes) {
                listaRet.add(new OrdenBean(0, idCliente, orden.getTipo(), orden.getMarca(), orden.getPrecio(), orden.getEstado()));
            }
            return listaRet;
        }
    }

}
