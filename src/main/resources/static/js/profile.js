function toggleSubscribe(toUserId, obj) {
    if ($(obj).text() === "팔로잉") {
        $.ajax({
            type: "delete",
            url: "/api/follow/" + toUserId,
        }).done(res => {
            $(obj).text("팔로우");
            // $(obj).toggleClass("blue");
        }).fail(error => {
            console.log("언팔로우 실패", error);
        });
    } else {
        $.ajax({
            type: "post",
            url: "/api/follow/" + toUserId,
        }).done(res => {
            $(obj).text("팔로잉");
            // $(obj).toggleClass("blue");
        }).fail(error => {
            console.log("팔로우 실패", error);
        });
    }
}

function followerInfoModalOpen(profileId) {
    $(".modal-follower").css("display", "flex");

    $.ajax({
        url: `/api/user/${profileId}/follower`,
        dataType: "json"
    }).done(res => {
        res.forEach((follow) => {
            let item = getfollowModalItem(follow);
            $("#followerModalList").append(item);
        });
    }).fail(error => {
        console.log("구독정보 불러오기 오류", error);
    });
}
function followingInfoModalOpen(profileId) {
    $(".modal-following").css("display", "flex");

    $.ajax({
        url: `/api/user/${profileId}/following`,
        dataType: "json"
    }).done(res => {
        res.forEach((follow) => {
            let item = getfollowModalItem(follow);
            $("#followingModalList").append(item);
        });
    }).fail(error => {
        console.log("구독정보 불러오기 오류", error);
    });
}

function getfollowModalItem(follow) {
    let item = `<div class="subscribe__item" id="subscribeModalItem-${follow.id}">
	<div class="subscribe__img">
		<a href="/user/profile?id=${follow.id}" ><img src="/profile_imgs/${follow.imageUrl}" onerror="this.src='/img/default_profile.jpg';" /></a>
	</div>
	<div class="subscribe__text">
		<h2>${follow.name}</h2>
	</div>
	<div class="subscribe__btn">`;
    if(!follow.loginUser){
        if(follow.follow){
            item += `<button class="cta cta-follow" onclick="toggleSubscribe(${follow.id}, this)">팔로잉</button>`;
        }else{
            item += `<button class="cta cta-follow" onclick="toggleSubscribe(${follow.id}, this)">팔로우</button>`;
        }
    }
    item += `
	</div>
</div>`;
    return item;
}

function popup(obj) {
    $(obj).css("display", "flex");
}

function postEditPopup(obj, postId) {
    $(obj).css("display", "flex");
    document.querySelector(obj).style.setProperty('--postId', postId);
}

function closePopup(obj) {
    $(obj).css("display", "none");
}

function reloadPage() {
    location.reload();
}

// 사용자 정보(회원정보, 로그아웃, 닫기) 모달
function modalInfo() {
    $(".modal-info").css("display", "none");
}

// (6) 사용자 프로파일 이미지 메뉴(사진업로드, 취소) 모달
function modalImage() {
    $(".modal-image").css("display", "none");
}

// (7) 구독자 정보 모달 닫기
function modalClose() {
    $(".modal-follower").css("display", "none");
    location.reload();
}
function modalClose() {
    $(".modal-following").css("display", "none");
    location.reload();
}

//포스트
function postPopup(postId, obj) {
    $(obj).css("display", "flex");

    $.ajax({
        url: "/api/post/" + postId,
        dataType: "json"
    }).done(res => {
        let item = getPostModalInfo(res, postId);
        $("#postInfoModal").append(item);

        let modal = document.getElementById(obj.toLocaleString().substring(1));
        modal.addEventListener("click", function(e){
            if (!$("#postInfoModal").is(e.target) && $("#postInfoModal").has(e.target).length === 0) {
                modal.style.display = "none";

                // 팝업된 내용 삭제
                $("#postInfoModal").empty();
            }
        });

    }).fail(error => {
        console.log("post 정보 불러오기 오류", error);
    });
}

let formData = new FormData();

