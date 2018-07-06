;(function ($) {
    $.extend($, {
        licenseplate: function (options) {
            var defaults = {
                //省键
                province: ["京", "沪", "浙", "苏", "粤", "鲁", "晋", "冀", "豫", "川", "渝", "辽", "吉", "黑", "皖", "鄂", "津", "贵", "云", "桂", "琼", "青", "新", "藏", "蒙", "宁", "甘", "陕",
                    "闽", "赣", "湘","OK","Del"],
                //数字字母键
                digital: ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "A", "S", "D", "F", "G", "H", "J", "K", "L",
                    "OK", "Z", "X", "C", "V", "B", "N", "M","OK","Del"],

                other: ["京", "沪", "浙", "苏", "粤", "鲁", "晋", "冀", "豫", "川", "渝", "辽", "吉", "黑", "皖", "鄂", "津", "贵", "云", "桂", "琼", "青", "新", "藏", "蒙", "宁", "甘", "陕",
                    "闽", "赣", "湘","港","澳","警","使","领","民","学","挂","试","超","0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "A", "S", "D", "F", "G", "H", "J", "K", "L",
                     "Z", "X", "C", "V", "B", "N", "M","中/英","OK","Del"],

                //初始化当前下标，车牌类型最大值 及车牌号
                current: 0,
                // currentlength:7,
                licenseplate: undefined,
                //车牌类型 0：蓝牌  1：绿牌  2：自定车牌
                licenseplatetype: "0",
                //容器id
                brandbox: "#brand-box",//车牌控件核心容器id
                // typebox:".type-box",//车牌类型按钮容器
                // inpurwarp:".inpur-warp",//输入块容器
                // inputtype:".input-type",//自定义输入容器
                // inputbox:".input-box",//绿牌蓝牌输入容器
                // prompt:".prompt-box",//错误提示容器
                // brandprovince:".brand-city",//车牌第一个“省”
                // brandcity:".brand-city",//车牌第二个“市”
                // inputactv:".actv",//当前正在输入的input 样式
                // tabactv:".tab-actv",//当前没类型样式  
                // keyboardbox: "#keyboard-box",  //键盘容器
                textArr: []
            };
            //合并初始参数及用户传参 优先使用用户传参
            options = $.extend({}, defaults, options);
            var cph="粤B00007";
            var textarr = options.textArr;
            var parentbox = options.brandbox;
            var sinoBritishText=false //中英文件盘标识 true:Chinese  false:English
            var inputLi = $(parentbox).find(".input-box li");
            inputLi.eq(options.current).addClass("actv").siblings().removeClass("actv");

            //*******************默认
            //开启省键盘
            showProvince();
            //车牌类型tab
            tabtypefn();
            //车牌类型输入框
            tabinput();

            //点击车牌类型切换
            $(parentbox).find(".type-box span").on("touchstart", function (event) {
                if(options.licenseplatetype != $(this).index()){//防止在当前类型点击类型清空输入框
                    options.licenseplatetype = $(this).index();
                    tabtypefn();
                    tabinput();
                    if (options.licenseplatetype > 1) {
                        closePro();
                        otherKey(); 
                        disableKey([33,34,35,36,37,38,39,40,,51,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76]); 
                        sinoBritish();                    
                    } else {
                        showProvince();
                    }
                }
                
                $("#prompt").html("");
            })

            //切换车牌类型
            function tabtypefn() {  
                emptyfn();
                var indxeId = options.licenseplatetype == "" ? 0 : options.licenseplatetype;
                indxeId = parseInt(indxeId);
                if (indxeId == undefined || indxeId == null) {
                    console.log("当前车牌类型不能为空或非数字" + indxeId);
                } else {
                    $(parentbox).find(".type-box").find("span").eq(indxeId).addClass("tab-actv").siblings("span").removeClass("tab-actv");
                }
            }

            //清空填写数组及输入框内容
            function emptyfn(){
                //有切换或提交成功才会调用此方法 所以 li的个数相差一个 二数组长度都8 所以循环都多一个 不需 lis.lenght-1
                var lis = $(parentbox).find(".input-box li");
                for (var i = 0; i < lis.length; i++) {
                    lis.eq(i).text("");
                    inputLi.eq(0).addClass("actv").siblings().removeClass("actv");
                    textarr[i] = "";
                };        
            }

            //蓝绿牌判断
            function tabinput() {
                var litype = options.licenseplatetype;
                var parentboxs = $(parentbox);        
                var li = parentboxs.find(".input-box").find("li");
                if (litype == 0) {
                    parentboxs.find(".input-box").removeClass("min-list");
                    li.eq(li.length - 1).hide();
                }else{
                    parentboxs.find(".input-box").addClass("min-list");
                    li.eq(li.length - 1).show();
                }                  
            }

            //点击焦点切换
            $(inputLi).on("touchstart", function (event) {
                changeCurrentIndex($(this).index());
                $("#prompt").html("");
            })

            //焦点切换方法
            function changeCurrentIndex(index) {
                $(inputLi).eq(index).addClass("actv").siblings("li").removeClass("actv");
                options.current = index;
                closePro();
                if(options.licenseplatetype==0 ){
                    if(index==0){                        
                        showProvince();  
                    }else if(index==1){                        
                        showKeybord();
                        disableKey([0,1,2,3,4,5,6,7,8,9,17,18]);
                    }else{                        
                        showKeybord();
                        disableKey([17,18]);
                    }
                }else if(options.licenseplatetype==1){
                    if(index==0){                        
                        showProvince();  
                    }else if(index==1){                        
                        showKeybord();
                        disableKey([0,1,2,3,4,5,6,7,8,9,17,18]);
                    }else if(index==2||index==7){                        
                        showKeybord();
                        disableKey([10,11,12,13,14,15,16,17,18,19,20,21,24,25,26,27,28,30,31,32,33,34,35,36]);
                    }else{
                        showKeybord();
                        disableKey([17,18]);                    }
                }else{
                    otherKey(); 
                    if(index==0){
                       disableKey([33,34,35,36,37,38,39,40,,51,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76]); 
                       sinoBritishText=false;
                       sinoBritish();          
                   }else if(index==1){
                       disableKey([0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40]); 
                       sinoBritishText=true;       
                       sinoBritish();
                   }else if(index>=2&&index<=5){
                        disableKey([0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,58,59]);
                        sinoBritishText=true;
                        sinoBritish();
                   }else if(index == 6){
                        sinoBritishText=false;
                        sinoBritish();
                        disableKey([0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,33,58,59]);      
                   }else{                       
                        disableKey([0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,34,35,36,37,38,39,40,41,42,43,44,45,
                            46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76]);
                        sinoBritishText=false;
                        sinoBritish();
                   }
                }
            }
            
            //删除 del
            function boardDel(){ 
                var text = $(inputLi).eq(options.current).text(); //当前选中的input值
                var indexs = options.current;////当前选中的input索引
                if(text!=''){//当前有值
                    $(inputLi).eq(options.current).text("");//删除当前值                    
                    textarr[indexs]=undefined;//删除数组 
                    console.log(textarr)
                }else{
                    if(indexs>0){
                        var _indexs = options.current-1;
                        changeCurrentIndex(_indexs);
                    }                    
                }
            }

            //OK方法
            function valueOk(){
                var tag = true;
                var len;
                if(options.licenseplatetype==0){
                    len = 7
                }else if(options.licenseplatetype>0){
                    len = 8;
                }

                for(var i = 0; i < len; i++){
                    if(options.licenseplatetype<2){
                        if(!!$(inputLi).eq(i).text()){//输入框不为空
                        }else{
                            tag = false;                        
                        }
                    }else{
                        if(i<7){
                            if(!!$(inputLi).eq(i).text()){
                            }else{
                                tag = false;                        
                            }
                        }                   
                    }                  
                }
                if(tag == true){
                    var submintdata=textarr.join("");  
                    if(submintdata==cph){
                        console.log('提交成功之后：'+ submintdata);
                        //跳转至付页面


                        
                        window.location.href="payment.html";
                    }else{
                        $("#prompt").html("未找到车牌<span>[ "+submintdata+" ]</span>的车辆，请确认车牌号！");
                    }                   
                }else{
                    console.log('请输入完整的车牌号');
                }
            }

            //关闭键盘
            function closePro() {
                $(parentbox).find("#keyboard-box ul").remove();
            }

            //循环生成省键盘
            function showProvince() {
                if ($('#keyboard-box').length == 0) {
                    $(parentbox).append('<div id="keyboard-box"></div>');
                }
                $('#keyboard-box').empty();
                var ss = "<ul class='ul_pro'>";
                for (var i = 0; i < options.province.length; i++) {
                    ss = ss +'<li><span>' + options.province[i] + '</span></li>';
                }
                ss += "<li class='li_close'><span>关闭</span></li></ul>"               
                $('#keyboard-box').append(ss);
            }

            //循环生成数字字母软键盘
            function showKeybord() {
                if ($('#keyboard-box').length == 0) {
                    $(parentbox).append('<div id="keyboard-box"></div>');
                }
                $('#keyboard-box').empty();
                var sz = "";
                for (var i = 0; i < options.digital.length; i++) {
                    sz += '<li class="ikey ikey' + i + ' ' + (i > 9 ? "li_zm" : "li_num") + '" ><span>' + options.digital[i] + '</span></li>'
                }
                    sz += "<li class='li_close'><span>关闭</span></li>";
                $("#keyboard-box").append("<ul class='ul_keybord'>" + sz + "</ul>");
            }

            //循环生成其他键盘
            function otherKey(){
                if ($('#keyboard-box').length == 0) {
                    $(parentbox).append('<div id="keyboard-box"></div>');
                }
                $('#keyboard-box').empty();
                var ss = "<ul class='ul_other'>";
                for (var i = 0; i < options.other.length; i++) {
                    ss += '<li class="' + i +'" ><span>' + options.other[i] + '</span></li>'                  
                }
                ss += "<li class='li_close'><span>关闭</span></li></ul>"
                $('#keyboard-box').append(ss);
            }

            //数字字母禁止键盘方法   
            function disableKey(keyarr){
                var keybox=$(parentbox).find("#keyboard-box ul.ul_keybord").find("li");    
                if(options.licenseplatetype == 2 ){//其它键盘时
                    var keybox=$(parentbox).find("#keyboard-box ul.ul_other").find("li");                  
                }
                for(var i=0; i<keyarr.length; i++){
                    var span=keybox.eq(keyarr[i]).find("span").addClass("disable");
                }               
            }  

            //点击键盘事件
            $('#keyboard-box').delegate('span:not(.disable)', 'touchstart', function () {
                var that = $(this);
                var indexs = that.parent().index();  
                var texts = that.text();
                if(options.licenseplatetype>1){//当车牌为其它车牌时
                    if(indexs==$('#keyboard-box ul li').length - 1){ //倒数第一个键为关闭键
                        //去除输入框焦点
                        $(inputLi).each(function(i,ele){
                            $(ele).removeClass("actv");
                        })
                        //关闭键盘
                        closePro();   
                    }else if(indexs==$('#keyboard-box ul li').length - 2){//倒数第二个键为关del键
                        //del方法
                        boardDel();
                    }else if(indexs==$('#keyboard-box ul li').length - 3){//倒数第三个键为关ok键
                        //OK方法
                        valueOk();
                    }else if(indexs==$('#keyboard-box ul li').length -4){//倒数第四个键为关中英文切换键
                        //中英切换
                        sinoBritishText=!sinoBritishText;
                        sinoBritish();
                    }else{//其他键均为输入键
                        //输入方法
                        setText(texts);
                    }
                }else{//当车牌为蓝牌或绿牌时
                    if(indexs==$('#keyboard-box ul li').length - 1){ //倒数第一个键为关闭键
                        //去除输入框焦点
                        $(inputLi).each(function(i,ele){
                            $(ele).removeClass("actv");
                        })
                        //关闭键盘
                        closePro();   
                    }else if(indexs==$('#keyboard-box ul li').length - 2){//倒数第二个键为关del键
                        //del方法
                        boardDel();
                    }else if(indexs==$('#keyboard-box ul li').length - 3){//倒数第三个键为关ok键
                        //OK方法
                        valueOk();
                    }else{//其他键均为输入键
                        //输入方法
                        setText(texts);
                    }
                }
                $("#prompt").html("");
            })

            //中英文切换
            function sinoBritish(){
                var keybox=$(parentbox).find("#keyboard-box ul.ul_other").find("li");
                if(sinoBritishText){
                    keybox.each(function(i,ele){ 
                        if(i<=40){
                            $(ele).hide();                            
                        }else {
                            if(i<77 ){
                                $(ele).show();  
                            }                           
                        }
                    })
                }else{
                    keybox.each(function(i,ele){                    
                        if(i>40 && i<77 ){
                            $(ele).hide();                             
                        }else{
                            if(i<77 ){
                                $(ele).show();  
                            }
                        }
                    })           
                }
            }       

            //设置键盘输入内容到文本框
            function setText(text) {
                var curli = $(parentbox).find('.input-box li.actv');
                var indexs = curli.index();
                if (options.licenseplatetype == 0 && indexs < 7) {
                    textarr[indexs] = text;
                    curli.text(text);
                    if (indexs < 6) {
                        changeCurrentIndex(curli.next().index());
                    }
                } else if (options.licenseplatetype == 1 && indexs < 8) {
                    textarr[indexs] = text;
                    curli.text(text);
                    if (indexs < 7) {
                        changeCurrentIndex(curli.next().index());
                    }
                }else if(options.licenseplatetype == 2 && indexs < 8){
                     textarr[indexs] = text;
                    curli.text(text);
                    if (indexs < 7) {
                        changeCurrentIndex(curli.next().index());
                    }
                }
                // console.log(textarr + options.licenseplatetype);
            }

            //点击提交
            $("#submit").on("click",function(){                
                valueOk();
            })
            //返回当前方法
            return this;
        }
    })
})(Zepto)