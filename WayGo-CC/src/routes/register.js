const RegisterController = require('../controller/register');


console.log('Register route defined');

module.exports = [
  {
    method: 'POST',
    path: '/register',
    handler: RegisterController,
    options: {
      auth: false,
      payload: {
        allow: 'application/json',
      },
    },
  },
];


