'use strict'
var inputLeftRoller = undefined;
var inputMiddleRoller = undefined;
var inputRightRoller = undefined;
var upperInput = undefined;
var lowerInput = undefined;
var RIGHT_ROLLER = undefined;

class Roller{
	constructor(input, nextRoller, offset){
		this.input = input;
		this.nextRoller = nextRoller;
		this.offset = offset;
		this.primeList = [];

		this.getCipheredCharIndex = function(){
			let currentCharacter = input.value;
			let charIndex = currentCharacter.charCodeAt() - 'A'.charCodeAt();
			let prime = this.primeList[(charIndex + this.offset) % this.primeList.length];
			let semiPrime = this.nextRoller == null ? 1 : this.nextRoller.getCipheredCharIndex();
			return prime * semiPrime;
		}
	}
}

function load(){
	upperInput = document.getElementById('upper-input');
	lowerInput = document.getElementById('lower-input');
	upperInput.oninput = callCipher;
	lowerInput.oninput = callCipher;
	for(const element of document.getElementsByTagName('input')){
		switch(element.type){
			case 'text':
				element.oninput = onInput;
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
	let roller3 = new Roller(inputLeftRoller, null, 3);
	let roller2 = new Roller(inputMiddleRoller, roller3, 2);
	let roller1 = new Roller(inputRightRoller, roller2, 1);
	RIGHT_ROLLER = roller1;

	let rollerList = [roller1, roller2, roller3];
	let predefinedPrimeList = [2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199, 211, 223, 227, 229, 233, 239, 241, 251, 257, 263, 269, 271, 277, 281, 283, 293, 307, 311, 313, 317, 331, 337, 347, 349, 353, 359, 367, 373, 379, 383, 389, 397];
	for(let index = 0; index < predefinedPrimeList.length; index++) {
		rollerList[index % 3].primeList.push(predefinedPrimeList[index]);
	}
}
function onInput(inputEvent){
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
	stepWheel(inputEvent.target, 0<window.scrollY);
}
function wheelUp(mouseEvent){
	stepWheel(mouseEvent.target);
}
function wheelDown(mouseEvent){
	stepWheel(mouseEvent.target, false);
}
function stepWheel(input, up=true){
	console.log(input);
	console.log(up);
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
	message.split('\n').forEach(subMessage => {
		if(cipheredMessage !== ''){
			cipheredMessage += '\n';
		}
		for(let index = 0; index < subMessage.length; index++){
			const character = subMessage[index];
			if(/[A-Z]/.test(character)){
				cipheredMessage += cipherCharacter(character);
			}else{
				cipheredMessage += character; // TODO: Fix later.
			}
		}
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