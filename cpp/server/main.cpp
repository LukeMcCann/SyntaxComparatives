#include <iostream>
#include <memory>
#include "http/server.hpp"

using namespace std;
using namespace http::server;

// external dependencies should be passed in as function arguments
// this makes it easy to switch out implementations and improves testability
void startServer(int port, shared_ptr<Database> db) {
  // create the server and set the callback for incoming requests
  Server server;
  server.set_callback([&db](Request& request, Response& response) {
    // handle the /users route
    if (request.get_path() == "/users") {
      try {
        // get the list of users from the database
        auto users = db->getUsers();

        // send the list of users in the response
        response.set_content(users, "application/json");
      } catch (const exception& e) {
        // return a 500 error if there was an exception
        response.set_status(500);
        response.set_content(e.what(), "text/plain");
      }
    } else {
      // return a 404 error for other routes
      response.set_status(404);
      response.set_content("Not Found", "text/plain");
    }
  });

  // start the server
  server.listen("localhost", port);
  cout << "Server listening on port " << port << endl;
}
