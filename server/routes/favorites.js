const express = require('express');
const router = express.Router();
const db = require('../config/db');

// 즐겨찾기 조회
router.get('/', function(req, res) {
	const email = req.email;

	var sql = 'select GROUP_CONCAT(equipmentID) as favoritesList from favorites where email=?';
	db.query(sql, [email], function(err, data, fields) {
		if (err) throw err;
		return res.status(200).json({
			success: "true",
			message: "즐겨찾기 목록 조회에 성공했습니다.",
			favorites: data[0].favoritesList
		});
	})
})

// 즐겨찾기 추가
router.post('/:equipmentID', function(req, res) {
	const email = req.email;
	const equipmentID = req.params.equipmentID;

	var sql = 'select * from favorites where email=? and equipmentID=?'
	db.query(sql, [email, equipmentID], function(err, data, fields) {
		if (err) throw err;
		if (data.length > 0) {
			return res.status(400).json({
				success: "false",
				message: "이미 즐겨찾기로 등록된 운동기구입니다."
			});
		} else {
			var sql = 'insert into favorites values (?,?)';
			db.query(sql, [email, equipmentID], function(err, data, fields) {
				if (err) throw err;
				return res.status(200).json({
					success: "true",
					message: "즐겨찾기 추가에 성공했습니다"
				});
			})
		}
	})
})

// 즐겨찾기 삭제
router.delete('/:equipmentID', function(req, res) {
	const equipmentID = req.params.equipmentID;

	var sql = 'delete from favorites where equipmentID=?';
	db.query(sql, [equipmentID], function(err, data, fields) {
		if (err) throw err;
		if (data.affectedRows == 0) {
			return res.status(400).json({
				success: "false",
				message: "즐겨찾기에 등록되지 않은 운동기구입니다."
			})
		} else {
			return res.status(200).json({
				success: "true",
				message: "즐겨찾기 삭제에 성공했습니다."
			})
		}
	})
})

module.exports = router;