import edu.pk.poc.orm.core.Configuration;
import edu.pk.poc.orm.core.Session;
import edu.pk.poc.orm.core.helper.SessionFactoy;
import edu.pk.poc.orm.entity.DepartmentEntity;

public class Test {
    public static void main(String[] args) throws Exception {
        SessionFactoy factoy = new Configuration().configure().addAnnotatedClass(DepartmentEntity.class).buildSessionFactory();
        Session session = factoy.openSession();

        //Saving Entity
        DepartmentEntity entity = new DepartmentEntity();
        entity.setDeptId(50);
        entity.setDeptName("VISA");
        entity.setLocation("Bangalore");
        session.persist(entity);
        System.out.println("Entity Saved...");

        //Fetching Entity
        entity = null;
        entity = session.get(DepartmentEntity.class, 50);
        System.out.println("Fetched Entity : "+entity);

        //Updating Entity
        entity.setLocation("Chennai");
        session.update(entity);
        System.out.println("Updating Entity..");
        //Fetching Entity
        entity = null;
        entity = session.get(DepartmentEntity.class, 50);
        System.out.println("Updated Entity : "+entity);

        //Deleting Entity
        session.delete(entity);
        System.out.println("Entity Deleted..");

        //Fetching Entity
        entity = null;
        entity = session.get(DepartmentEntity.class, 50);
        System.out.println("Fetched Entity : "+entity);
    }
}
