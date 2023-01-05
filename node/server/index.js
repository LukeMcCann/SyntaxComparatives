const express = require('express');
const app = express();

// external dependencies should be passed in as function arguments
// this makes it easy to switch out implementations and improves testability
function startServer(port, db) {
  app.listen(port, () => {
    console.log(`Server listening on port ${port}`);
  });

  app.get('/users', (req, res) => {
    db.getUsers((err, users) => {
      if (err) {
        res.status(500).send(err);
      } else {
        res.send(users);
      }
    });
  });
}

module.exports = {
  startServer
};
