const jwt = require('jsonwebtoken');

require('dotenv').config();

const ACCESS_TOKEN_SECRET = process.env.ACCESS_TOKEN_SECRET;

// Access 토큰 유효성 검증
const verifyToken = (req, res, next) => {
    const authHeader = req.headers['authorization'];
    const token = authHeader && authHeader.split('=')[1];

    if (!token) {
        return res.status(401).json({
            success: "false",
            message: "토큰이 존재하지 않습니다."
        });
    }

    jwt.verify(token, ACCESS_TOKEN_SECRET, (err, data) => {
        if (err) {
            return res.status(403).json({
                success: "false",
                message: "토큰 인증 오류입니다."
            });
        }
        req.email = data.email;
        req.password = data.password;
        next();
    })
}

module.exports.verifyToken = verifyToken;