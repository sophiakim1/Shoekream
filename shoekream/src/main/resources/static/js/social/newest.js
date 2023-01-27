window.onload = function(){
    const items = document.querySelectorAll('gnb_item');
    items.forEach((it) => {
        it.classList.remove('gnb_on');
    })
    const item = document.getElementById('st_gnb');
    item.classList.add('gnb_on');

    fetch("http://localhost:8889/api/social/newest/")
        .then((response) => response.json())
        .then((data) => {
            console.log(data)
            let feedList = "";
            for(let i=0;i <data.length;i++){
                feedList +=
                    `
                    <div class="feed_card item vertical" style="padding-top: 10px; position: absolute; left: ${Math.floor(i/5)*307}px; top: ${(i%5)*465}px">
                                    <a href="#" class="feed_each">
                                        <div class="card_box">
                                            <div class="social_img_box vertical">
                                                <picture class="picture social_img">
                                                    <source type="image/webp"
                                                        srcset="">
                                                    <source
                                                        srcset="">
                                                    <img alt="소셜이미지"
                                                        src="${data[i].img}"
                                                        loading="auto" class="image">
                                                </picture>
                                            </div>
                                            <div class="card_detail">
                                                <div class="user_box">
                                                    <picture class="picture img_profile">
                                                        <source type="image/webp"
                                                            srcset="">
                                                        <source
                                                            srcset="">
                                                        <img alt="사용자 프로필 이미지"
                                                            src="${data[i].memberDTO.imgUrl}"
                                                            loading="auto" class="image">
                                                    </picture>
                                                    <p class="user_name">${data[i].memberDTO.nickname}</p><span aria-label="좋아요"
                                                        role="button" class="btn like" >
                                                        <img id="like_icon" src="../../img/styleImg/like_icon.png" alt="좋아요 이미지"
                                                            class="icon sprite-icons social-like-gray-sm">
                                                        <span class="like_count">${data[i].lks.length}</span></span>
                                                </div>
                                                <p class="text_box">${data[i].content}` + ` #` + `${data[i].hashtag}</p>
                                            </div>
                                        </div>
                                    </a>
                                </div>
                    `
            }
            feedList = `<div class="gutter_item"></div>` + feedList;
            document.getElementById('masonry_posts').innerHTML = feedList;
        })
}