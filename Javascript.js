'use strict'
var inputFirstWheel = undefined;
var inputSecondWheel = undefined;
var inputThirdWheel = undefined;
function load(){
	for(const element of document.getElementsByTagName('input')){
		switch(element.type){
			case 'text':
				element.oninput = onInput;
				element.onwheel = onScroll;
				element.dataset.previousValue = element.value;
				switch(element.parentElement.id){
					case 'first-wheel':  inputFirstWheel = element; break;
					case 'second-wheel': inputFirstWheel = element; break;
					case 'third-wheel':  inputFirstWheel = element; break;
				}
				break;
			case 'button':
				element.onclick = element.value === '+' ? wheelUp : wheelDown;
		}
	}
}
function onInput(inputEvent){
	let invalid = false;
	if(inputEvent.data){
		let input = inputEvent.data.toUpperCase().substring(inputEvent.data.length-1,1);
		if(input.match(/[A-Z]/i)){
			inputEvent.target.value = input;
			inputEvent.target.dataset.previousValue = input;
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
function stepWheel(wheel, up=true){
	console.log(wheel);
	console.log(up);
}