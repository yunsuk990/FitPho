const nodemailer = require('nodemailer');
require('dotenv').config();

// 랜덤 인증번호 생성
const generateRandom = function(min, max) {
	var randomNum = Math.floor(Math.random() * (max - min + 1)) + min;
	return randomNum;
}

// 임시 비밀번호 생성
const generatePassword = () => {
	const chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz!@#$%^&*";
	const stringLength = 8;

	var randomString = "";
	for (let i = 0; i < stringLength; i++) {
		let randomNum = Math.floor(Math.random() * chars.length);
		randomString += chars.substring(randomNum, randomNum + 1);
	}

	return randomString;
}

const transport = nodemailer.createTransport({
	service: 'gmail',
	auth: {
		user: process.env.NODEMAILER_USER,
		pass: process.env.NODEMAILER_PASSWORD,
	},
	tls: {
		rejectUnauthorized: false
	}
});

// 이메일 인증
const authEmail = function(req, res) {
	const number = generateRandom(1111, 9999);

	const transmitEmail = req;

	const mailOptions = {
		from: "FitPho",
		to: transmitEmail,
		subject: "[FitPho] 인증 관련 이메일입니다.",
		text: "오른쪽 숫자 4자리를 입력해주세요 : " + number
	};

	// 인증번호 정상 전송 여부 확인
	transport.sendMail(mailOptions, (err, responses) => {
		if (err) throw err;
		transport.close();
	})

	return number;
}

// 이메일 인증 후 임시 비밀번호 발급
const temporary = function(req, res) {
	const temporaryPW = generatePassword();

	const transmitEmail = req;

	const mailOptions = {
		from: "FitPho",
		to: transmitEmail,
		subject: "[FitPho] 임시 비밀번호 발송 이메일입니다.",
		html: `${transmitEmail} 님<br />` +
			`임시 비밀번호 : ${temporaryPW}<br />` +
			`<a href="http://localhost:3000/">로그인하러 가기</a><br />` +
			`정보보호를 위해 로그인 후 비밀번호를 꼭 변경해주세요!`
	};

	// 인증번호 정상 전송 여부 확인
	transport.sendMail(mailOptions, (err, responses) => {
		if (err) throw err;
		transport.close();
	})

	return temporaryPW;
}

module.exports.authEmail = authEmail;
module.exports.temporary = temporary;