function uploadPopup(obj) {
    $(obj).css("display", "flex");
    $(obj).css("background-color", "rgba(0, 0, 0, 0.3)");

    $.ajax({
        url: "/api/upload/",
        dataType: "json"
    }).done(res => {
        let item = uploadMainModalInfo();
        $("#uploadPostInfoModal").append(item);

        let fileSelectEle = document.getElementById('upload-file');
        fileSelectEle.onclick = function ()
        {
            $("#modal-upload-size").animate({
                width: 875,
                height: 578
            });

            $("#uploadPostInfoModal").animate({
                width: 875
            });

            // 기존 업로드 화면 삭제
            let remain_div = document.getElementById('post-box-full');
            remain_div.remove();

            // 상세 설명 적는 화면으로 전환
            let newItem = uploadModalInfo(res);
            $("#uploadPostInfoModal").append(newItem);

            // 공유하기 버튼 추가
            $("#modify-header").append(
                `<div class="share-post" id="share-post">
                    <button class="share-port-button" id="share-port-button" onclick="uploadToPost(formData)">
                        공유하기
                    </button>
                </div>`
            );
        }

    }).fail(error => {
        console.log("[post 팝업 업로드] 불러오기 오류", error);
    });
}

function uploadToPost(formData) {
    const textInput = document.getElementById("upload-text").value;
    const tagInput = document.getElementById("upload-tag").value;

    formData.append("uploadText", textInput);
    formData.append("uploadTag", tagInput);

    console.log(formData);
    console.log(formData.get("uploadImgUrl"))

    $.ajax({
        type: "post",
        url: "/api/upload/",
        data : formData,
        contentType : false,
        processData : false
    }).done(res => {
        console.log("[post 업로드] 전송 성공", res);
    }).fail(error => {
        console.log("[post 업로드] 불러오기 오류", error);
    });

    let uploadDiv = document.getElementById('uploadPostInfoModal');
    uploadDiv.remove();
    location.reload();
}

function uploadMainModalInfo() {
    let item = `
    <div class="modify-header" id="modify-header">
        <span>새 게시물 만들기</span> 
        <button class="exit" onclick="modalCancelClose()" id="upload-exit"><i class="fas fa-times"></i></button>
    </div>`;

    item += `
    <div class="post-box" id="post-box-full">
        <div class="upload-image-form" id="upload-logo">
            <img src="/img/upload-logo.png"/>
        </div>
        <div class="upload-image-description" id="upload-image">
            사진과 동영상을 여기에 끌어다 놓으세요
        </div>
        <label for="upload-file" id="upload-image-input">
          <div class="cta blue image-button" id="upload-image-button">컴퓨터에서 선택</div>
        </label>
        <input type="file" name="uploadImgUrl" id="upload-file" onchange="imageChoose(this)">
    </div>`;
    return item;
}

function uploadModalInfo(postInfoDto) {
    let item = `
    <div class="post-box" id="post-box-full" >
        <div class="upload-description">
            <!--업로드 Form-->
            <form class="upload-form" style="position: absolute; right: 10px;" method="post" id="upload-form" enctype="multipart/form-data">
              <!--프로필 정보-->
              <div class="upload-profile">
                <div class="profile-info">
                    <a href="/user/profile?id=${postInfoDto.postUploader.id}">
                    <img class="post-img-profile pic" src="/profile_imgs/${postInfoDto.postUploader.profileImgUrl}" onerror="this.src='/img/default_profile.jpg'" id="upload-user-image"">
                    </a>  
                    <span style="font-size:13px;">${postInfoDto.postUploader.name}</span>
                </div>
              </div>     
              <!--설명-->
              <div class="upload-description-after">
                <textarea class="input_upload" type="text" name="text" id="upload-text" rows="5"> </textarea>
              </div>
              <div class="upload-description-tag">
                <input type="text" class="tag-input" placeholder="태그. 공백 없이 ,로 구분하여 입력해주세요." name="tag" id="upload-tag" />
              </div>
              <!--설명 end-->
              <!-- 사진 -->
<!--              <input type="file" style="display: none;" id="uploadImgUrl"/>-->
              <!-- 사진 end -->
            </form>
            <!--업로드 Form end-->
        </div>
        <div class="image-preview">
            <img src="/img/default.jpg" alt="" id="imageUploadPreview" />
        </div>
    </div>`;
    return item;
}

