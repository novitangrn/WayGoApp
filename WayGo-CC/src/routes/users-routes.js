const userController = require('../controllers/userController');

const userRoutes = [
  {
    method: 'POST',
    path: '/register',
    handler: userController.register,
  },
  {
    method: 'POST',
    path: '/login',
    handler: userController.login,
  },
  {
    method: 'UPDATE',
    path: '/change-pass',
    handler: changePassword,
  }
];

module.exports = userRoutes;
