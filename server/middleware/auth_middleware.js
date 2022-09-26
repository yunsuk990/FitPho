const dotenv = require('dotenv')
const jwt = require('jsonwebtoken');
dotenv.config();

const auth = (req, res, next) => {
  const KEY = process.env.SECRET_KEY;
  const accessToken = req.cookies.accessToken;

  if (!accessToken) {
    return res.status(403).json({
        message: "토큰 오류입니다."
    });
  }
  try {
    const decoded = jwt.verify(accessToken, KEY);
    req.email = decoded.email;
    req.password = decoded.password;
    return next();
  } catch {
    return res.status(403).json({
      message: "토큰 오류입니다."
  });
  }
};

module.exports = auth;