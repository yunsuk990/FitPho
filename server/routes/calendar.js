const express = require('express');
const router = express.Router();
const db = require('../config/db');

// 전체 일정 조회
router.get('/', function(req, res) {
    const email = req.email;

    var sql='select group_concat(distinct tvDate order by tvDate asc) as list from calendar where email=?';
    db.query(sql, [email], function (err, data, fields) {
        if(err) throw err;
        return res.status(200).json({
            success: "true",
            message: "전체 일정 조회에 성공했습니다.",
            data: data[0].list.split(',')
        });
    })
})

// 일자별 일정 조회
router.get('/:date', function(req, res) {
    const email = req.email;
    const date = req.params.date;

    var sql='select tvTitle, tvDate, tvStart, tvEnd, tvContent from calendar where email=? and tvDate=?'
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
router.get('/detail/:date/:tvStart', function(req, res) {
    const date = req.params.date;
    const start = req.params.tvStart;
    
    var sql='select tvTitle, tvDate, tvStart, tvEnd, tvContent from calendar where tvDate=? and tvStart=?';
    db.query(sql, [date, start], function (err, data, fields) {
        if(err) throw err;
        if (data.length === 0) {
			return res.status(400).json({
				success: "false",
				message: "캘린더에 등록되지 않은 일정입니다.",
                data: ""
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
router.post('/:date/:tvStart', function(req, res) {
    const email = req.email;

    var data = {
        tvTitle: req.body.tvTitle,
        tvDate: req.body.tvDate,
        tvStart: req.body.tvStart,
        tvEnd: req.body.tvEnd,
        tvContent: req.body.tvContent
    }

    var sql = 'insert into calendar values (?,?,?,?,?,?)';
    db.query(sql, [email, data.tvTitle, data.tvDate, data.tvStart, data.tvEnd, data.tvContent], function (err, data, fields) {
        if(err) throw err;
        return res.status(200).json({
            success: "true",
            message: "일정 추가에 성공했습니다"
        });
    }) 
})   

// 일정 수정
router.patch('/:date/:tvStart', function(req, res) {
    const email = req.email;

    const date = req.params.date;
    const start = req.params.tvStart;

    var data = {
        tvTitle: req.body.tvTitle,
        tvDate: req.body.tvDate,
        tvStart: req.body.tvStart,
        tvEnd: req.body.tvEnd,
        tvContent: req.body.tvContent
    }

    var sql = 'update calendar set tvTitle=?, tvDate=?, tvStart=?, tvEnd=?, tvContent=? where email=? and tvDate=? and tvStart=?';
    db.query(sql, [data.tvTitle, data.tvDate, data.tvStart, data.tvEnd, data.tvContent, email, date, start], function (err, data, fields) {
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
router.delete('/:date/:tvStart', function(req, res) {
    const email = req.email;

    const date = req.params.date;
    const start = req.params.tvStart;

    var sql='delete from calendar where email=? and tvDate=? and tvStart=?';
    db.query(sql, [email, date, start], function (err, data, fields) {
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