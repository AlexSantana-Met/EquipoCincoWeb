package entity;

import entity.Citas;
import entity.Login;
import entity.Ordenes;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-11-22T14:13:22")
@StaticMetamodel(Clientes.class)
public class Clientes_ { 

    public static volatile SingularAttribute<Clientes, Date> fechaNac;
    public static volatile SingularAttribute<Clientes, Integer> idCliente;
    public static volatile SingularAttribute<Clientes, Login> loginCorreo;
    public static volatile SingularAttribute<Clientes, String> apMaterno;
    public static volatile SingularAttribute<Clientes, String> ciudad;
    public static volatile SingularAttribute<Clientes, String> direccion;
    public static volatile ListAttribute<Clientes, Ordenes> ordenesList;
    public static volatile SingularAttribute<Clientes, String> nombre;
    public static volatile ListAttribute<Clientes, Citas> citasList;
    public static volatile SingularAttribute<Clientes, String> apPaterno;

}