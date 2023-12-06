const LoginController = require('../controller/login.js');
module.exports = [
  {
    method: 'POST',
    path: '/login',
    handler: LoginController,
  },
];
