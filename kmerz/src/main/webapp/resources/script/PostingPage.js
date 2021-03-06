function dropHandler(ev) {
  console.log('File(s) dropped');
  ev.stopPropagation();
  ev.preventDefault();
  var xhr = new XMLHttpRequest();
  var data = new FormData();
  var files = ev.dataTransfer.files;
  for(var i = 0; i < files.length; i++){
  data.append("files", files[i]);
  }
  xhr.open("POST", "/uploadFile");
  xhr.send(data);
  var videozone = document.getElementById("video_zone");
  xhr.onreadystatechange = function () {
        if (this.readyState == 4) {
        console.log(xhr.responseText);
            if (this.status == 200) { videozone.innerHTML += xhr.responseText; }
            if (this.status == 404) { videozone.innerHTML = "용량초과 or 확장자 지원 안함"; }
        }
  }
}
function dragOverHandler(ev) {
  ev.stopPropagation();
  ev.preventDefault();
}

function getCategoryInput(){
	var communityid = document.getElementById("community-input").value;
	var categoryinput = document.getElementById("category-input");
	var appendedcategory = document.getElementById("appended-category");
	includeHTML(categoryinput, 'include/setCategory?communityid=' + communityid);
}

// 글쓰기
function posting(){
	var content = document.getElementById("editable").innerHTML;
	var community_id = document.getElementById("community-input").value;
	console.log(community_id);
	var categoryinput = document.getElementById("category-input-val");
	console.log(categoryinput);
	var category_no = categoryinput.value;
	console.log(category_no);
	var post_title = document.getElementById("post_title").value;
	var textFile = null,
	// 글쓰기 내용을 파일로 변환
  makeTextFile = function (content) {
    var data = new File([content], "1.txt",{type: "text/plain", lastModified: Date.now()});
	return data;
  };
      upload(makeTextFile(content), community_id, category_no, post_title);
}

function upload(file, community_id, category_no, post_title){
		var data = new FormData();
		data.append("file", file);
		data.append("community_id", community_id);
		data.append("category_no", category_no);
		data.append("post_title", post_title);
		console.log(data);
		var xhr = new XMLHttpRequest();
		xhr.open("POST", "media/upload_media", false);
		xhr.send(data);
		var page = document.querySelector('.modal-section');
		page.innerHTML = xhr.responseText;
}
function openFILE_INSERT_Modal() {
  includeHTML(document.querySelector('.modal-section'), '/include/FILE_INSERT_MODAL');
}
function closeModal() {
  var modal = document.getElementById("myModal");
  modal.remove();
}
function includeHTML(divContainer, urlHTML) {
        let xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function () {
        if (this.readyState == 4) {
            if (this.status == 200) { divContainer.innerHTML = xhttp.responseText; }
            if (this.status == 404) { divContainer.innerHTML = "Page not found."; }
        }
    }	
    xhttp.open("GET", urlHTML, true);
    xhttp.send();
}
function insertLink(){
	var url = document.getElementById("url");
	var content = document.getElementById("editable");
	content.innerHTML += "<div><img draggable='false' src='" + url.value + "'><div>"
}

// 첨부파일을 넣고 '입력' 버튼을 눌렀을때
function insertMedia(){
	var source = document.getElementById("video_zone")
	var media = source.querySelectorAll("div");
	var content = document.getElementById("editable");
	content.innerHTML += "<div>" + source.innerHTML.trim() + "<div>";
}