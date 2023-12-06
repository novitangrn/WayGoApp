const changePassword = require('../controller/change-pass.js');

module.exports = [
  {
    method: 'UPDATE',
    path: '/change-pass',
    handler: changePassword,
  },
];