function imageChoose(obj) {
    let f = obj.files[0];
    if (!f.type.match("image.*")) {
        alert("이미지를 등록해야 합니다.");
        return;
    }

    let reader = new FileReader();
    reader.onload = (e) => {
        $("#imageUploadPreview").attr("src", e.target.result);
        formData.append("uploadImgUrl", obj.files[0]);
    }
    reader.readAsDataURL(f); // 이 코드 실행시 reader.onload 실행됨.
}

function modalCancelClose() {
    $(".modal-upload-size").css("display", "none");
    location.reload();
}

function getPostModalInfo(postInfoDto, postId) {
    let diffentTime = function () {
        const currentTime = new Date();
        const postTimeStamp = new Date(postInfoDto.date);
        const postTimeInMillis = postTimeStamp.getTime();
        const postTime = new Date(postTimeInMillis);

        const timeDiff = currentTime - postTime;
        const msDiff = timeDiff;
        const secDiff = msDiff / 1000;
        const minDiff = secDiff / 60;
        const hourDiff = minDiff / 60;
        const dayDiff = hourDiff / 24;
        const monthDiff = dayDiff / 30;
        const yearDiff = monthDiff / 12;

        let timeAgo = "";
        if (yearDiff >= 1) {
            timeAgo = Math.floor(yearDiff) + "년 전";
        } else if (monthDiff >= 1) {
            timeAgo = Math.floor(monthDiff) + "개월 전";
        } else if (dayDiff >= 1) {
            timeAgo = Math.floor(dayDiff) + "일 전";
        } else if (hourDiff >= 1) {
            timeAgo = Math.floor(hourDiff) + "시간 전";
        } else if (minDiff >= 1) {
            timeAgo = Math.floor(minDiff) + "분 전";
        } else {
            timeAgo = "지금";
        }

        return timeAgo;
    }
    let principalId = $("#principalId").val();
    let item = `
    <div class="subscribe-header">
            <a href="/user/profile?id=${postInfoDto.postUploader.id}"><img class="post-img-profile pic" src="/profile_imgs/${postInfoDto.postUploader.profileImgUrl}" onerror="this.src='/img/default_profile.jpg'""></a>  
            <span>${postInfoDto.postUploader.name}</span> `;
    item += `<button class="exit" onclick="modalClose()"><i class="fas fa-times"></i></button>`
    if(postInfoDto.uploader) {
        item += `<button class="edit" onclick="postEditPopup('.post-edit-modal-info', ${postId})"><i class="far fa-edit"></i></button>`
    }
    item += `

    </div>
    <div class="post-box">
	    <div class="subscribe__img">
		    <img src="/upload/${postInfoDto.postImgUrl}" style="border-bottom: 1px solid #ddd;" />
	    </div>
	    <div class="post-div">
	    <div class="post-info">
	        <div class="text post-text-area"> `;
            if(postInfoDto.likeState) {
                item += `<i class="fas fa-heart heart active" id="storyLikeIcon" onclick="toggleLike(${postInfoDto.id})"></i>
                         <span class="like-text" onclick="postLikeInfoModal(${postInfoDto.id})">좋아요 <span id="likeCount" style="font-size:inherit;">${postInfoDto.likeCount}</span>개</span>`;
            } else {
                item += `<i class="far fa-heart heart" id="storyLikeIcon" onclick="toggleLike(${postInfoDto.id})"></i>
                         <span class="like-text" onclick="postLikeInfoModal(${postInfoDto.id})">좋아요 <span id="likeCount" style="font-size:inherit;">${postInfoDto.likeCount}</span>개</span>`;
            }
            item += `
            </div>
	        <div class="text post-text-area">
	            <span>${postInfoDto.text}</span>
            </div>
	        <div class="tag post-text-area">`;
                    let arr = postInfoDto.tag.split(',');

                    for(let i = 0; i < arr.length; i++) {
                        item += `<span class="tag-span" onclick="location.href='/post/search?tag=${arr[i]}'">#${arr[i]} </span>`;
                    }
                    item += `
            </div>
        </div>
        <div class="subscribe__img post-text-area">
            <span class="post-time">${diffentTime()}</span>
        </div>
        <div class="comment-section" >
                <ul class="comments" id="storyCommentList-${postInfoDto.id}">`;
                    postInfoDto.comments.forEach((comment)=>{
                    item += `<li id="storyCommentItem-${comment.id}">
                                <a href="/user/profile?id=${comment.userId}">
                                   <img class="comment-pic" src="/profile_imgs/${comment.imageUrl}" onerror="src='/img/default_profile.jpg'">
                                </a>
                                <span>
                                   <span class="comment-span point-span">${comment.name}</span>${comment.text}
                                </span>`;
                                if(principalId == comment.userId) {
                                    item += `<button onclick="deleteComment(${comment.id})" class="delete-comment-btn">
                                                <i class="fas fa-times"></i>
                                            </button>`;
                                }
                    item += `</li>`});
                item += `
                </ul>
            </div>
            </div>
            <div class="comment_input">
                    <input id="storyCommentInput-${postInfoDto.id}" class="input-comment-post" type="text" placeholder="댓글 달기..." >
                    <button type="button" class="submit-comment" onClick="addComment(${postInfoDto.id})">게시</button>
            </div>
        </div>
    </div>`;
    return item;
}
function toggleLike(postId) {
    let likeIcon = $("#storyLikeIcon");

    if (likeIcon.hasClass("far")) { // 좋아요
        $.ajax({
            type: "post",
            url: `/api/post/${postId}/likes`,
            dataType: "text"
        }).done(res =>{
            let likeCountStr = $("#likeCount").html();
            let likeCount = Number(likeCountStr) + 1;
            $("#likeCount").html(likeCount);

            likeIcon.addClass("fas");
            likeIcon.addClass("active");
            likeIcon.removeClass("far");
        }).fail(error=>{
            console.log("오류", error);
        });
    } else { // 좋아요 취소
        $.ajax({
            type: "delete",
            url: `/api/post/${postId}/likes`,
            dataType: "text"
        }).done(res =>{
            let likeCountStr = $("#likeCount").html();
            let likeCount = Number(likeCountStr) - 1;
            $("#likeCount").html(likeCount);

            likeIcon.removeClass("fas");
            likeIcon.removeClass("active");
            likeIcon.addClass("far");
        }).fail(error=>{
            console.log("오류", error);
        });
    }
}

