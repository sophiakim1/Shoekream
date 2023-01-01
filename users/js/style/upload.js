// function getImageFile(e){
//     const file = e.currentTarget.file;
//     const imgPreview = document.querySelector('.preview');
//     // if(!file.type.match("image/.*")){
//     //     alert('사진만 업로드 가능합니다.');
//     // }

//     const reader = new FileReader();
//     // reader.onload = (e) => {
//         const preview = createElement(e, file);
//         imgPreview.appendChild(preview);
//     // };
//     // reader.readAsDataURL(file);
// }

// function createElement(e, file){
//     console.log('createElement()');

//     const container = document.createElement('div');
//     const img = document.createElement('div');
//     const reader = new FileReader();
//     console.log(reader.readAsDataURL(e.result));
//     img.setAttribute('src', reader.readAsDataURL(e.result));

//     console.log(e.target.result);

//     img.setAttribute('data-file', file.name);
//     container.appendChild(img);
//     return container;
// }

// const imgUpload = document.getElementById('imgUpload');
// imgUpload.addEventListener('change', getImageFile);








// textarea 크기 자동조정
function resize(obj){
    obj.style.height = "1px";
    obj.style.height = (12+obj.scrollHeight)+"px";
}


//popup
function addProducts() {
    const popup = document.getElementById('follower_pop');
    popup.style.display = "block";
    const body = document.querySelector('body');
    body.style.overflow = 'hidden'
}

function closeSearch() {
    const popdown = document.getElementById('follower_pop');
    popdown.style.display = "none"
    const body = document.querySelector('body');
    body.style.overflow = 'unset'
}






// 이미지 미리보기
function readImage(input){

    // 인풋 태그에 파일이 있는 경우
    if(input.files && input.files[0]){
        const reader = new FileReader();

        reader.onload = e => {
            const previewImage = document.getElementById("previewImage");
            previewImage.src = e.target.result;
        }
        reader.readAsDataURL(input.files[0]);
    }
}






// change 이벤트
const inputImage = document.getElementById('imgUpload');
inputImage.addEventListener("change", e => {
    readImage(e.target);
})