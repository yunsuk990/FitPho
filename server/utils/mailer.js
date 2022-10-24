const nodemailer = require('nodemailer');
require('dotenv').config();

// 랜덤 인증번호 생성
const generateRandom = function(min, max) {
	var randomNum = Math.floor(Math.random() * (max - min + 1)) + min;
	return randomNum;
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

module.exports.authEmail = authEmail;