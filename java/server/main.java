import static spark.Spark.*;

import com.google.gson.Gson;

// external dependencies should be passed in as function arguments
// this makes it easy to switch out implementations and improves testability
public void startServer(int port, Database db) {
  port(port);

  get("/users", (req, res) -> {
    try {
      // get the list of users from the database
      List<User> users = db.getUsers();
      res.type("application/json");
      return new Gson().toJson(users);
    } catch (Exception e) {
      // return a 500 error if there was an exception
      res.status(500);
      return e.getMessage();
    }
  });
}
