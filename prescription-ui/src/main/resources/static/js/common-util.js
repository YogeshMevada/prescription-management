function generateUUID() {
    return uuidv4();
}

function generateAesEncryptedData(inputText, aesEncryptedKey) {
    return (CryptoJS.AES.encrypt(inputText, aesEncryptedKey)).toString();
}

function generateRsaEncryptedKey(input, publicEncKey) {
    const jSEncrypt = new JSEncrypt();
    jSEncrypt.setPublicKey(publicEncKey);
    return jSEncrypt.encrypt(input);
}

function setCsrfTokenInRequestHeader(xhr) {
	const authorizationToken = window.localStorage.getItem('authorization');
	xhr.setRequestHeader("csrf_token", authorizationToken);
}