const bcrypt = require('bcryptjs');
const db = require('../db-connect');

module.exports = {
  findUserByEmail: async (email) => {
    try {
      const [users] = await db.execute('SELECT * FROM users WHERE email = ?', [email]);
      // Assuming that [users] is an array, return the first user or null if not found
      return users.length > 0 ? users[0] : null;
    } catch (error) {
      console.error('Error finding user by email:', error);
      throw error;
    }
  },

  createUser: async ({ name, email, password }) => {
    try {
      const hashedPassword = await bcrypt.hash(password, 10);
      const [result] = await db.execute(
        'INSERT INTO users (name, email, password) VALUES (?, ?, ?)',
        [name, email, hashedPassword]
      );

      const userId = result.insertId;
      return { id: userId, name, email };
    } catch (error) {
      console.error('Error creating user:', error);
      throw error;
    }
  },

  updateUserPassword: async (userId, newPassword) => {
    try {
      const hashedPassword = await bcrypt.hash(newPassword, 10);
      await db.execute('UPDATE users SET password = ? WHERE id = ?', [hashedPassword, userId]);
    } catch (error) {
      console.error('Error updating user password:', error);
      throw error;
    }
  },
};
