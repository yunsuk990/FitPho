const dotenv = require('dotenv');
const jwt = require('jsonwebtoken');

dotenv.config();

const ACCESS_TOKEN_SECRET = process.env.ACCESS_TOKEN_SECRET;
const REFRESH_TOKEN_SECRET = process.env.REFRESH_TOKEN_SECRET;

// Access 토큰 발행
const generateAccessToken = (email, password) => {
	const accessToken = jwt.sign({
		email,
		password
	}, ACCESS_TOKEN_SECRET, {
		expiresIn: "1d"
	});
	return accessToken;
}

// Refresh 토큰 발행
const generateRefreshToken = (email, password) => {
	const refreshToken = jwt.sign({
		email,
		password
	}, REFRESH_TOKEN_SECRET, {
		expiresIn: "15d"
	});
	return refreshToken;
}

module.exports.generateAccessToken = generateAccessToken;
module.exports.generateRefreshToken = generateRefreshToken;