const express = require('express');
const bcrypt = require('bcrypt');
const dotenv = require('dotenv')
const jwt = require('jsonwebtoken');
const router = express.Router();
const db = require('../config/db');
const auth_middleware = require('../middleware/auth_middleware');
const secretKey = process.env.SECRET_KEY;

dotenv.config();

// 회원가입
router.post('/register', function(req, res) {
    var userData ={
        email: req.body.email,
        password: req.body.password,
        confirm_password: req.body.confirm_password
    };
    // check unique email address
    var sql='SELECT * FROM member WHERE email = ?';
    db.query(sql, [userData.email], async function (err, data) {
        if(err) throw err;
        if(data.length > 0) {
            res.status(400).json({
                success: "false",
                message: userData.email + "은 이미 존재하는 이메일 주소입니다."
            });
        } else if(userData.confirm_password != userData.password) {
            res.status(400).json({
                success: "false",
                message: "비밀번호와 비밀번호 재확인이 일치하지 않습니다."
            });
        } else {
            const salt = await bcrypt.genSalt(10);
            const hashPassword = await bcrypt.hash(userData.password, salt);

            var sql = 'INSERT INTO member values (?,?,?)';
            db.query(sql, [userData.email, hashPassword], function (err, data) {
                if (err) throw err;
            });
            res.status(200).json({
                success: "true",
                message: "회원가입에 성공하였습니다."
            });
        }
    })    
});

// 로그인
router.post('/login', function(req, res) {
    var email = req.body.email;
    var password = req.body.password;

    var sql='SELECT * FROM member WHERE email=?';
    db.query(sql, [email], async function (err, data) {
        if(err) throw err;
        if(data.length !== 0) {
            const email = data[0].email;
            const hashedPassword = data[0].password;
            const verified = await bcrypt.compare(password, hashedPassword);

            let token = "";

            if (verified) {
                token = jwt.sign({
                    type: "JWT",
                    email: email
                }, secretKey,
                {
                    expiresIn: "15m"
                });

                return res.status(200).json({
                    success: "true",
                    message: "로그인에 성공하였습니다. 토큰이 생성되었습니다.",
                    token: token
                });
            } else {
                return res.status(400).json({
                    success: "false",
                    message: "비밀번호가 일치하지 않습니다."
                });
            }
        } else {
            return res.status(400).json({
                success: "false",
                message: "이메일 또는 비밀번호가 존재하지 않습니다."
            });
        }
    })
});

// 회원탈퇴
router.post('/delete', (req, res) => {
    const token = req.body.token;
    var decoded = jwt.decode(token, secretKey);
    const email = decoded.email;

    var sql='DELETE FROM member WHERE email=?';
    db.query(sql, [email], async function (err, data) {
        if(err) throw err;
        if(data.length == 0) {
            res.status(400).json({
                success: "false",
                message: "회원탈퇴에 실패했습니다."
            })
        } else {
            res.status(200).json({
                success: "true",
                message: "회원탈퇴에 성공하였습니다."
            })
        }
    });
})

// 회원정보 수정 (비밀번호 변경)
router.post('/edit', (req, res) => {
    var token = req.body.token;
    var email = req.body.email;
    var password = req.body.password;
    var confirm_password = req.body.confirm_password;

    if (password == confirm_password) {
        var sql='select * from member where email=?';
        db.query(sql, [email], async function (err, data) {
            if(err) throw err;
            if(data.length !== 0) {
                if (password == confirm_password) {
                    const salt = await bcrypt.genSalt(10);
                    const hashPassword = await bcrypt.hash(password, salt);

                    var sql='update member set password=? where email=?';
                    db.query(sql, [hashPassword, email], async function (err, data) {
                        return res.status(200).json({
                            success: "true",
                            message: "비밀번호 변경에 성공했습니다."
                        });
                    })
                }
            } else {
                return res.status(400).json({
                    success: "false",
                    message: "해당 이메일이 존재하지 않습니다."
                });
            }
        })
    } else {
        return res.status(400).json({
            success: "false",
            message: "비밀번호와 비밀번호 재확인이 일치하지 않습니다."
        });
    }
})

// 토큰 검증
router.get("/payload", auth_middleware, (req, res) => {
    return res.status(200).json({
      code: 200,
      message: "토큰이 정상입니다."
    });
});

module.exports = router;