function toggleLikeHome(postId) {
    let likeIcon = $(`#storyLikeIcon-${postId}`);

    if (likeIcon.hasClass("far")) { // 좋아요
        $.ajax({
            type: "post",
            url: `/api/post/${postId}/likes`,
            dataType: "text"
        }).done(res =>{
            let likeCountStr = $(`#likeCount-${postId}`).html();
            let likeCount = Number(likeCountStr) + 1;
            $(`#likeCount-${postId}`).html(likeCount);

            likeIcon.addClass("fas");
            likeIcon.addClass("active");
            likeIcon.removeClass("far");
        }).fail(error=>{
            console.log("[좋아요] 오류", error);
        });
    } else { // 좋아요 취소
        $.ajax({
            type: "delete",
            url: `/api/post/${postId}/likes`,
            dataType: "text"
        }).done(res =>{
            let likeCountStr = $(`#likeCount-${postId}`).html();
            let likeCount = Number(likeCountStr) - 1;
            $(`#likeCount-${postId}`).html(likeCount);

            likeIcon.removeClass("fas");
            likeIcon.removeClass("active");
            likeIcon.addClass("far");
        }).fail(error=>{
            console.log("[좋아요 취소] 오류", error);
        });
    }
}

//댓글 추가
function addComment(postId) {
    let commentInput = $(`#storyCommentInput-${postId}`);
    let commentList = $(`#storyCommentList-${postId}`);

    let data = {
        postId: postId,
        text: commentInput.val()
    }

    if (data.text === "") {
        alert("댓글을 작성해주세요!");
        return;
    }

    $.ajax({
        type: "post",
        url: "/api/comment",
        data: JSON.stringify(data),
        contentType: "application/json; charset=utf-8",
        dataType: "json"
    }).done(res=>{
        console.log("성공", res);
        let comment = res;
        let content = `
		    <li id="storyCommentItem-${comment.id}">
                 <a href="/user/profile?id=${comment.userId}">
                    <img class="comment-pic" src="/profile_imgs/${comment.imageUrl}" onerror="src='/img/default_profile.jpg'">
                 </a>
                 <span>
                    <span class="comment-span point-span">${comment.name}</span>${comment.text}
                 </span>`;
     content += `<button onclick="deleteComment(${comment.id})" class="delete-comment-btn">
                        <i class="fas fa-times"></i>
                 </button>
            </li>`;
        commentList.append(content);
    }).fail(error=>{
        console.log("오류", error);
        alert(error.responseText);
    });

    commentInput.val(""); // 인풋 필드를 깨끗하게 비워준다.
}

