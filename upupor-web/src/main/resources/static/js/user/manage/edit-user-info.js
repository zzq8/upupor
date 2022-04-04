/*
 * MIT License
 *
 * Copyright (c) 2021-2022 yangrunkang
 *
 * Author: yangrunkang
 * Email: yangrunkang53@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

$(function () {
    // 激活右边内容按钮
    userLeftContentBtnActive();

    // 编辑用户信息
    editUserInfo();

});



function editUserInfo() {
    $("form").submit(function () {
        console.log("个人中心");
        let userName = $("#userName").val();
        let introduce = $("#introduce").val();
        let openEmail = $("#openEmail").val();

        let editData = {
            userName:userName,
            introduce:introduce,
            openEmail:openEmail,
        }

        $.cvPost('/member/edit', editData, function (data) {
            if (data.data.success) {
                $.cvSuccess("更新个人资料成功");
                setTimeout(function () {
                    history.go(0)
                }, 1600)
            } else {
                console.log('更新失败')
            }
        });

        return false;
    });
}

function userLeftContentBtnActive(){
    let path_name = window.location.pathname;
    console.log(path_name.split("/").slice(1).join("_"));
    $("." + path_name.split("/").slice(1).join("_")).addClass('active');
}
