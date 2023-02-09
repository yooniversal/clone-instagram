// (1) 스토리 로드하기
let page = 0;

function storyLoad() {
    $.ajax({
        url: `/api/post`,
        dataType: "json"
    }).done(res => {
        res.forEach((post) => {
            let postItem = getStoryItem(post);
            $("#feeds").append(postItem);
        });
    }).fail(error => {
        console.log("오류", error);
    });
}

storyLoad();

function getStoryItem(post) {
    let principalId = $("#principalId").val();
    let item = `
        <article>
            <header>
                <div class="profile-of-article">
                    <a href="/user/profile?id=${post.postUploader.id}"><img class="img-profile pic" src="/profile_imgs/${post.postUploader.profileImgUrl}" onerror="this.src='/img/default_profile.jpg'""></a>
                    <span class="userID main-id point-span" >${post.postUploader.name}</span>
                    <div class="subscribe__img post-text-area post-time">
                        <span>• ${diffentTime(post)}</span>
                    </div>
                </div>
            </header>
            <div class="main-image" style="border-bottom: 1px solid #ddd;">
                <img src="/upload/${post.postImgUrl}" class="mainPic">
            </div>
            <div class="icons-react">
                <div class="icons-left">`;
            if(post.likeState) {
                item += `<i class="fas fa-heart heart active" id="storyLikeIcon-${post.id}" onclick="toggleLikeHome(${post.id})"></i>
                         <span class="like-text" id="search-like-text" onclick="postLikeInfoModal(${post.id})">좋아요 <span id="likeCount-${post.id}" style="font-size:inherit;">${post.likeCount}</span>개</span>`;
            } else {
                item += `<i class="far fa-heart heart" id="storyLikeIcon-${post.id}" onclick="toggleLikeHome(${post.id})"></i>
                         <span class="like-text" id="search-like-text" onclick="postLikeInfoModal(${post.id})">좋아요 <span id="likeCount-${post.id}" style="font-size:inherit;">${post.likeCount}</span>개</span>`;
            }
            item += `
                </div>
            </div>
            <div class="reaction">
                <div class="text post-text-area">
	                <span>${post.text}</span>
                </div>
	            <div class="tag post-text-area">`;
                    let arr = post.tag.split(',');

                    for(let i = 0; i < arr.length; i++) {
                        item += `<span class="tag-span" onclick="location.href='/post/search/${arr[i]}'">#${arr[i]} </span>`;
                    }
                    item += `
                </div>
                <div class="comment-section" >
                <ul class="comments" id="storyCommentList-${post.id}">`;

                post.comments.forEach((comment)=>{
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
                    <input id="storyCommentInput-${post.id}" class="input-comment" type="text" placeholder="댓글 달기..." >
                    <button type="button" class="submit-comment" onClick="addComment(${post.id})">게시</button>
            </div>
        </article>`;
        return item;
}

function diffentTime(postInfoDto) {
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

// (2) 스토리 스크롤 페이징하기
$(window).scroll(() => {
    let checkNum = $(window).scrollTop() - ( $(document).height() - $(window).height() );
    //console.log(checkNum);

    if(checkNum < 1 && checkNum > -1){
        page++;
        storyLoad();
    }
});
//좋아요
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
                 </span>
                 <button onclick="deleteComment(${comment.id})" class="delete-comment-btn">
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