function deleteComment(commentId) {
    $.ajax({
        type: "delete",
        url: `/api/comment/${commentId}`
    }).done(res=>{
        console.log("성공", res);
        $(`#storyCommentItem-${commentId}`).remove();
    }).fail(error=>{
        console.log("오류", error);
    });
}

function wrapWindowByMask(){
    var maskHeight = $(document).height();
    var maskWidth = $(window).width();

    $('#mask').css({'width':maskWidth,'height':maskHeight});

    // $('#mask').fadeIn(1000);
    // $('#mask').fadeTo("slow",0.8);
}

function postModeToLike() {
    document.getElementById("profile-container-post").style.display = "none";
    document.getElementById("profile-container-like").style.display = "flex";

    document.querySelector(".post-btn").style.borderTop = "none"
    document.getElementById("post-text-list").style.color = "#8a8989";
    document.getElementById("grid-img").src = "/img/grid-gray.png";

    document.querySelector(".post-like-btn").style.borderTop = "1px solid #2a2a2a"
    document.getElementById("post-text-like").style.color = "black";
    document.getElementById("heart-img").src = "/img/heart.png";
}

function postModeToList() {
    document.getElementById("profile-container-like").style.display = "none";
    document.getElementById("profile-container-post").style.display = "flex";

    document.querySelector(".post-like-btn").style.borderTop = "none"
    document.getElementById("post-text-like").style.color = "#8a8989";
    document.getElementById("heart-img").src = "/img/heart-gray.png";

    document.querySelector(".post-btn").style.borderTop = "1px solid #2a2a2a"
    document.getElementById("post-text-list").style.color = "black";
    document.getElementById("grid-img").src = "/img/grid.png";
}

function notificationInfo() {
    $.ajax({
        url: `/api/user/notification/`,
        dataType: "json"
    }).done(res => {
        let item = ``
        res.forEach((notificationDto) => {
            item += getNotificationItem(notificationDto);
        });

        $("#notification-info").append(item);
    }).fail(error => {
        console.log("알림 정보 불러오기 오류", error);
    });
}
notificationInfo();

