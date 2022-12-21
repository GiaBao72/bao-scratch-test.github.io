const LOCAL = {
	get: (key) => JSON.parse(localStorage.getItem(key)),
	set: (key, value) => localStorage.setItem(key, JSON.stringify(value)),
	remove: (key) => localStorage.removeItem(key)
}

/**
* @param host is protocol://domain:port
* @param paths add at the end host
* @returns url connect to server
*/
function getLink(host, ...paths) {
	return !paths instanceof String ? host.concat('/', paths) : host.concat('/', paths).replaceAll(',', '/')
}

/**
* @param column of entity to find in array
* @param value of column
* @param array to get index
*/
function getIndex(column, value, array) {
	// # 0==false
	if (!value || !array) return -1;
	if (column) {
		for (let i = 0; i < array.length; i++)
			if (array[i][column] == value) return i;
	} else {
		for (let i = 0; i < array.length; i++)
			if (array[i] == value) return i;
	} return -1;
}

// _________________________________________________________________________________________________
/**
* <h3>show password form input</h3>
* @param element is input[type="password"] => change type to password or text
* @param eye is control to set type input
*/
function showPass(element, eye) {
	if (element.type == "text") {
		element.type = "password";
		if (eye) eye.setAttribute("class", "fa-solid fa-eye-slash");
	} else {
		element.type = "text";
		if (eye) eye.setAttribute("class", "fa-solid fa-eye");
	}
}

function setImage(input, toSet) {
	if (input.files.length > 0) toSet.src = URL.createObjectURL(input.files[0]);
}

/** _______________________________________________
 * @param e element
 * @param cls class
 * 
 */
function setClass(e, name) {
	let cls = e.getAttribute('class').trim();
	e.setAttribute("class", cls.search(name ? name : 'show') > -1
		? cls.replace(` ${name ? name : "show"}`, '')
		: (cls + ` ${name ? name : "show"}`)
	);
}

// _______________________________________________ QR Code
function generalQR(element, qrText, w, h) {
	if(!element) return;
	element.innerHTML = null;
	new QRCode(element, {
		text: qrText,
		width: w ? w : 128,
		height: h ? h : 128,
		colorDark: "#000000",
		colorLight: "#ff000000",
		correctLevel: QRCode.CorrectLevel.H
	})
}
this.generalQR(document.getElementById('qrCode'), "https://github.com/hoangit2k2/lovepink.git");


// set images
function setImage(input, toSet) {
    if(input.files.length>0) toSet.src = URL.createObjectURL(input.files[0]);
}
