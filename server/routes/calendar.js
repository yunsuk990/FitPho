const express = require('express');
const router = express.Router();
const db = require('../config/db');
const { verifyToken } = require('../middleware/auth_middleware');

// 전체 일정 조회
router.get('/', verifyToken, function(req, res) {
    const email = req.email;

    var sql='select * from calendar where email=?';
    db.query(sql, [email], function (err, data, fields) {
        if(err) throw err;
        return res.status(200).json({
            success: "true",
            message: "전체 일정 조회에 성공했습니다.",
            data: data[0]
        });
    })
})

// 단일 일정 조회
router.get('/:scheduleID', verifyToken, function(req, res) {
    let {scheduleID} = req.params;
    
    var sql='select * from calendar where scheduleID=?';
    db.query(sql, [scheduleID], function (err, data, fields) {
        if(err) throw err;
        return res.status(200).json({
            success: "true",
            message: "일정 조회에 성공했습니다.",
            data: data[0]
        });
    })
})

// 일정 추가
router.post('/:scheduleID', verifyToken, function(req, res) {
    const email = req.email;
    let {scheduleID} = req.params;

    var data = {
        start: req.body.start,
        end: req.body.end,
        part: req.body.part,
        color: req.body.color,
        memo: req.body.memo
    }

    var sql = 'insert into calendar values (?,?,?,?,?,?,?)';
    db.query(sql, [scheduleID, email, data.start, data.end, data.part, data.color, data.memo], function (err, data, fields) {
        if(err) throw err;
        return res.status(200).json({
            success: "true",
            message: "일정 추가에 성공했습니다"
        });
    }) 
})   

// 일정 수정
router.put('/:scheduleID', verifyToken, function(req, res) {
    let {scheduleID} = req.params;

    var data = {
        start: req.body.start,
        end: req.body.end,
        part: req.body.part,
        color: req.body.color,
        memo: req.body.memo
    }

    var sql = 'update calendar set start=?, end=?, part=?, color=?, memo=? where scheduleID=?';
    db.query(sql, [data.start, data.end, data.part, data.color, data.memo, scheduleID], function (err, data, fields) {
        if(err) throw err;
        return res.status(200).json({
            success: "true",
            message: "일정 수정에 성공했습니다"
        });
    }) 
})   

// 일정 삭제
router.delete('/:scheduleID', verifyToken, function(req, res) {
    let {scheduleID} = req.params;

    var sql='delete from calendar where scheduleID=?';
    db.query(sql, [scheduleID], function (err, data, fields) {
        if(err) throw err;
        if(data.affectedRows == 0) {
            return res.status(400).json({
                success: "false",
                message: "캘린더에 등록되지 않은 일정입니다."
            })
        } else {
            return res.status(200).json({
                success: "true",
                message: "일정 삭제에 성공했습니다."
            })
        }
    })
})

module.exports = router;