function getNotificationItem(notificationDto) {
    let msg = "profile-info";
    if(notificationDto.status == "FOLLOW") {
        msg = `<span style="font-weight: bold;font-size: 13px;">${notificationDto.fromUserName}</span>
                님이 회원님을 팔로우하기 시작했습니다.`;
    } else {
        msg = `<span style="font-weight: bold;font-size: 13px;">${notificationDto.fromUserName}</span>
                님이 회원님의 사진을 좋아합니다.`;
    }

    let info = "button";
    if(notificationDto.status == "FOLLOW") {
        info = `
                <div class="notification-profile-text" style="width: 240px; margin-right:5px;">
                    <span class="notification-profile-info">
                        ${msg}
                        <span class="sub-span-notification">${diffentTime(notificationDto.time)}</span>
                    </span>
                </div>
                <button class="cta notification-follow" onclick="toggleSubscribe(${notificationDto.fromUserId}, this)">
                    팔로잉
                </button>`
    } else {
        info = `<div class="notification-profile-text">
                    <span class="notification-profile-info">
                        ${msg}
                        <span class="sub-span-notification">${diffentTime(notificationDto.time)}</span>
                    </span>
                </div>
                <img class="notification-img" src="/upload/${notificationDto.postImageUrl}" />
                `
    }

    let item = `<div class="notification-friend-profiles">
                  <a href="/user/profile?id=${notificationDto.fromUserId}">
                    <img class="img-profiles pic" src="/profile_imgs/${notificationDto.fromUserImageUrl}" onerror="this.src='/img/default_profile.jpg';"/>
                  </a>
                  ${info}
                </div>
                `

    return item;
}

function diffentTime(date) {
    const currentTime = new Date();
    const postTimeStamp = new Date(date);
    const postTimeInMillis = postTimeStamp.getTime();
    const postTime = new Date(postTimeInMillis);

    const timeDiff = currentTime - postTime;
    const msDiff = timeDiff;
    const secDiff = msDiff / 1000;
    const minDiff = secDiff / 60;
    const hourDiff = minDiff / 60;
    const dayDiff = hourDiff / 24;
    const monthDiff = dayDiff / 30;
    const yearDiff = monthDiff / 12;

    let timeAgo = "";
    if (yearDiff >= 1) {
        timeAgo = Math.floor(yearDiff) + "년";
    } else if (monthDiff >= 1) {
        timeAgo = Math.floor(monthDiff) + "개월";
    } else if (dayDiff >= 1) {
        timeAgo = Math.floor(dayDiff) + "일";
    } else if (hourDiff >= 1) {
        timeAgo = Math.floor(hourDiff) + "시간";
    } else if (minDiff >= 1) {
        timeAgo = Math.floor(minDiff) + "분";
    } else {
        timeAgo = "지금";
    }

    return timeAgo;
}

let search_button_clicked = false;
let notification_button_clicked = false;

