import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

public class Server {
  // external dependencies should be passed in as function arguments
  // this makes it easy to switch out implementations and improves testability
  public static void startServer(int port, Database db) {
    try {
      // create and initialize the ORB
      ORB orb = ORB.init(new String[] {}, null);

      // get the root naming context
      org.omg.CORBA.Object objRef =
          orb.resolve_initial_references("NameService");
      NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

      // bind the object to the naming context
      String name = "Database";
      DatabaseServant servant = new DatabaseServant(db);
      ncRef.rebind(ncRef.to_name(name), servant);

      System.out.println("Server ready and waiting ...");

      // wait for requests
      orb.run();
    } catch (Exception e) {
      System.err.println("ERROR: " + e);
      e.printStackTrace(System.out);
    }
  }
}

// implementation of the Database interface
class DatabaseServant extends DatabasePOA {
  private final Database db;

  public DatabaseServant(Database db) {
    this.db = db;
  }

  public User[] getUsers() {
    return db.getUsers();
  }
}
