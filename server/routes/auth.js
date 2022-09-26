const express = require('express');
const bcrypt = require('bcrypt');
const dotenv = require('dotenv')
const jwt = require('jsonwebtoken');
const router = express.Router();
const db = require('../config/db');
const auth_middleware = require('../middleware/auth_middleware');
const SECRET_KEY = process.env.SECRET_KEY;

dotenv.config();

// 회원가입
router.post('/register', async function(req, res) {
    var userData ={
        email: req.body.email,
        password: req.body.password
    };
        
    var salt = await bcrypt.genSalt(10);
    var hashPassword = await bcrypt.hash(userData.password, salt);

    var sql = 'insert into member values (?,?)';
    db.query(sql, [userData.email, hashPassword], function (err, data) {
        if (err) throw err;
        return res.status(200).json({
            success: "true",
            message: "회원가입에 성공하였습니다."
        });
    });     
})   

// 로그인
router.post('/login', function(req, res) {
    var email = req.body.email;
    var password = req.body.password;

    var sql='select * from member where email=?';
    db.query(sql, [email], async function (err, data) {
        if(err) throw err;
        if(data.length !== 0) {
            var email = data[0].email;
            var hashedPassword = data[0].password;
            var verified = await bcrypt.compare(password, hashedPassword);

            if (verified) {
                const accessToken = jwt.sign({
                    email: email,
                    password: hashedPassword
                }, SECRET_KEY, {
                    expiresIn: "30m"
                });

                const refreshToken = jwt.sign({
                    email: email,
                    password: hashedPassword
                }, SECRET_KEY, {
                    expiresIn: "1d"
                });

                return res
                .cookie('accessToken', accessToken, {httpOnly: true, maxAge: 1000000})
                .cookie('refreshToken', refreshToken, {httpOnly: true, maxAge: 1000000})
                .status(200).json({
                    success: "true",
                    message: "로그인에 성공하였습니다. 토큰이 생성되었습니다.",
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
                message: "존재하지 않는 이메일입니다."
            });
        }
    })
});

// 로그아웃
router.get("/logout", auth_middleware, (req, res) => {
    return res
      .clearCookie("accessToken")
      .status(200).json({ 
        success: "true",
        message: "로그아웃에 성공했습니다."
    });
});

// 회원탈퇴
router.post('/delete', auth_middleware, function(req, res) {
    const accessToken = req.cookies.accessToken;
    var decoded = jwt.decode(accessToken, SECRET_KEY);
    var email = decoded.email;

    var sql='delete from member where email=?';
    db.query(sql, [email], function (err, data) {
        console.log(data);
        if(err) throw err;
        if(data.affectedRows == 0) {
            return res.status(400).json({
                success: "false",
                message: "회원탈퇴에 실패했습니다."
            })
        } else {
            return res
            .clearCookie("accessToken")
            .status(200).json({
                success: "true",
                message: "회원탈퇴에 성공하였습니다."
            })
        }
    });
})

// 회원정보 수정 (비밀번호 변경) 
router.post('/edit', async function(req, res) {
    var token = req.cookies.accessToken;
    var decoded = jwt.decode(token, SECRET_KEY);

    var email = decoded.email;
    var old_password = req.body.old_password;
    var password = req.body.password;

    const verified = await bcrypt.compare(old_password, decoded.password);
    if (!verified) {
        return res.status(400).json({
            success: "false",
            message: "기존 비밀번호가 일치하지 않습니다."
        });
    } else {
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
})

module.exports = router;

// // DB 내 이메일 중복 확인
// router.get('/email', function(req, res) {
//     var email = req.body.email;

//     var sql='select * from member where email=?';
//     db.query(sql, [email], async function (err, data) {
//         if(err) throw err;
//         if(data.length > 0) {
//             return res.status(400).json({
//                 success: "false",
//                 message: email + "은 이미 존재하는 이메일 주소입니다."
//             });
//         } else {
//             return res.status(200).json({
//                 success: "true",
//                 message: email + "은 사용 가능한 이메일 주소입니다."
//             });
//         }
//     })
// })