function toggleTab(status) {
    const spans = document.querySelectorAll(".nav-2 span")
    const logo_instagram = document.querySelector(".logo_instagram");
    const logo_instagram_icon = document.querySelector(".logo_instagram_icon");
    const common_icons = document.querySelectorAll(".common-icon");
    const search_box = document.querySelector(".search-box");
    const notification_box = document.querySelector(".notification-box");

    logo_instagram.removeEventListener('animationend', toggleTab);
    logo_instagram_icon.removeEventListener('animationend', toggleTab);

    if(status === 1) search_button_clicked = !search_button_clicked;
    else notification_button_clicked = !notification_button_clicked;

    if((status === 1 && search_button_clicked === true) || (status === 2 && notification_button_clicked === true)) {

        // nav 유지하고 추가되는 창만 교체
        if(status === 1 && notification_button_clicked === true) {
            notification_button_clicked = false;
            notification_box.style.animation = "slideReverse 0.5s ease-in-out forwards";

            search_box.style.display = "block";
            search_box.style.animation = "slide 0.5s ease-in-out forwards";
            return;
        } else if(status === 2 && search_button_clicked === true) {
            search_button_clicked = false;
            search_box.style.animation = "slideReverse 0.5s ease-in-out forwards";

            notification_box.style.display = "block";
            notification_box.style.animation = "slide 0.5s ease-in-out forwards";
            return;
        }

        $("#nav").animate({
            width: 66
        });

        // 메뉴 글자 가리기
        for (const span of spans) {
            span.style.display = 'none';
        }

        // instagram 글자 로고 -> 아이콘
        // instagram 글자 로고 서서히 사라짐
        logo_instagram.style.animation = "disappear 0.3s ease-in-out";

        // 글자 로고가 다 사라진 뒤 아이콘이 서서히 나타나도록 설정
        logo_instagram.addEventListener('animationend', function (e) {
            if((status === 1 && search_button_clicked === true) || (status === 2 && notification_button_clicked === true)) {
                logo_instagram.style.opacity = '0';
                logo_instagram_icon.style.opacity = '1';
                logo_instagram_icon.style.animation = "appear 0.3s ease-in-out";

                for (const common_icon of common_icons) {
                    common_icon.style.setProperty('--beforeWidth', '50px');
                    common_icon.style.setProperty('--beforeBorder', '1px solid #b2b2b2');
                }

                if(status === 1) {
                    search_box.style.display = "block";
                    search_box.style.animation = "slide 0.5s ease-in-out forwards";
                } else {
                    notification_box.style.display = "block";
                    notification_box.style.animation = "slide 0.5s ease-in-out forwards";
                }
            }
        }, { once : true});

    } else {

        $("#nav").animate({
            width: 245
        }, function() {
            // 메뉴 글자 복구
            for (const span of spans) {
                span.style.display = 'inline';
            }
        });

        // instagram 글자 로고 -> 아이콘
        // instagram 아이콘 서서히 사라짐
        logo_instagram_icon.style.animation = "disappear 0.3s ease-in-out";

        // 아이콘이 다 나타난 뒤 글자 로고가 서서히 나타나도록 설정
        logo_instagram_icon.addEventListener('animationend', function (e) {
            if((status === 1 && search_button_clicked === false) || (status === 2 && notification_button_clicked === false)) {
                logo_instagram_icon.style.opacity = '0';
                logo_instagram.style.opacity = '1';
                logo_instagram.style.animation = "appear 0.3s ease-in-out";

                for (const common_icon of common_icons) {
                    common_icon.style.setProperty('--beforeWidth', '210px');
                    common_icon.style.setProperty('--beforeBorder', '0');
                }

                if(status === 1) {
                    // search_box.style.display = "none";
                    search_box.style.animation = "slideReverse 0.5s ease-in-out forwards";
                } else {
                    // notification_box.style.display = "none";
                    notification_box.style.animation = "slideReverse 0.5s ease-in-out forwards";
                }
            }
        }, { once : true});
    }
}

function goSearch(input) {
    $.ajax({
        url: "/api/search/" + input,
        dataType: "json"
    }).done(searchDto => {

        // 검색 결과 초기화
        $("#search-result").empty()

        // 태그 정보
        let item = `
                    <div class="search-friend-profile"">
                        <a href="/post/search/${input}">
                            <img class="img-profile pic" src="/img/tag.png">
                        </a>
                        <div class="search-profile-text">
                            <span class="search-user-name">
                                #${input}
                            </span>
                            <span class="search-user-username">
                                게시물 ${searchDto.count}개
                            </span>
                        </div>
                    </div>
                    `;

        // 사용자 프로필
        searchDto.userDtos.forEach((userDto) => {
            item += getSearchItem(userDto);
        });

        $("#search-result").append(item);

    }).fail(error => {
        console.log("[search] 불러오기 오류", error);
    });
}

function getSearchItem(userDto) {
    let item = `
                <div class="search-friend-profile"">
                    <a href="/user/profile?id=${userDto.id}">
                        <img class="img-profile pic" src="/profile_imgs/${userDto.imageUrl}" onerror="src='/img/default_profile.jpg'">
                    </a>
                    <div class="search-profile-text">
                        <span class="search-user-name">
                            ${userDto.name}
                        </span>
                        <span class="search-user-username">
                            ${userDto.username}
                        </span>
                    </div>
                </div>
                `;

    return item;
}

