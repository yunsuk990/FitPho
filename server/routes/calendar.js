const express = require('express');
const router = express.Router();
const db = require('../config/db');

// 전체 일정 조회
router.get('/', function(req, res) {
    const email = req.email;

    var sql='select updateDate, tvTitle, tvDate, tvStart, tvEnd, tvContent from calendar where email=?';
    db.query(sql, [email], function (err, data, fields) {
        if(err) throw err;
        return res.status(200).json({
            success: "true",
            message: "전체 일정 조회에 성공했습니다.",
            data: data
        });
    })
})

// 일자별 일정 조회
router.get('/:date', function(req, res) {
    const email = req.email;
    const date = req.params.date;

    var sql='select updateDate, tvTitle, tvDate, tvStart, tvEnd, tvContent from calendar where email=? and tvDate=?'
    db.query(sql, [email, date], function (err, data, fields) {
        if(err) throw err;
        return res.status(200).json({
            success: "true",
            message: "일자별 일정 조회에 성공했습니다.",
            data: data
        });
    })
})

// 세부 일정 조회
router.get('/detail/:updateDate', function(req, res) {
    const updateDate = req.params.updateDate;
    
    var sql='select updateDate, tvTitle, tvDate, tvStart, tvEnd, tvContent from calendar where updateDate=?';
    db.query(sql, [updateDate], function (err, data, fields) {
        if(err) throw err;
        if (data.length === 0) {
			return res.status(400).json({
				success: "false",
				message: "캘린더에 등록되지 않은 일정입니다."
			});
		} else {
            return res.status(200).json({
            success: "true",
            message: "세부 일정 조회에 성공했습니다.",
            data: data
            });
        }
    })
})

// 일정 추가
router.post('/:updateDate', function(req, res) {
    const email = req.email;
    const updateDate = req.params.updateDate;

    var data = {
        tvTitle: req.body.tvTitle,
        tvDate: req.body.tvDate,
        tvStart: req.body.tvStart,
        tvEnd: req.body.tvEnd,
        tvContent: req.body.tvContent
    }

    var sql = 'insert into calendar values (?,?,?,?,?,?,?)';
    db.query(sql, [email, updateDate, data.tvTitle, data.tvDate, data.tvStart, data.tvEnd, data.tvContent], function (err, data, fields) {
        if(err) throw err;
        return res.status(200).json({
            success: "true",
            message: "일정 추가에 성공했습니다"
        });
    }) 
})   

// 일정 수정
router.patch('/:updateDate', function(req, res) {
    const email = req.email;
    const updateDate = req.params.updateDate;

    var data = {
        updateDate: req.body.updateDate,
        tvTitle: req.body.tvTitle,
        tvDate: req.body.tvDate,
        tvStart: req.body.tvStart,
        tvEnd: req.body.tvEnd,
        tvContent: req.body.tvContent
    }

    var sql = 'update calendar set updateDate=?, tvTitle=?, tvDate=?, tvStart=?, tvEnd=?, tvContent=? where email=? and updateDate=?';
    db.query(sql, [data.updateDate, data.tvTitle, data.tvDate, data.tvStart, data.tvEnd, data.tvContent, email, updateDate], function (err, data, fields) {
        if(err) throw err;
        if (data.affectedRows === 0) {
			return res.status(400).json({
				success: "false",
				message: "캘린더에 등록되지 않은 일정입니다."
			});
		} else {
            return res.status(200).json({
                success: "true",
                message: "일정 수정에 성공했습니다"
            });
        }
    }) 
})   

// 일정 삭제
router.delete('/:updateDate', function(req, res) {
    const email = req.email;
    const updateDate = req.params.updateDate;

    var sql='delete from calendar where email=? and updateDate=?';
    db.query(sql, [email, updateDate], function (err, data, fields) {
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