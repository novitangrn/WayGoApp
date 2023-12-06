// server.js
const Hapi = require('@hapi/hapi');
const HapiJwt = require('hapi-auth-jwt2');
const userController = require('./controller/login');
const db = require('./db-connect');

const init = async () => {
  const server = Hapi.server({
    port: 3000,
    host: 'localhost',
  });

  // Register JWT plugin
  await server.register(HapiJwt);

  // Set up the JWT authentication strategy
  server.auth.strategy('jwt', 'jwt', {
    key: 'pl4nPl4n', // You may want to consider using a more secure way to store your secret key
    validate: async (decoded, request, h) => {
      try {
        // Perform any checks on decoded JWT data
        // For example, check if the user still exists in your database
        const { id } = decoded;

        // Assuming you have a users table
        const [rows] = await db.execute('SELECT * FROM users WHERE id = ?', [id]);
        const userExists = rows.length > 0;

        if (!userExists) {
          return { isValid: false };
        }

        return { isValid: true };
      } catch (error) {
        console.error(error);
        return { isValid: false };
      }
    },
  });

  // Set the default authentication strategy to 'jwt'
  server.auth.default('jwt');

  // Routes
  server.route(require('./routes/login'));
  server.route(require('./routes/register'));
  // server.route(require('./routes/change-pass'));

  await server.start();
  console.log(`Server running at: ${server.info.uri}`);
};

process.on('unhandledRejection', (err) => {
  console.error(err);
  process.exit(1);
});

init();
