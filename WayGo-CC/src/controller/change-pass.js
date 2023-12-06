const UserModel = require('../models/users');
const jwt = require('jsonwebtoken');
const bcrypt = require('bcryptjs');

const changePassword = async (request, h) => {
  const { userId, oldPassword, newPassword } = request.payload;

  try {
    // Validate user identity using JWT token
    const decodedToken = jwt.verify(request.headers.authorization, 'pl4nPl4n');
    if (decodedToken.id !== userId) {
      return h.response('Unauthorized').code(401);
    }

    const user = await UserModel.findUserById(userId);

    if (!user) {
      return h.response('User not found').code(404);
    }

    const isPasswordValid = await bcrypt.compare(oldPassword, user.password);

    if (isPasswordValid) {
      const newHashedPassword = await bcrypt.hash(newPassword, 10);
      await UserModel.updateUserPassword(userId, newHashedPassword);
      return h.response('Password changed successfully').code(200);
    } else {
      return h.response('Invalid old password').code(401);
    }
  } catch (error) {
    console.error(error.message);
    return h.response('Internal Server Error').code(500);
  }
};

module.exports = changePassword;
