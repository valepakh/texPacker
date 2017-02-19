function test(packName) {
    loadPack(packName, arguments);
}

function drawIcon(packName, pack, iconName) {
    var icon = findIcon(pack, iconName);
    if (icon == undefined) return;
    var r = icon.rect;
    var div = document.createElement('div');
    div.style.width = r.width + "px";
    div.style.height = r.height + "px";
    var src = packName + ".png";
    div.style.backgroundImage = "url('" + src + "')";
    div.style.backgroundPosition = "-" + r.x + "px -" + r.y + "px";
    document.body.appendChild(div);
}

function loadPack(name, args) {
    loadJSON(name + ".json", function(response) {
        drawIcons(name, response, args);
    });
}

function drawIcons(packName, pack, args) {
    for (i = 1; i < args.length; i++) { // skip first arg
        drawIcon(packName, pack, args[i]);
    }
}

function findIcon(pack, name) {
    return pack.icons.find(function(icon) {
        return icon.path == name;
    });
}

function loadJSON(url, callback) {
    var xobj = new XMLHttpRequest();
    xobj.open('GET', url, true);
    xobj.responseType = "json";
    xobj.onload = function () {
        if (xobj.status == 200) {
            callback(xobj.response);
        }
    };
    xobj.send();
}
