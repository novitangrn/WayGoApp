const UserModel = require('../models/users');
const jwt = require('jsonwebtoken');

const register = async (request, h) => {
  const { name, email, password } = request.payload;
  console.log('Registration request payload:', request.payload);

  // Validate name and email
  if (!name || !email || typeof name !== 'string' || typeof email !== 'string' || name.trim() === '' || email.trim() === '') {
    console.log('Invalid name or email detected');
    return h.response('Invalid name or email').code(400);
  }

  try {
    // Check if the user already exists
    const userExists = await UserModel.findUserByEmail(email);

    if (userExists) {
      return h.response('Email already taken').code(403);
    }

    // Create a new user
    const newUser = await UserModel.createUser({ name, email, password });

    // Generate JWT token
    const token = jwt.sign({ id: newUser.id, name: newUser.name, email: newUser.email }, 'pl4nPl4n', { expiresIn: '1d' });

    return { token };
  } catch (error) {
    console.error('Error during registration:', error.message);
    return h.response('Internal Server Error').code(500);
  }
};

module.exports = register;
