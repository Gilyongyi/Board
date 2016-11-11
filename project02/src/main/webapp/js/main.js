//Handlebars source & template ( List )
var source = $('#tr-template').text();
var template = Handlebars.compile(source);
//Handlebars source & template ( reply )
var replySource = $('#reply-template').text();
var replyTemplate = Handlebars.compile(replySource);
//default pageNo, pageSize
var pageNo = 1;
var pageSize = 5;
loadBoards();	//게시물 리스트 function 호출

//게시물 리스트
function loadBoards() {
	$(document).scroll(function() {
		var scrollTop = $(this).scrollTop();		
		var documentHeight = $(document).height();	
		var windowHeight = $(window).height();
		if ( scrollTop == ( documentHeight - windowHeight )) {
			append();	//무한스크롤 데이터 추가
		}
	});
	
	$('#formDetail').hide();	//게시물 상세보기 숨기기
	$('#formPage').hide();		//글쓰기 숨기기
	$('.form-reply').hide();	//댓글쓰는 div 숨기기
	
	$('#boardTbl > tbody > tr').remove();	//리스트 리로딩 전 지우고 다시추가
	$.ajax({
		url: contextRoot + 'board/list.json',	//URL
		dataType: 'json',						//json data를 주고받음
		method: 'get',							//http protocol get 방식
		data :{
			pageNo : pageNo,
			pageSize : pageSize
		},
		success: function(result) {				//성공 시
			if (result.status != 'success') {
				alert('loadboards 실행 중 오류 발생!');
				return;
			}
			$('#totalCount').text('총 게 시 물 : '+result.data[0].totalCount+'개');		//게시물 총 갯수
			$('#resetBtn').click();				//reset 버튼
			$('#boardTbl > tbody').append(template(result));	//위에서 지운거 다시 추가
		},
		error: function() {
			alert('서버 요청 오류!');
		}
	});
}

//게시물 추가하기
$('#addBtn').click(function() {
	var upFile = $('input[name=upFile]')[0].files[0];
	if( $('#fContent').val() == null || $('#fContent').val() ==''){
		alert('내용을 입력하지 않았습니다.');
		return false;
	}else if($('#fTitle').val() == null || $('#fTitle').val() ==''){
		alert('제목을 입력하지 않았습니다.');
		return false;
	}else if($('#fUser').val() == null || $('#fUser').val() == ''){
		alert('작성자를 입력하지 않았습니다.');
		return false;
	}else if($('#fPwd').val() == null || $('#fPwd').val() ==''){
		alert('비밀번호를 입력하지 않았습니다.')
		return false;
	}
	
	if(upFile==null || upFile==""){
	$.ajax({
		url: contextRoot + 'board/add.json',	//URL
		method: 'post',							//http protocol post 방식
		data: {
			title:$('#fTitle').val(), 
			content:$('#fContent').val(),
			user:$('#fUser').val(),
			pwd:$('#fPwd').val()
		},
		dataType: 'json',						//json 데이터 주고받음
		success: function(result) {
			if (result.status != 'success') {
				alert('게시물 등록 오류입니다.');
				return;
			}
			$('#boardTbl > tbody').append(template(result));
			pageNo=1;
			loadBoards(); 
		},
		error: function() {
			alert('서버 요청 오류!');
		}
	});
	
	}else{
	var formData = new FormData();
	formData.append("title",$('input[name=title]').val());
	formData.append("content",$('textarea[name=content]').val());
	formData.append("user",$('input[name=user]').val());
	formData.append("pwd",$('input[name=pwd]').val());
	formData.append("upFile",upFile);
	
	$.ajax({
		type : "POST",
		url : contextRoot + 'board/add.do',
		data : formData,
		contentType: false,
		processData: false,
		success: function(result) {
			$('#boardTbl > tbody').append(template(result));
			pageNo=1;
			loadBoards();
			$('#formPage').hide();
		},
		error: function() {
			alert('서버 요청 오류!');
		}
	})
	}
	
});

