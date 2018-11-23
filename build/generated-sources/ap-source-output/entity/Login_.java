package entity;

import entity.Clientes;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-11-23T15:27:47")
@StaticMetamodel(Login.class)
public class Login_ { 

    public static volatile ListAttribute<Login, Clientes> clientesList;
    public static volatile SingularAttribute<Login, String> correo;
    public static volatile SingularAttribute<Login, String> passw;

}