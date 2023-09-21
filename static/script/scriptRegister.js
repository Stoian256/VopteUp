const { modPow } = require('bigint-crypto-utils');

const hashedPassword = parseInt(CryptoJS.MD5(passwordVal).toString(),16)%1000;
const xx = CryptoJS.SHA256(passwordVal).toString();
console.log(xx);
console.log(hashedPassword)
const gBigInt = BigInt(g0);
// Converteste valoarea hash într-un număr BigInt
const xBigInt = BigInt(`0x${hashedPassword}`);
// Calculează Y = g ^ x
//const Y = gBigInt ** xBigInt;
const n = BigInteger('10000000')
const Y =bigintCryptoUtils.modPow(gBigInt, xBigInt, n);
//var Y = Math.pow(g0,parseInt(encryptedPassword.words[0].toString(16), 16));
console.log(Y)

const password = CryptoJS.MD5(Y).toString();
console.log(password)