//게시물 상세보기
$('#boardTbl').on('click', '.titleLink', function(event) {
	$('#detailEdit').hide();	//편집버튼 숨김
	event.preventDefault();
	$.getJSON(
			contextRoot + 'board/detail.json?no=' + $(this).attr('data-no'), //URL
			function(result) {
		    if (result.status != 'success') {
		    	alert('게시물 조회 오류!');
		    	return;
		    }
		    
		    $('.view-form').removeClass('y-hidden');	//번호,등록일 등 변경 불가능한 div 숨기기
		    $('.y-new-form').addClass('y-hidden');		//등록버튼 보이기
		    
		    //데이터 삽입 ( 편집시 기존 데이터 삽입 )
		    $('#fNo').text(result.data.no);			
		    $('#fTitle').val(result.data.title);
		    $('#fContent').val(result.data.content);
		    $('#fUser').val(result.data.user);
		    $('#fCreatedDate').text(result.data.createdDate);
		    $('#formDetail').show();
		    //데이터 삽입 ( 상세보기 데이터 삽입 ) 
		    $('#formPage').hide();
		    $('#fNoView').text(result.data.no);
		    $('#fTitleView').text(result.data.title);
		    $('#fContentView').text(result.data.content);
		    $('#fUserView').text(result.data.user);
		    $('#fCreatedDateView').text(result.data.createdDate);
		    $('#fImgView').attr("src","../file/"+result.data.fileName);
		    $('#fFileNameView').text(result.data.fileName);
		    $('#getBoardNo').attr("value",result.data.no);
		    $('#downloadBtn').attr("href","../file/"+result.data.fileName);
		    loadReplys();
		    $('.form-reply').show();
		    
		    scrollTop();
	});
});

$('#delBtn').click(function(event) {
  $.getJSON(
		  contextRoot + 'board/delete.json?no=' + $('#fNo').text(), 
      function(result) {
        if (result.status != 'success') {
          alert('댓글이 달린 게시물은 삭제 할 수 없습니다!');
          return;
        }
        alert('삭제');
        $('.view-form').addClass('y-hidden');
        $('.y-new-form').removeClass('y-hidden');
        $('#resetBtn').click();
        
        pageNo=1;
        loadBoards();
  });
});

$('#updBtn').click(function() {
	if( $('#fContent').val() == null || $('#fContent').val() ==''){
		alert('내용을 입력하지 않았습니다.');
		return false;
	}else if($('#fTitle').val() == null || $('#fTitle').val() ==''){
		alert('제목을 입력하지 않았습니다.');
		return false;
	}else if($('#fUser').val() == null || $('#fUser').val() == ''){
		alert('작성자를 입력하지 않았습니다.');
		return false;
	}
	$.post(contextRoot + 'board/update.json', {
      no: $('#fNo').text(),
      title:$('#fTitle').val(), 
      content:$('#fContent').val(),
      user:$('#fUser').val()
    }, function(result) {
      if (result.status != 'success') {
        alert('게시물 수정 오류입니다.');
        return;
      }
      
      $('.view-form').addClass('y-hidden');
      $('.y-new-form').removeClass('y-hidden');
      $('#resetBtn').click();
      pageNo=1;
      loadBoards();
      
    }, 'json');
});
$('#sWord').keyup(function(){
//$('#searchBtn').click(function(){
	$('#formDetail').hide();
	$('#formPage').hide();
	
	$('#boardTbl > tbody > tr').remove();	
	$.ajax({
		url: contextRoot + 'board/searchList.json',
		dataType: 'json',
		method: 'get',
		data :{
			sValue:$('#sValue').val(),
			sWord:$('#sWord').val()
		},
		success: function(result) {
			console.log(result.data);
			if (result.status != 'success') {
				alert('검색 중 오류 발생!');
				return;
			}
			$('#resetBtn').click(); 
			$('#boardTbl > tbody').append(template(result));
			$('#sWord').focus();
			
			$('.form-reply').hide();
			pageNo=1;
		},
		error: function() {
			alert('검색 서버 요청 오류!');
		}
	});
});

function loadReplys() {
	var no = $('#getBoardNo').val();
	$('.reply-slot > tr').remove();
	$.ajax({
		url: contextRoot + 'board/replyList.json?no='+no,
		dataType: 'json',
		method: 'get',
		success: function(result) {
			if (result.status != 'success') {
				alert('댓글 불러오기 중 오류 발생!');
				return;
			}
			$('.reply-slot').append(replyTemplate(result));
		},
		error: function() {
			alert('댓글 불러오기 서버 요청 오류!');
		}
	});
}

