const express = require('express');
const router = express.Router();
const db = require('../config/db');

const lookUp = (startID, endID) => {
	return new Promise((resolve, reject) => {
		var sql = 'select * from library where id between ? and ?';
		db.query(sql, [startID, endID], function(err, data, fields) {
			if (err) reject(err);
			resolve(data);
		})
	})
}

// 전체 라이브러리 조회
router.get('/', function(req, res) {
	var sql = 'select * from library';
	db.query(sql, function(err, data, fields) {
		if (err) throw err;
		return res.status(200).json({
			success: "true",
			message: "전체 라이브러리 조회에 성공했습니다.",
			data: data
		});
	})
})

// 가슴 부위 라이브러리 조회
router.get('/chest', async function(req, res) {
	var data = await lookUp(1, 8);

	return res.status(200).json({
		success: "true",
		message: "부위별 라이브러리 조회에 성공했습니다.",
		data: data
	});
})

// 등 부위 라이브러리 조회
router.get('/back', async function(req, res) {
	var data = await lookUp(9, 19);

	return res.status(200).json({
		success: "true",
		message: "부위별 라이브러리 조회에 성공했습니다.",
		data: data
	});
})

// 팔 부위 라이브러리 조회
router.get('/arm', async function(req, res) {
	var data = await lookUp(20, 21);

	return res.status(200).json({
		success: "true",
		message: "부위별 라이브러리 조회에 성공했습니다.",
		data: data
	});
})

// 하체 부위 라이브러리 조회
router.get('/lower', async function(req, res) {
	var data = await lookUp(22, 30);

	return res.status(200).json({
		success: "true",
		message: "부위별 라이브러리 조회에 성공했습니다.",
		data: data
	});
})

// 어깨 부위 라이브러리 조회
router.get('/shoulder', async function(req, res) {
	var data = await lookUp(31, 43);

	return res.status(200).json({
		success: "true",
		message: "부위별 라이브러리 조회에 성공했습니다.",
		data: data
	});
})

// 기타 부위 라이브러리 조회
router.get('/etc', async function(req, res) {
	var data = await lookUp(44, 48);

	return res.status(200).json({
		success: "true",
		message: "부위별 라이브러리 조회에 성공했습니다.",
		data: data
	});
})

// 각 운동기구별 세부내용 조회
router.get('/detail/:equipmentID', function(req, res) {
	const equipmentID = req.params.equipmentID;

	var sql = 'select * from libraryDetail where id=?';
	db.query(sql, [equipmentID], function(err, data, fields) {
		if (err) throw err;
		return res.status(200).json({
			success: "true",
			message: "운동기구 세부내용 조회에 성공했습니다.",
			data: data
		});
	})
})

module.exports = router;