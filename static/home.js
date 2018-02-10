var socket = io.connect('http://' + document.domain + ':' + location.port);

function get_data(text){
    socket.emit('channel_text_req', text);
    socket.on('channel_text_resp', function (data) {
        console.log(data);
        h1_tag = document.getElementById('result');
        h1_tag.innerHTML = data;
    });
}

function textAreaCallback(){
    console.log("herekjdbj fshkbg ksjbfk jabsdf");
    text_area = document.getElementById('input-area');
    text = text_area.value;
    get_data(text)
};

function initialise(){
    get_data();
}

$(document).ready(initialise);