function editPostPopup(obj) {
    let postId = window.getComputedStyle(document.querySelector('.post-edit-modal-info')).getPropertyValue('--postId');
    $(obj).css("display", "flex");
    $(obj).css("background-color", "rgba(0, 0, 0, 0.3)");

    $.ajax({
        url: "/api/post/" + postId,
        dataType: "json"
    }).done(res => {

        // 수정 화면
        let item = `<div class="modify-header" id="modify-header">
                        <span>정보 수정</span> 
                        <button class="exit" onclick="editModalCancelClose()" id="upload-exit"><i class="fas fa-times"></i></button>
                    </div>`

        item += uploadModalInfo(res);
        $("#editPostModal").append(item);
        document.getElementById("upload-text").value = res.text;
        document.getElementById("upload-tag").value = res.tag;
        document.getElementById("imageUploadPreview").src = "/upload/" + res.postImgUrl;

        $("#model-edit-post").animate({
            width: 875,
            height: 578
        });

        $("#editPostModal").animate({
            width: 875
        });

        // 완료 버튼 추가
        $("#modify-header").append(
            `<div class="share-post" id="share-post">
                <button class="share-port-button" id="share-port-button" onclick="editPostComplete(${postId})">
                    완료
                </button>
             </div>`
        );

    }).fail(error => {
        console.log("[post 수정 팝업] 불러오기 오류", error);
    });
}

function editPostComplete(postId) {
    const textInput = document.getElementById("upload-text").value;
    const tagInput = document.getElementById("upload-tag").value;

    let editFormData = new FormData();
    editFormData.append("id", postId);
    editFormData.append("text", textInput);
    editFormData.append("tag", tagInput);

    $.ajax({
        type: "post",
        url: "/api/edit/",
        data : editFormData,
        contentType : false,
        processData : false
    }).done(res => {
        console.log("[post 업데이트] 전송 성공", res);
    }).fail(error => {
        console.log("[post 업데이트] 오류", error);
    });

    document.getElementById('editPostModal').remove();
    location.reload();
}

function editModalCancelClose() {
    $(".model-edit-post").css("display", "none");
    location.reload();
}

function deletePost() {
    let postId = window.getComputedStyle(document.querySelector('.post-edit-modal-info')).getPropertyValue('--postId');

    $.ajax({
        type: "delete",
        url: "/api/post/delete/" + postId
    }).done(res => {
        console.log("[post 삭제] 성공");
    }).fail(error => {
        console.log("[post 삭제] 실패", error);
    });
}

function postLikeInfoModal(postId) {
    $("#likesOfPostModalList").empty();
    $(".modal-likes").css("display", "flex");

    $.ajax({
        url: `/api/post/likes/` + postId,
        dataType: "json"
    }).done(res => {
        res.forEach((likeDto) => {
            let item = getLikesOfPostModalItem(likeDto);
            $("#likesOfPostModalList").append(item);
        });
    }).fail(error => {
        console.log("[post 좋아요 정보] 불러오기 오류", error);
    });
}

function getLikesOfPostModalItem(likeDto) {
    let item = `<div class="subscribe__item" id="subscribeModalItem-${likeDto.userId}">
	<div class="subscribe__img">
		<a href="/user/profile?id=${likeDto.userId}" ><img src="/profile_imgs/${likeDto.userImageUrl}" onerror="this.src='/img/default_profile.jpg';" /></a>
	</div>
	<div class="subscribe__text">
		<h2>${likeDto.username}</h2>
	</div>
	<div class="subscribe__btn">`;
    if(!likeDto.login){
        if(likeDto.follow){
            item += `<button class="cta cta-follow" onclick="toggleSubscribe(${likeDto.userId}, this)">팔로잉</button>`;
        }else{
            item += `<button class="cta cta-follow" onclick="toggleSubscribe(${likeDto.userId}, this)">팔로우</button>`;
        }
    }
    item += `
	</div>
</div>`;
    return item;
}

function deleteUser(userId) {
    $.ajax({
        type: "delete",
        url: "/api/user/delete/" + userId,
    }).done(res => {
        console.log("[회원탈퇴] 성공");
    }).fail(error => {
        console.log("[회원탈퇴] 실패", error);
    });
}