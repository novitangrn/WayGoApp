const UserModel = require('../models/users');
const jwt = require('jsonwebtoken');
const bcrypt = require('bcryptjs');

const login = async (request, h) => {
  const { email, password } = request.payload;
  console.log('Login request payload:', request.payload);

  // Validate that email is present
  if (!email || typeof email !== 'string' || email.trim() === '') {
    console.log('Invalid email detected');
    return h.response('Invalid email').code(400);
  }

  try {
    // Check if the user already exists
    const user = await UserModel.findUserByEmail(email);

    if (!user) {
      console.log('User not found for email:', email);
      return h.response('Invalid credentials').code(401);
    }

    // Check if the password is valid
    const isValidPassword = await bcrypt.compare(password, user.password);

    if (isValidPassword) {
      const token = jwt.sign({ id: user.id, email: user.email }, 'pl4nPl4n', { expiresIn: '1d' });
      return { token };
    } else {
      console.log('Invalid password for email:', email);
      return h.response('Invalid credentials').code(401);
    }
  } catch (error) {
    console.error('Error during login:', error.message);
    return h.response('Internal Server Error').code(500);
  }
};

module.exports = login;
