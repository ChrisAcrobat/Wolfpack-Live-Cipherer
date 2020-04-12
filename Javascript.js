'use strict'
const REGEX_UNSUPPORTED_CHARS = /[^A-Z0-9]/;
const REGEX_UNSUPPORTED_CIPHER_CHARS = /[^A-Z]/;

var inputLeftRoller = undefined;
var inputMiddleRoller = undefined;
var inputRightRoller = undefined;
var inputPrivateKey = undefined;
var upperInput = undefined;
var lowerInput = undefined;
var RIGHT_ROLLER = undefined;
var rollerList = undefined;

class Roller{
	constructor(input, nextRoller, offset){
		this.input = input;
		this.nextRoller = nextRoller;
		this.offset = offset;
		this.primeList = [];

		this.getCipheredCharIndex = ()=>{
			let currentCharacter = input.value;
			let charIndex = currentCharacter.charCodeAt() - 'A'.charCodeAt();
			let prime = this.primeList[(charIndex + this.offset) % this.primeList.length];
			let semiPrime = this.nextRoller == null ? 1 : this.nextRoller.getCipheredCharIndex();
			return prime * semiPrime;
		}
		this.step = (next=true)=>{
			let nextValue = this.input.value.charCodeAt() + (next ? 1 : -1);
			let pedningChar = String.fromCharCode(nextValue);
			this.input.value = nextValue <= 'Z'.charCodeAt() ? pedningChar : 'A';
			if(this.input.value === pedningChar){
				this.input.value = 'A'.charCodeAt() <= nextValue ? pedningChar : 'Z';
			}
			if(this.input.value != pedningChar && this.nextRoller != null){
				this.nextRoller.step(next);
			}
		}
		this.getState = ()=>{
			return (this.nextRoller == null ? "" : this.nextRoller.getState()) + this.input.value;
		}
		this.setState = (state='')=>{
			this.input.value = state[state.length-1];
			if(this.nextRoller != null){
				this.nextRoller.setState(state.substring(0, state.length-1));
			}
		}
	}
}

function load(){
	upperInput = document.getElementById('upper-input');
	lowerInput = document.getElementById('lower-input');
	inputPrivateKey = document.getElementById('use-private-key');
	inputPrivateKey.onchange = ()=>{cipher();};
	upperInput.oninput = callCipher;
	lowerInput.oninput = callCipher;
	for(const element of document.getElementsByTagName('input')){
		switch(element.type){
			case 'text':
				element.oninput = setWeel;
				element.onwheel = onScroll;
				element.dataset.previousValue = element.value;
				switch(element.parentElement.id){
					case 'first-wheel':  inputLeftRoller = element; break;
					case 'second-wheel': inputMiddleRoller = element; break;
					case 'third-wheel':  inputRightRoller = element; break;
				}
				break;
			case 'button':
				element.onclick = element.value === '▲' ? wheelUp : wheelDown;
		}
	}
	let roller_3 = new Roller(inputLeftRoller, null, 3);
	let roller_2 = new Roller(inputMiddleRoller, roller_3, 2);
	let roller_1 = new Roller(inputRightRoller, roller_2, 1);
	RIGHT_ROLLER = roller_1;

	inputLeftRoller.dataset.rollerListIndex = 2;
	inputMiddleRoller.dataset.rollerListIndex = 1;
	inputRightRoller.dataset.rollerListIndex = 0;

	rollerList = [roller_1, roller_2, roller_3];
	let predefinedPrimeList = [2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199, 211, 223, 227, 229, 233, 239, 241, 251, 257, 263, 269, 271, 277, 281, 283, 293, 307, 311, 313, 317, 331, 337, 347, 349, 353, 359, 367, 373, 379, 383, 389, 397];
	for(let index = 0; index < predefinedPrimeList.length; index++) {
		rollerList[index % 3].primeList.push(predefinedPrimeList[index]);
	}
}
function setWeel(inputEvent){
	let invalid = false;
	if(inputEvent.data){
		let input = inputEvent.data.toUpperCase().substring(inputEvent.data.length-1, 1);
		if(input.match(/[A-Z]/i)){
			inputEvent.target.value = input;
			inputEvent.target.dataset.previousValue = input;
			cipher();
		}else{
			invalid = true;
		}
	}else{
		invalid = true;
	}
	if(invalid){
		inputEvent.target.value = inputEvent.target.dataset.previousValue
	}
}
function onScroll(inputEvent){
	stepWheel(inputEvent.target, 0 < inputEvent.deltaY);
}
function wheelUp(mouseEvent){
	stepWheel(mouseEvent.target);
}
function wheelDown(mouseEvent){
	stepWheel(mouseEvent.target, false);
}
function stepWheel(input, up=true){
	rollerList[input.dataset.rollerListIndex].step(up);
}
function callCipher(inputEvent){
	cipher(inputEvent.target, inputEvent.target === upperInput ? lowerInput : upperInput);
}
function cipher(from=upperInput, too=lowerInput){
	// TODO: Get caretPosition
	from.value = from.value.toUpperCase();
	too.value = cipherMessage(from.value);
	// TODO: Set caretPosition
}
function cipherMessage(message){
	let cipheredMessage = '';
	let state = RIGHT_ROLLER.getState();
	message.split('\n').forEach(subMessage => {
		if(cipheredMessage !== ''){
			cipheredMessage += '\n';
		}
		for(let index = 0; index < subMessage.length; index++){
			const character = subMessage[index];
			if(/[A-Z]/.test(character)){
				RIGHT_ROLLER.step(true);
				cipheredMessage += cipherCharacter(character);
			}else{
				cipheredMessage += character.replace(REGEX_UNSUPPORTED_CHARS, ' ');
			}
			let privateKeyIsValid = cipheredMessage.replace(REGEX_UNSUPPORTED_CIPHER_CHARS, '') === cipheredMessage;
			let privateKeyIsLength = cipheredMessage.length == 3;
			if(inputPrivateKey.checked && privateKeyIsValid && privateKeyIsLength){
				RIGHT_ROLLER.setState(cipheredMessage);
			}
		}
		RIGHT_ROLLER.setState(state);
	});
	return cipheredMessage;
}
function cipherCharacter(character=''){
	let number = RIGHT_ROLLER.getCipheredCharIndex();
	let charactersToBeMapped = [];
	for(let c = 'A'.charCodeAt(); c <= 'Z'.charCodeAt(); c++){
		charactersToBeMapped.push(c);
	}
	let keyMap = [];
	while(0 < charactersToBeMapped.length){
		let cipheredCharacter_1 = charactersToBeMapped[number % charactersToBeMapped.length];
		removeFromArray(cipheredCharacter_1, charactersToBeMapped);
		let cipheredCharacter_2 = charactersToBeMapped[number % charactersToBeMapped.length];
		removeFromArray(cipheredCharacter_2, charactersToBeMapped);
		keyMap[cipheredCharacter_1] = cipheredCharacter_2;
		keyMap[cipheredCharacter_2] = cipheredCharacter_1;
	}
	return String.fromCodePoint(keyMap[character.charCodeAt()]);
}
function removeFromArray(item, array){
	let index = array.indexOf(item);
	if(-1 < index){
		array.splice(index, 1);
	}
}