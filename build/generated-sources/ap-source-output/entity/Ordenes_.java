package entity;

import entity.Clientes;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-11-23T15:27:47")
@StaticMetamodel(Ordenes.class)
public class Ordenes_ { 

    public static volatile SingularAttribute<Ordenes, String> marca;
    public static volatile SingularAttribute<Ordenes, String> tipo;
    public static volatile SingularAttribute<Ordenes, Double> precio;
    public static volatile SingularAttribute<Ordenes, String> estado;
    public static volatile SingularAttribute<Ordenes, Clientes> clientesId;
    public static volatile SingularAttribute<Ordenes, Integer> idOrden;

}