$('#addReplyBtn').click(function() {
	if( $('#replyUser').val() == null || $('#replyUser').val() ==''){
		alert('닉네임을 입력하지 않았습니다.');
		return false;
	}else if($('#replyContent').val() == null || $('#replyContent').val() ==''){
		alert('댓글을 입력하지 않았습니다.');
		return false;
	}
	$.ajax({
		url: contextRoot + 'board/addReply.json',
		method: 'post',
		data: {
			no: $('#getBoardNo').val(),
			ruser:$('#replyUser').val(), 
			rcontent:$('#replyContent').val(),
		},
		dataType: 'json',
		success: function(result) {
			if (result.status != 'success') {
				alert('댓글 등록 오류입니다.');
				return;
			}
			loadReplys(); 
		},
		error: function() {
			alert('댓글등록 서버 요청 오류!');
		}
	});
});

$(document).on('click','.delRpBtn',function() {
	var rno = $(this).parent().parent().attr('data-no')
	  $.getJSON(
		contextRoot + 'board/deleteReply.json?rno=' + rno, 
	      function(result) {
	        if (result.status != 'success') {
	          return;
	        }
	        
	        pageNo=1;
	        loadReplys();
	  });
	});

$('#fPwdView').on('keyup',function(){	
      var pwd = $('#fPwdView').val();
      var bno = $('#getBoardNo').val();
        $.ajax({
        	type : 'POST',
            url : contextRoot+'board/checkPwd.json',
            data:
            {
                pwd : pwd,
                bno : bno
            },
            success : function(result) {
                if (result.data==pwd) { 
					$('#detailEdit').show();
					$('.replyBtnTag').html('<button type="button" class="close" aria-label="Close" >'+
					'<span aria-hidden="true" id="delRpBtn" class="delRpBtn">&times;</span></button>');
					$('#detailEditFailed').hide();
                } else {
                	$('#detailEdit').hide();
                	$('.replyBtnTag').html('');
                	$('#detailEditFailed').show();
                }
            } 
        }); 
	}); 

$('#detailEdit').on('click',function(){
	$('#formDetail').hide();
	$('#formPage').show();
	$('.form-reply').hide();
	$('#fPwdEdit').hide();
})

$('#writeBtn').on('click',function(){
	$('#formDetail').hide();
	$('#formPage').show();
	$('#resetBtn').click();
	$('.form-reply').hide();
	$('#fPwdEdit').show();
	scrollTop();
	$('#fTitle').focus();
})

$('#sWord').on('keypress',function(e){
	if(e.which ==13){
		$('#searchBtn').click();
	}
})

$('#resetBtn').click(function() {
	$('#fNo').text("");
	$('#fCreatedDate').text("");
    $('#fUser').text("");
    //$('.form-reply').hide();
    
  $('.view-form').addClass('y-hidden');
  $('.y-new-form').removeClass('y-hidden');
});

/*스크롤 이동*/
$( window ).scroll( function() {
	if ($(this).scrollTop() > 200) {
	    $('.top').fadeIn();
	} else {
	    $('.top').fadeOut();
	  }
	});

$('.top').click( function() {
	event.preventDefault();
	scrollTop();
	});

function scrollTop(){
	$('html, body').animate({scrollTop:0},400);
	  return false;
}

//페이징 처리 (무한스크롤 (검색어 유/무) )
function append(){
	pageNo++;
	if($('#sWord').val()==null || $('#sWord').val()==''){
		$.ajax({
		url: contextRoot + 'board/list.json',	//URL
		dataType: 'json',						//json data를 주고받음
		method: 'get',							//http protocol get 방식
		data :{
			pageNo : pageNo,
			pageSize : pageSize
		},
		success: function(result) {				//성공 시
			console.log(result.data);
			if (result.status != 'success') {
				alert('실행 중 오류 발생!');
				return;
			}
			console.log(result.data);			//result 값 console
			$('#boardTbl > tbody').append(template(result));
		},
		error: function() {
			alert('서버 요청 오류!');
			}
		});
	}else{
		
		$.ajax({
			url: contextRoot + 'board/searchList.json',
			dataType: 'json',
			method: 'get',
			data :{
				pageNo : pageNo,
				pageSize : pageSize,
				sValue:$('#sValue').val(),
				sWord:$('#sWord').val()
			},
			success: function(result) {
				console.log(result.data);
				if (result.status != 'success') {
					alert('실행 중 오류 발생!');
					return;
				}
				$('#resetBtn').click(); 
				$('#boardTbl > tbody').append(template(result));
				$('#sWord').focus();
				
				//$('.form-reply').hide();
			},
			error: function() {
				alert('서버 요청 오류!');
			}
		});
	}
}
